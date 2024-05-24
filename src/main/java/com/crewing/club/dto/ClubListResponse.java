package com.crewing.club.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class ClubListResponse {
    private int pageNum;
    private int pageSize;
    private int totalCnt;
    private Page<ClubInfoResponse> clubs;
}
