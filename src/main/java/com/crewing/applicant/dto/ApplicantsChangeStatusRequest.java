package com.crewing.applicant.dto;

import com.crewing.applicant.entity.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ApplicantsChangeStatusRequest {
    @NotNull
    private Long clubId;

    private List<Long> changeList;

    @NotBlank
    private Status status;

    @NotBlank
    private String content;
}
