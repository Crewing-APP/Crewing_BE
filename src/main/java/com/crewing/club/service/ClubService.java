package com.crewing.club.service;

import com.crewing.club.dto.ClubCreateRequest;
import com.crewing.club.dto.ClubUpdateRequest;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.user.entity.User;

public interface ClubService {
    Club createClub(ClubCreateRequest clubCreateRequest, User user, String profile);
    Club updateClub(Long clubId, ClubUpdateRequest clubUpdateRequest, User user, String profile);
    void deleteClub(Long clubId, User user);
    Club changeStatus(Long clubId, User user, Status status);
}
