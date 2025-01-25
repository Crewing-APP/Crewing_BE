package com.crewing.club.service;

import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.dto.ClubListInfoResponse;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClubReadService {
    ClubInfoResponse getClubInfo(Long clubId, User user);
    ClubListResponse getAllClubInfo(Pageable pageable);
    ClubListResponse getAllFilterClubInfo(Pageable pageable,int category);
    ClubListResponse getAllSearchClubInfo(Pageable pageable,String search, int category);
    ClubListResponse getAllStatusClubInfo(Pageable pageable, Status status, User user);
    ClubListResponse getAllMyClubInfo(Pageable pageable, User user);
    ClubListResponse getAllRecommendedClubInfo(Pageable pageable, String search, User user);
    List<ClubListInfoResponse> getAllRecommendedClubInfoLegacy(String search, User user);
}
