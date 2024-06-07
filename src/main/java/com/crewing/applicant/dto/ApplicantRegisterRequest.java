package com.crewing.applicant.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ApplicantRegisterRequest {
    @NotNull
    private Long clubId;
}
