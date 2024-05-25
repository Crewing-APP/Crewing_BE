package com.crewing.club.service;

import com.crewing.club.dto.ClubCreateRequest;
import com.crewing.club.dto.ClubUpdateRequest;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.club.repository.ClubRepository;
import com.crewing.common.error.club.ClubAccessDeniedException;
import com.crewing.common.error.club.ClubNotFoundException;
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
    private final FileServiceImpl fileService;

    @Override
    @Transactional
    public Club createClub(ClubCreateRequest clubCreateRequest, User user, MultipartFile profile, List<MultipartFile> images) throws IOException {
        // 프로필 업로드
        String profileUrl=null;

        if(!profile.isEmpty())
            profileUrl = fileService.uploadFile(profile);
        // 동아리 생성
        Club result = clubRepository.save(Club.builder().
                name(clubCreateRequest.getName()).
                oneLiner(clubCreateRequest.getOneLiner()).
                introduction(clubCreateRequest.getIntroduction()).
                profile(profileUrl).
                application(clubCreateRequest.getApplication()).
                status(Status.UNDEFINED).
                category(clubCreateRequest.getCategory()).
                build());

        List<String> imageList;
        // 소개글 이미지들 업로드
        if(!images.isEmpty()) {
            imageList = fileService.uploadMultiFile(images);
            fileService.createClubFile(result, imageList);
        }
        // 매니저 임명
        memberRepository.save(Member.builder().
                user(user).
                club(result).
                role(Role.MANAGER).
                build());

        return result;

    }

    @Override
    @Transactional
    public Club updateClub(Long clubId, ClubUpdateRequest clubUpdateRequest, User user,
                           MultipartFile profile, List<MultipartFile> images, List<String> deletedImages) throws IOException {

        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        Member member = memberRepository.findByClubAndUserAndRole(club,user, Role.MANAGER).orElseThrow(ClubAccessDeniedException::new);

        String profileUrl = club.getProfile();
        if(!profile.isEmpty()){ //프로필 변경
            fileService.deleteFile(profileUrl); // 기존 프로필 삭제
            profileUrl = fileService.uploadFile(profile); // 새 프로필 업로드
        }

        List<String> imageList;
        // 소개글 추가된 이미지들 업로드
        if(!images.isEmpty()) {
            imageList = fileService.uploadMultiFile(images);
            fileService.createClubFile(club, imageList);
        }

        //삭제된 이미지 처리
        if(!deletedImages.isEmpty()){
            fileService.deleteMultiFile(deletedImages);
        }

        return clubRepository.save(club.toBuilder().
                name(clubUpdateRequest.getName()).
                oneLiner(clubUpdateRequest.getOneLiner()).
                introduction(clubUpdateRequest.getIntroduction()).
                application(clubUpdateRequest.getApplication()).
                profile(profileUrl).
                build());
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
    public Club changeStatus(Long clubId, User user, Status status) {
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        Club newClub = club.toBuilder().
                status(status).
                build();
        return clubRepository.save(newClub);
    }

}
