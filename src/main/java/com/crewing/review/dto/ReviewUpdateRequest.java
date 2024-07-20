package com.crewing.review.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewUpdateRequest {
    private String review;
    @NotNull
    private int rate;
}
