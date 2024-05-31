package com.crewing.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
public class ReviewCreateRequest {
    @NotNull
    private Long clubId;

    private String review;

    @NotNull
    private int rate;
}
