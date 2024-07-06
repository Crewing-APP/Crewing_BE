package com.crewing.club.dto;

import com.crewing.club.entity.Status;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class ClubListInfoResponse {
    private Long clubId;
    private String name;
    private String oneLiner;
    private Float reviewAvg;
    private int reviewNum;
    private String profile;
    private int category;
    private Status status;
    private Boolean isRecruit;
    private Boolean isOnlyStudent; // 대학 인증된 학생만 모집할 경우
    private String docDeadLine; // 서류 접수일(마감일)
    private String docResultDate; // 서류 결과 발표일
    private String interviewStartDate; // 면접 시작일
    private String interviewEndDate; // 면접 종료일
    private String finalResultDate; // 최종 발표일

    // for club repository jpql
    public ClubListInfoResponse(Long clubId, String name,String oneLiner,Double reviewAvg, Long reviewNum
    , String profile, int category, Status status, Boolean isRecruit, Boolean isOnlyStudent, String docDeadLine,
                                String docResultDate, String interviewStartDate, String interviewEndDate, String finalResultDate) {
        this.clubId = clubId;
        this.name = name;
        this.oneLiner = oneLiner;
        this.reviewAvg = reviewAvg != null ? reviewAvg.floatValue() : 0;
        this.reviewNum = reviewNum != null ? reviewNum.intValue() : 0;
        this.profile = profile;
        this.category = category;
        this.status = status;
        this.isRecruit = isRecruit;
        this.isOnlyStudent = isOnlyStudent;
        this.docDeadLine = docDeadLine;
        this.docResultDate = docResultDate;
        this.interviewStartDate = interviewStartDate;
        this.interviewEndDate = interviewEndDate;
        this.finalResultDate = finalResultDate;
    }
}
