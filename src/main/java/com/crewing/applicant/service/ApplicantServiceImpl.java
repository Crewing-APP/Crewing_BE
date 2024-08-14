package com.crewing.applicant.service;

import com.crewing.applicant.dto.*;
import com.crewing.applicant.entity.Applicant;
import com.crewing.applicant.entity.Status;
import com.crewing.applicant.repository.ApplicantRepository;
import com.crewing.club.dto.ClubListInfoResponse;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.entity.Club;
import com.crewing.club.repository.ClubRepository;
import com.crewing.common.error.applicant.ApplicantAlreadyExistsException;
import com.crewing.common.error.applicant.ApplicantNotFoundException;
import com.crewing.common.error.club.ClubNotFoundException;
import com.crewing.common.error.user.UserNotFoundException;
import com.crewing.member.dto.MemberInfoResponse;
import com.crewing.member.service.MemberServiceImpl;
import com.crewing.notification.entity.NotificationType;
import com.crewing.notification.entity.SSEEvent;
import com.crewing.notification.service.SSEService;
import com.crewing.review.repository.ReviewRepository;
import com.crewing.user.entity.User;
import com.crewing.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ApplicantServiceImpl implements ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final ClubRepository clubRepository;
    private final MemberServiceImpl memberService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ApplicantCreateResponse createApplicant(ApplicantCreateRequest request, User user) {
        // 동아리가 존재하는지 확인
        Club club = clubRepository.findById(request.getClubId()).orElseThrow(ClubNotFoundException::new);
        // 이미 존재하는 지원자인지 확인
        if(applicantRepository.findByUserAndClub(user,club).isPresent()) throw new ApplicantAlreadyExistsException();

        Applicant applicant = applicantRepository.save(Applicant.builder()
                .club(club)
                .user(user)
                .status(Status.WAIT)
                .build());
        return getApplicantCreateResponse(applicant);
    }

    @Override
    @Transactional
    public ApplicantCreateResponse createApplicantByManager(ApplicantEnrollRequest request, User manager) {
        // 사용자가 동아리 운영진인지 확인
        Club club = memberService.checking(request.getClubId(),manager);
        // 추가하려는 회원이 존재하는지 확인
        User user = userRepository.findById(request.getUserId()).orElseThrow(UserNotFoundException::new);
        // 이미 존재하는 지원자인지 확인
        if(applicantRepository.findByUserAndClub(user,club).isPresent()) throw new ApplicantAlreadyExistsException();

        Applicant applicant = applicantRepository.save(Applicant.builder()
                .club(club)
                .user(user)
                .status(Status.WAIT)
                .build());
        return getApplicantCreateResponse(applicant);
    }

    @Override
    @Transactional
    public List<ApplicantCreateResponse> changeApplicantStatus(ApplicantsChangeStatusRequest request, User user) {
        // 운영진 여부 확인
        Club club = memberService.checking(request.getClubId(),user);

        Status status = request.getStatus();
        List<Applicant> applicantList = applicantRepository.findAllByApplicantIdIn(request.getChangeList());
        List<Applicant> newApplicantList = new ArrayList<>();
        for(Applicant applicant : applicantList) {
            newApplicantList.add(applicant.toBuilder().status(status).build());
        }
        List<Applicant> result = applicantRepository.saveAll(newApplicantList);
        List<Applicant> applicants = applicantRepository.findAllByApplicantIdIn(request.getChangeList());
        // 알림 기능
        NotificationType notificationType = setNotificationType(status);
        if(notificationType!=null) {
            for (Applicant receiver : applicants) {
                applicationEventPublisher.publishEvent(new SSEEvent(notificationType,receiver.getUser(), setMessage(club), request.getContent(), receiver.getClub()));
            }
        }
        return result.stream().map(this::getApplicantCreateResponse).toList();
    }

    @Override
    @Transactional
    public ApplicantListResponse getAllApplicantInfo(Pageable pageable, Long clubId, User user) {
        // 운영진 여부 확인
        Club club = memberService.checking(clubId,user);
        Sort sort = Sort.by(Sort.Order.asc("status").with(Sort.NullHandling.NULLS_LAST));
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),sort.and(Sort.by(Sort.Direction.DESC,"applicantId")));
        Page<Applicant> applicantPage = applicantRepository.findAllByClub(club,pageRequest);
        return toApplicantListResponse(applicantPage,applicantPage.getContent());
    }

    @Override
    @Transactional
    public ApplicantListResponse getAllStatusApplicantInfo(Pageable pageable, Long clubId, Status status, User user) {
        // 운영진 여부 확인
        Club club = memberService.checking(clubId,user);
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.Direction.DESC,"applicantId");
        Page<Applicant> applicantPage = applicantRepository.findAllByClubAndStatus(club,status,pageRequest);
        return toApplicantListResponse(applicantPage,applicantPage.getContent());
    }

    @Override
    @Transactional
    public void deleteApplicants(ApplicantsDeleteRequest applicantsDeleteRequest, User user) {
        // 운영진 여부 확인
        Club club = memberService.checking(applicantsDeleteRequest.getClubId(), user);
        applicantRepository.deleteByApplicantIdIn(applicantsDeleteRequest.getDeleteList());
    }

    @Override
    @Transactional
    public MemberInfoResponse registerApplicants(ApplicantRegisterRequest applicantRegisterRequest, User user) {
        // 동아리 존재 여부 확인
        Club club = clubRepository.findById(applicantRegisterRequest.getClubId()).orElseThrow(ClubNotFoundException::new);
        // 사용자가 최종 합격한 지원자인지 확인
        Applicant applicant = applicantRepository.findByUserAndClubAndStatus(user,club,Status.INTERVIEW).orElseThrow(ApplicantNotFoundException::new);
        // 동아리 회원으로 전환
        MemberInfoResponse memberInfoResponse = memberService.createMemberFromApplicant(club,user);
        // 지원자에서 삭제
        applicantRepository.delete(applicant);
        return memberInfoResponse;
    }

    @Override
    @Transactional
    public List<ClubListInfoResponse> getAllMyApplicantClubInfo(User user) {
        List<Applicant> applicantList = applicantRepository.findAllByUser(user);
        return applicantList.stream().map(applicant ->
                ClubListInfoResponse.toClubListInfoResponse(applicant.getClub(),reviewRepository.findAverageRateByClubId(applicant.getClub()).orElse(0f))).toList();
    }

    public ApplicantCreateResponse getApplicantCreateResponse(Applicant applicant) {
        MemberInfoResponse.UserInfo userInfo = MemberInfoResponse.UserInfo.builder()
                .userId(applicant.getUser().getId())
                .name(applicant.getUser().getName())
                .imageUrl(applicant.getUser().getProfileImage())
                .build();
        return ApplicantCreateResponse.builder()
                .applicantId(applicant.getApplicantId())
                .clubId(applicant.getClub().getClubId())
                .createdDate(applicant.getCreatedDate())
                .lastModifiedDate(applicant.getLastModifiedDate())
                .user(userInfo)
                .status(applicant.getStatus())
                .build();
    }

    public ApplicantListResponse toApplicantListResponse(Page<Applicant> page,List<Applicant> applicantList) {
        List<ApplicantCreateResponse> createResponseList = applicantList.stream().map(this::getApplicantCreateResponse).toList();
        return ApplicantListResponse.builder()
                .pageNum(page.getNumber())
                .pageSize(page.getSize())
                .totalCnt(page.getTotalElements())
                .applicants(createResponseList)
                .build();
    }

    public String setMessage(Club club){
        return "연합동아리 "+club.getName()+"의 지원 결과가 발표되었습니다.";
    }

    public NotificationType setNotificationType(Status status){
        if(status.equals(Status.DOC)||status.equals(Status.DOC_FAIL))
            return NotificationType.DOC_RESULT;
        else if(status.equals(Status.INTERVIEW))
            return NotificationType.FINAL_RESULT_PASS;
        else if(status.equals(Status.FINAL_FAIL))
            return NotificationType.FINAL_RESULT_FAIL;
        else return null;
    }
}
