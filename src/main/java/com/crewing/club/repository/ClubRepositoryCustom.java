package com.crewing.club.repository;

import com.crewing.club.dto.ClubListInfoResponse;
import com.crewing.club.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClubRepositoryCustom {
    Page<ClubListInfoResponse> findAllClubsWithAverageRating(List<Integer> categories, Status clubStatus, String birth, String gender, Pageable pageable);
    Page<ClubListInfoResponse> findAllClubsWithAverageRatingByKeyword(List<Integer> categories, Status clubStatus, String birth, String keyword, String gender,Pageable pageable);
}
