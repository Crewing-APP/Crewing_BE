package com.crewing.club.service;

import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.entity.Club;

import java.util.List;

public interface ClubReadService {
    ClubInfoResponse getClubInfo(Long clubId);
    List<ClubInfoResponse> getAllClubInfo();
    List<ClubInfoResponse> getAllFilterClubInfo(int category);
    List<ClubInfoResponse> getAllSearchClubInfo(String search);
}
