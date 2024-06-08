package com.crewing.applicant.service;

import com.crewing.applicant.dto.*;
import com.crewing.applicant.entity.Applicant;
import com.crewing.applicant.entity.Status;
import com.crewing.applicant.repository.ApplicantRepository;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.entity.Club;
import com.crewing.club.repository.ClubRepository;
import com.crewing.common.error.applicant.ApplicantAlreadyExistsException;
import com.crewing.common.error.applicant.ApplicantNotFoundException;
import com.crewing.common.error.club.ClubNotFoundException;
import com.crewing.member.dto.MemberInfoResponse;
import com.crewing.member.service.MemberServiceImpl;
import com.crewing.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<ApplicantCreateResponse> changeApplicantStatus(ApplicantsChangeStatusRequest request, User user) {
        // 운영진 여부 확인
        Club club = memberService.checking(request.getClubId(),user);
        List<Applicant> applicantList = applicantRepository.findAllByApplicantIdIn(request.getChangeList());
        List<Applicant> newApplicantList = new ArrayList<>();
        for(Applicant applicant : applicantList) {
            newApplicantList.add(applicant.toBuilder().status(Status.valueOf(request.getStatus())).build());
        }
        List<Applicant> result = applicantRepository.saveAll(newApplicantList);
        // 알림 기능
        String message = request.getMessage();
        return result.stream().map(this::getApplicantCreateResponse).toList();
    }

    @Override
    @Transactional
    public ApplicantListResponse getAllApplicantInfo(Pageable pageable, Long clubId, User user) {
        // 운영진 여부 확인
        Club club = memberService.checking(clubId,user);
        Sort sort = Sort.by(Sort.Order.asc("status").with(Sort.NullHandling.NULLS_LAST));
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"applicantId"));
        Page<Applicant> applicantPage = applicantRepository.findAllByClub(club,pageRequest);
        return toApplicantListResponse(applicantPage,applicantPage.getContent());
    }

    @Override
    @Transactional
    public ApplicantListResponse getAllStatusApplicantInfo(Pageable pageable, Long clubId, String status, User user) {
        // 운영진 여부 확인
        Club club = memberService.checking(clubId,user);
        Sort sort = Sort.by(Sort.Order.asc("status").with(Sort.NullHandling.NULLS_LAST));
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort.and(Sort.by(Sort.Direction.DESC,"applicantId")));
        Page<Applicant> applicantPage = applicantRepository.findAllByClubAndStatus(club,Status.valueOf(status),pageRequest);
        return toApplicantListResponse(applicantPage,applicantPage.getContent());
    }

    @Override
    @Transactional
    public void deleteApplicants(ApplicantsDeleteRequest applicantsDeleteRequest, User user) {
        // 운영진 여부 확인
        Club club = memberService.checking(applicantsDeleteRequest.getClubId(), user);
        applicantRepository.deleteByApplicantIdIn(applicantsDeleteRequest.getDeleteList());

        // 알림 기능
        String message = applicantsDeleteRequest.getMessage();
    }

    @Override
    @Transactional
    public MemberInfoResponse registerApplicants(ApplicantRegisterRequest applicantRegisterRequest, User user) {
        // 동아리 존재 여부 확인
        Club club = clubRepository.findById(applicantRegisterRequest.getClubId()).orElseThrow(ClubNotFoundException::new);
        // 사용자가 면접합격한 지원자인지 확인
        Applicant applicant = applicantRepository.findByUserAndClubAndStatus(user,club,Status.INTERVIEW).orElseThrow(ApplicantNotFoundException::new);
        // 동아리 회원으로 전환
        MemberInfoResponse memberInfoResponse = memberService.createMemberFromApplicant(club,user);
        // 지원자에서 삭제
        applicantRepository.delete(applicant);
        return memberInfoResponse;
    }

    @Override
    @Transactional
    public List<MyApplicantResponse> getAllMyApplicantClubInfo(User user) {
        List<Applicant> applicantList = applicantRepository.findAllByUser(user);
        List<MyApplicantResponse> myApplicantResponseList = new ArrayList<>();
        for(Applicant applicant : applicantList) {
            myApplicantResponseList.add(MyApplicantResponse.builder()
                            .clubId(applicant.getClub().getClubId())
                            .name(applicant.getClub().getName())
                            .profile(applicant.getClub().getProfile())
                            .build());
        }
        return myApplicantResponseList;
    }

    public ApplicantCreateResponse getApplicantCreateResponse(Applicant applicant) {
        MemberInfoResponse.UserInfo userInfo = MemberInfoResponse.UserInfo.builder()
                .userId(applicant.getUser().getId())
                .nickname(applicant.getUser().getNickname())
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
                .totalCnt(page.getTotalPages())
                .applicants(createResponseList)
                .build();
    }

}
