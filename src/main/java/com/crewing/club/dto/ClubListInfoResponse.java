package com.crewing.club.dto;

import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.review.entity.Review;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClubListInfoResponse {
    @JsonProperty("clubId")
    private Long clubId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("oneLiner")
    private String oneLiner;

    @JsonProperty("reviewAvg")
    private Float reviewAvg;

    @JsonProperty("reviewNum")
    private int reviewNum;

    @JsonProperty("latestReview")
    private String latestReview;

    @JsonProperty("profile")
    private String profile;

    @JsonProperty("category")
    private int category;

    @JsonProperty("status")
    private Status status;

    @JsonProperty("isRecruit")
    private Boolean isRecruit;

    @JsonProperty("isOnlyStudent")
    private Boolean isOnlyStudent; // 대학 인증된 학생만 모집할 경우

    @JsonProperty("docDeadLine")
    private String docDeadLine; // 서류 접수일(마감일)

    @JsonProperty("docResultDate")
    private String docResultDate; // 서류 결과 발표일

    @JsonProperty("interviewStartDate")
    private String interviewStartDate; // 면접 시작일

    @JsonProperty("interviewEndDate")
    private String interviewEndDate; // 면접 종료일

    @JsonProperty("finalResultDate")
    private String finalResultDate; // 최종 발표일

    public void setLatestReview(String latestReview) {
        this.latestReview = latestReview;
    }

    public ClubListInfoResponse(Long clubId, String name,String oneLiner,Double reviewAvg, Long reviewNum, String latestReview,
                                String profile, int category, Status status, Boolean isRecruit, Boolean isOnlyStudent, String docDeadLine,
                                String docResultDate, String interviewStartDate, String interviewEndDate, String finalResultDate) {
        this.clubId = clubId;
        this.name = name;
        this.oneLiner = oneLiner;
        this.reviewAvg = reviewAvg != null ? reviewAvg.floatValue() : 0;
        this.reviewNum = reviewNum != null ? reviewNum.intValue() : 0;
        this.latestReview = latestReview;
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

    public static ClubListInfoResponse toClubListInfoResponse(Club club, Float reviewAvg) {
        List<Review> reviewList = club.getReviewList();
        return ClubListInfoResponse.builder().
                name(club.getName()).
                clubId(club.getClubId()).
                oneLiner(club.getOneLiner()).
                reviewAvg(reviewAvg).
                reviewNum(reviewList.size()).
                latestReview(reviewList.isEmpty() ? null : reviewList.get(reviewList.size()-1).getReview()).
                profile(club.getProfile()).
                category(club.getCategory()).
                status(club.getStatus()).
                isRecruit(club.getIsRecruit()).
                isOnlyStudent(club.getIsOnlyStudent()).
                docDeadLine(club.getDocDeadLine()).
                docResultDate(club.getDocResultDate()).
                interviewStartDate(club.getInterviewStartDate()).
                interviewEndDate(club.getInterviewEndDate()).
                finalResultDate(club.getFinalResultDate()).
                build();

    }
}
