package com.crewing.club.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubUpdateRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String oneLiner;

    @NotBlank
    private String introduction;

    private String application;
}
