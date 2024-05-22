package com.crewing.club.dto;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateClubRequest {

    @NotBlank
    private String name;

    private String oneLiner;

    private String introduction;

    @NotBlank
    private List<String> category;

}
