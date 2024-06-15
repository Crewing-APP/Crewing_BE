package com.crewing.club.service;

import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.club.repository.ClubRepository;
import com.crewing.common.error.club.ClubNotFoundException;
import com.crewing.common.error.user.UserAccessDeniedException;
import com.crewing.file.entity.ClubFile;
import com.crewing.member.entity.Member;
import com.crewing.member.repository.MemberRepository;
import com.crewing.review.repository.ReviewRepository;
import com.crewing.user.entity.Role;
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
public class ClubReadServiceImpl implements ClubReadService{
    private final ClubRepository clubRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    // 동아리 상세 정보 조회
    @Override
    @Transactional
    public ClubInfoResponse getClubInfo(Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        return toClubInfoResponse(club);
    }

    // 모든 동아리 정보 조회
    @Override
    @Transactional
    public ClubListResponse getAllClubInfo(Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByStatus(Status.ACCEPT,pageRequest);
        Page<ClubInfoResponse> clubInfoPages = clubPage.map(this::toClubInfoResponse);

        return getClubListResponse(clubInfoPages);
    }

    // 카테고리별 동아리 정보 조회
    @Override
    @Transactional
    public ClubListResponse getAllFilterClubInfo(Pageable pageable,int category) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByCategoryAndStatus(category, Status.ACCEPT, pageRequest);
        Page<ClubInfoResponse> clubInfoPages = clubPage.map(this::toClubInfoResponse);

        return getClubListResponse(clubInfoPages);
    }

    // 검색어를 통한 동아리 정보 조회
    @Override
    @Transactional
    public ClubListResponse getAllSearchClubInfo(Pageable pageable,String search) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByNameContainingAndStatus(search, Status.ACCEPT,pageRequest);
        Page<ClubInfoResponse> clubInfoPages = clubPage.map(this::toClubInfoResponse);
        return getClubListResponse(clubInfoPages);
    }

    @Override
    @Transactional
    public ClubListResponse getAllStatusClubInfo(Pageable pageable,String status, User user) {
        if(!user.getRole().equals(Role.ADMIN)){
            throw new UserAccessDeniedException();
        }
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByStatus(Status.valueOf(status), pageRequest);
        Page<ClubInfoResponse> clubInfoPages = clubPage.map(this::toClubInfoResponse);

        return getClubListResponse(clubInfoPages);
    }

    @Override
    @Transactional
    public ClubListResponse getAllMyClubInfo(Pageable pageable, User user) {
        List<Member> memberList = memberRepository.findAllByUser(user);
        List<Long> clubIds = new ArrayList<>();
        for(Member member : memberList){
            clubIds.add(member.getClub().getClubId());
        }
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByClubIdIn(clubIds, pageRequest);
        Page<ClubInfoResponse> clubInfoPages = clubPage.map(this::toClubInfoResponse);

        return getClubListResponse(clubInfoPages);
    }

    private static ClubListResponse getClubListResponse(Page<ClubInfoResponse> clubInfoPages) {
        return ClubListResponse.builder()
                .pageNum(clubInfoPages.getNumber())
                .pageSize(clubInfoPages.getSize())
                .totalCnt(clubInfoPages.getTotalPages())
                .clubs(clubInfoPages.getContent())
                .build();
    }

    private ClubInfoResponse toClubInfoResponse(Club club) {
        List<ClubFile> clubFileList = club.getClubFileList();
        List<ClubFile.ImageInfo> imageInfoList = clubFileList.stream().map(ClubFile::toDto).toList();
        return ClubInfoResponse.builder().
                name(club.getName()).
                clubId(club.getClubId()).
                oneLiner(club.getOneLiner()).
                introduction(club.getIntroduction()).
                reviewAvg(reviewRepository.findAverageRateByClubId(club).orElse(0f)).
                reviewNum(club.getReviewList().size()).
                profile(club.getProfile()).
                category(club.getCategory()).
                application(club.getApplication()).
                images(imageInfoList).
                status(club.getStatus()).
                isRecruit(club.getIsRecruit()).
                recruitStartDate(club.getRecruitStartDate()).
                recruitEndDate(club.getRecruitEndDate()).
                build();
    }

}
