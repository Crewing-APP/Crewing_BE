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
    private Boolean isOnlyStudent; // 대학 인증된 학생만 모집할 경우
    private String docDeadLine; // 서류 접수일(마감일)
    private String docResultDate; // 서류 결과 발표일
    private String interviewStartDate; // 면접 시작일
    private String interviewEndDate; // 면접 종료일
    private String finalResultDate; // 최종 발표일
}
