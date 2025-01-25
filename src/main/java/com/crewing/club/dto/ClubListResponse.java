package com.crewing.club.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class ClubListResponse {
    @JsonProperty("pageNum")
    private int pageNum;
    @JsonProperty("pageSize")
    private int pageSize;
    @JsonProperty("totalCnt")
    private Long totalCnt;
    @JsonProperty("clubs")
    private List<ClubListInfoResponse> clubs;
}
