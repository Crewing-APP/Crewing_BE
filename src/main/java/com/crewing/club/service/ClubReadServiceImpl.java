package com.crewing.club.service;

import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.entity.Club;
import com.crewing.club.repository.ClubRepository;
import com.crewing.common.error.EntityNotFoundException;
import com.crewing.common.error.ErrorCode;
import com.crewing.common.error.club.ClubNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClubReadServiceImpl implements ClubReadService{
    private final ClubRepository clubRepository;

    // 동아리 상세 정보 조회
    @Override
    @Transactional
    public ClubInfoResponse getClubInfo(Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);

        ClubInfoResponse clubInfo = ClubInfoResponse.builder().
                name(club.getName()).
                clubId(club.getClubId()).
                oneLiner(club.getOneLiner()).
                introduction(club.getIntroduction()).
                reviewAvg(0).
                reviewNum(0).
                profile(club.getProfile()).
                category(club.getCategory()).
                application(club.getApplication()).
                fileList(null).
                build();

        return clubInfo;
    }

    // 모든 동아리 정보 조회
    @Override
    @Transactional
    public List<ClubInfoResponse> getAllClubInfo() {
        return List.of();
    }

    // 카테고리별 동아리 정보 조회
    @Override
    public List<ClubInfoResponse> getAllFilterClubInfo(int category) {
        return List.of();
    }

    // 검색어를 통한 동아리 정보 조회
    @Override
    public List<ClubInfoResponse> getAllSearchClubInfo(String search) {
        return List.of();
    }

}
