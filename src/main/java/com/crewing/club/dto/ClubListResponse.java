package com.crewing.club.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class ClubListResponse {
    private int pageNum;
    private int pageSize;
    private Long totalCnt;
    private List<ClubListInfoResponse> clubs;
}
