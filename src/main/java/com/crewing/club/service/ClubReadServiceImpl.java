package com.crewing.club.service;

import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.entity.Club;
import com.crewing.club.repository.ClubRepository;
import com.crewing.common.error.club.ClubNotFoundException;
import com.crewing.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClubReadServiceImpl implements ClubReadService{
    private final ClubRepository clubRepository;
    private final ReviewRepository reviewRepository;
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
        Page<Club> clubPage = clubRepository.findAll(pageRequest);
        Page<ClubInfoResponse> clubInfoPages = clubPage.map(this::toClubInfoResponse);

        return getClubListResponse(clubPage,clubInfoPages);
    }

    // 카테고리별 동아리 정보 조회
    @Override
    @Transactional
    public ClubListResponse getAllFilterClubInfo(Pageable pageable,int category) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByCategory(category, pageRequest);
        Page<ClubInfoResponse> clubInfoPages = clubPage.map(this::toClubInfoResponse);

        return getClubListResponse(clubPage,clubInfoPages);
    }

    // 검색어를 통한 동아리 정보 조회
    @Override
    @Transactional
    public ClubListResponse getAllSearchClubInfo(Pageable pageable,String search) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByNameContaining(search, pageRequest);
        Page<ClubInfoResponse> clubInfoPages = clubPage.map(this::toClubInfoResponse);
        return getClubListResponse(clubPage, clubInfoPages);

    }

    private static ClubListResponse getClubListResponse(Page<Club> clubPage, Page<ClubInfoResponse> clubInfoPages) {
        return ClubListResponse.builder()
                .pageNum(clubPage.getNumber())
                .pageSize(clubPage.getSize())
                .totalCnt(clubPage.getTotalPages())
                .clubs(clubInfoPages)
                .build();
    }

    private ClubInfoResponse toClubInfoResponse(Club club) {
        return ClubInfoResponse.builder().
                name(club.getName()).
                clubId(club.getClubId()).
                oneLiner(club.getOneLiner()).
                introduction(club.getIntroduction()).
                reviewAvg(reviewRepository.findAverageRateByClubId(club)).
                reviewNum(club.getReviewList().size()).
                profile(club.getProfile()).
                category(club.getCategory()).
                application(club.getApplication()).
                fileList(null).
                build();
    }

}
