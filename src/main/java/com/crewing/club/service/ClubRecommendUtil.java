package com.crewing.club.service;

import com.crewing.club.repository.ClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClubRecommendUtil {
    private final ClubRepository clubRepository;
}
