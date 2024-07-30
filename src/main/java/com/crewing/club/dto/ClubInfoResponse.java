package com.crewing.club.dto;

import com.crewing.club.entity.Status;
import com.crewing.file.entity.ClubFile;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ClubInfoResponse {
    private Long clubId;
    private String name;
    private String oneLiner;
    private String introduction;
    private Float reviewAvg;
    private int reviewNum;
    private String profile;
    private List<ClubFile.ImageInfo> images;
    private String application;
    private int category;
    private Status status;
    private Boolean isRecruit;
    private Boolean isOnlyStudent; // 대학 인증된 학생만 모집할 경우
    private String docDeadLine; // 서류 접수일(마감일)
    private String docResultDate; // 서류 결과 발표일
    private String interviewStartDate; // 면접 시작일
    private String interviewEndDate; // 면접 종료일
    private String finalResultDate; // 최종 발표일
    private Boolean isReviewAccess; // 리뷰 조회 권한이 있는지
}
