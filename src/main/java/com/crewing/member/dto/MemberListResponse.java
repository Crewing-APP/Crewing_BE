package com.crewing.member.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MemberListResponse {
    private int pageNum;
    private int pageSize;
    private Long totalCnt;
    private List<MemberInfoResponse> members;
}
