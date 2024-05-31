package com.crewing.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberCreateRequest {
    @NotNull
    private Long clubId;
    @NotNull
    private Long userId;
}
