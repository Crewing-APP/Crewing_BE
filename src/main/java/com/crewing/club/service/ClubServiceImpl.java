package com.crewing.club.service;

import com.crewing.club.dto.*;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.club.repository.ClubRepository;
import com.crewing.common.error.club.ClubAccessDeniedException;
import com.crewing.common.error.club.ClubNotFoundException;
import com.crewing.file.entity.ClubFile;
import com.crewing.file.repository.ClubFileRepository;
import com.crewing.file.service.FileServiceImpl;
import com.crewing.member.entity.Member;
import com.crewing.member.entity.Role;
import com.crewing.member.repository.MemberRepository;
import com.crewing.notification.entity.NotificationType;
import com.crewing.notification.service.SSEService;
import com.crewing.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService{
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;
    private final ClubFileRepository clubFileRepository;
    private final FileServiceImpl fileService;
    private final SSEService sseService;

    @Override
    @Transactional
    public ClubCreateResponse createClub(ClubCreateRequest clubCreateRequest, User user, MultipartFile profile, List<MultipartFile> images) throws IOException {
        // 프로필 업로드
        String profileUrl=null;

        if(profile!=null&&!profile.isEmpty())
            profileUrl = fileService.uploadFile(profile);
        // 동아리 생성
        Club result = clubRepository.save(Club.builder().
                name(clubCreateRequest.getName()).
                oneLiner(clubCreateRequest.getOneLiner()).
                introduction(clubCreateRequest.getIntroduction()).
                profile(profileUrl).
                application(clubCreateRequest.getApplication()).
                status(Status.HOLD).
                category(clubCreateRequest.getCategory()).
                isRecruit(clubCreateRequest.getIsRecruit()).
                isOnlyStudent(clubCreateRequest.getIsOnlyStudent()).
                docDeadLine(clubCreateRequest.getDocDeadLine()).
                docResultDate(clubCreateRequest.getDocResultDate()).
                interviewStartDate(clubCreateRequest.getInterviewStartDate()).
                interviewEndDate(clubCreateRequest.getInterviewEndDate()).
                finalResultDate(clubCreateRequest.getFinalResultDate()).
                build());

        List<String> imageList;
        // 소개글 이미지들 업로드
        if(images!=null&&!images.isEmpty()) {
            imageList = fileService.uploadMultiFile(images);
            List<ClubFile> clubFiles = fileService.createClubFile(result, imageList);
        }

        // 매니저 임명
        memberRepository.save(Member.builder().
                user(user).
                club(result).
                role(Role.MANAGER).
                build());
        return toClubCreateResponse(result);

    }

    @Override
    @Transactional
    public ClubCreateResponse updateClub(Long clubId, ClubUpdateRequest clubUpdateRequest, User user,
                                         MultipartFile profile, List<MultipartFile> images, ClubUpdateRequest.DeletedImages deletedImages) throws IOException {

        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        Member member = memberRepository.findByClubAndUserAndRole(club,user, Role.MANAGER).orElseThrow(ClubAccessDeniedException::new);

        String profileUrl = club.getProfile();
        if(profile!=null&&!profile.isEmpty()){ //프로필 변경
            log.info("change profile");
            fileService.deleteFile(profileUrl); // 기존 프로필 삭제
            profileUrl = fileService.uploadFile(profile); // 새 프로필 업로드
        }

        List<String> imageList;
        // 소개글 추가된 이미지들 업로드
        if(images!=null&&!images.isEmpty()) {
            log.info("add image");
            imageList = fileService.uploadMultiFile(images);
            fileService.createClubFile(club, imageList);
        }

        //삭제된 이미지 처리
        if(deletedImages!=null && !deletedImages.getDeletedImages().isEmpty()){
            log.info("delete image");
            fileService.deleteMultiFile(deletedImages.getDeletedImages());
        }

        Club result = clubRepository.save(club.toBuilder().
                name(clubUpdateRequest.getName()).
                oneLiner(clubUpdateRequest.getOneLiner()).
                introduction(clubUpdateRequest.getIntroduction()).
                application(clubUpdateRequest.getApplication()).
                profile(profileUrl).
                category(clubUpdateRequest.getCategory()).
                isRecruit(clubUpdateRequest.getIsRecruit()).
                isOnlyStudent(clubUpdateRequest.getIsOnlyStudent()).
                docDeadLine(clubUpdateRequest.getDocDeadLine()).
                docResultDate(clubUpdateRequest.getDocResultDate()).
                interviewStartDate(clubUpdateRequest.getInterviewStartDate()).
                interviewEndDate(clubUpdateRequest.getInterviewEndDate()).
                finalResultDate(clubUpdateRequest.getFinalResultDate()).
                build());
        return toClubCreateResponse(result);
    }

    @Override
    @Transactional
    public void deleteClub(Long clubId, User user){
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        Member member = memberRepository.findByClubAndUserAndRole(club,user, Role.MANAGER).orElseThrow(ClubAccessDeniedException::new);

        clubRepository.delete(club);
    }

    @Override
    @Transactional
    public ClubCreateResponse changeStatus(ClubChangeStatusRequest request,User user) {
        if(!user.getRole().equals(com.crewing.user.entity.Role.ADMIN)){
            throw new ClubAccessDeniedException();
        }
        Status status = Status.valueOf(request.getStatus());
        Club club = clubRepository.findById(request.getClubId()).orElseThrow(ClubNotFoundException::new);
        List<Member> managerList = memberRepository.findAllByClubAndRole(club,Role.MANAGER);
        Club newClub = clubRepository.save(club.toBuilder().
                status(status).
                build());
        String message = setMessage(Status.valueOf(request.getStatus()));
        // 알림 기능
        NotificationType notificationType = null;
        if(status.equals(Status.ACCEPT)) notificationType = NotificationType.CLUB_ACCEPT;
        else if(status.equals(Status.RETURN)) notificationType = NotificationType.CLUB_RETURN;

        if(notificationType!=null) {
            for (Member manager : managerList) {
                sseService.send(manager.getUser(),notificationType, message, request.getContent(), club);
            }
        }
        return toClubCreateResponse(newClub);
    }

    public ClubCreateResponse toClubCreateResponse(Club club){
        List<ClubFile> clubFiles = clubFileRepository.findByClub(club);
        List<ClubFile.ImageInfo> imageInfoList = clubFiles.stream().map(ClubFile::toDto).toList();
        return ClubCreateResponse.builder()
                .clubId(club.getClubId())
                .name(club.getName())
                .oneLiner(club.getOneLiner())
                .introduction(club.getIntroduction())
                .profile(club.getProfile())
                .images(imageInfoList)
                .category(club.getCategory())
                .application(club.getApplication())
                .status(club.getStatus())
                .isRecruit(club.getIsRecruit())
                .isOnlyStudent(club.getIsOnlyStudent())
                .docDeadLine(club.getDocDeadLine())
                .docResultDate(club.getDocResultDate())
                .interviewStartDate(club.getInterviewStartDate())
                .interviewEndDate(club.getInterviewEndDate())
                .finalResultDate(club.getFinalResultDate())
                .build();
    }

    public String setMessage(Status status){
        if(status.equals(Status.ACCEPT)){
            return "연합동아리 등록이 승인되었습니다.";
        }
        else if(status.equals(Status.RETURN)){
            return "연합동아리 등록이 반려되었습니다.";
        }
        else{
            return "연합동아리 등록이 보류되었습니다.";
        }
    }

}
