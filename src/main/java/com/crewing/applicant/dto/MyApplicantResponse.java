package com.crewing.applicant.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyApplicantResponse {
    private Long clubId;
    private String name;
    private String profile;
}
