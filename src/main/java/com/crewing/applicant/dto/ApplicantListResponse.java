package com.crewing.applicant.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ApplicantListResponse {
    private int pageNum;
    private int pageSize;
    private int totalCnt;
    private List<ApplicantCreateResponse> applicants;
}
