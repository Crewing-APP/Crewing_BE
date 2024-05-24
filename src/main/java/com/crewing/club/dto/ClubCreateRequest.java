package com.crewing.club.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
public class ClubCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String oneLiner;

    @NotBlank
    private String introduction;

    private String application;

    @NotNull
    private  int category;

}
