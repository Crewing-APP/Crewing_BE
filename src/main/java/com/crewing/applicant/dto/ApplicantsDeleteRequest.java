package com.crewing.applicant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ApplicantsDeleteRequest {
    @NotNull
    private Long clubId;

    private List<Long> deleteList;

    @NotBlank
    private String content;
}
