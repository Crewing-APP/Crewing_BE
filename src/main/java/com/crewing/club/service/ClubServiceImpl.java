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
                status(Status.WAIT).
                category(clubCreateRequest.getCategory()).
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
                           MultipartFile profile, List<MultipartFile> images, List<String> deletedImages) throws IOException {

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
        if(deletedImages!=null && !deletedImages.isEmpty()){
            log.info("delete image");
            fileService.deleteMultiFile(deletedImages);
        }

        Club result = clubRepository.save(club.toBuilder().
                name(clubUpdateRequest.getName()).
                oneLiner(clubUpdateRequest.getOneLiner()).
                introduction(clubUpdateRequest.getIntroduction()).
                application(clubUpdateRequest.getApplication()).
                profile(profileUrl).
                category(clubUpdateRequest.getCategory()).
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
    public ClubCreateResponse changeStatus(ClubChangeStatusRequest clubChangeStatusRequest,User user) {
        if(!user.getRole().equals(com.crewing.user.entity.Role.ADMIN)){
            throw new ClubAccessDeniedException();
        }
        Club club = clubRepository.findById(clubChangeStatusRequest.getClubId()).orElseThrow(ClubNotFoundException::new);
        Club newClub = clubRepository.save(club.toBuilder().
                status(Status.valueOf(clubChangeStatusRequest.getStatus())).
                build());
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
                .build();
    }

}
