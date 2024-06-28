package com.crewing.club.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ClubChangeStatusRequest {
    @NotNull
    private Long clubId;
    @NotBlank
    private String status;
    private String content;
}
