package com.crewing.club.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class ClubUpdateRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String oneLiner;

    @NotBlank
    private String introduction;

    private String application;

    @NotNull
    private int category;

    @NotNull
    private Boolean isRecruit;

    private String recruitStartDate;

    private String recruitEndDate;
}
