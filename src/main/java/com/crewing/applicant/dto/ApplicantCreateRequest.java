package com.crewing.applicant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ApplicantCreateRequest {
    @NotNull
    private Long clubId;
}
