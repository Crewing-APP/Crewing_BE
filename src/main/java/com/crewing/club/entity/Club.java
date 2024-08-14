package com.crewing.club.entity;

import com.crewing.common.entity.BaseTimeEntity;
import com.crewing.file.entity.ClubFile;
import com.crewing.member.entity.Member;
import com.crewing.review.entity.Review;
import com.crewing.review.entity.ReviewAccess;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Club extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long clubId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    private String oneLiner;

    @Column(length = 500)
    private String application;

    private String profile;

    @Column(nullable = false)
    private int category;

    @Convert(converter = StatusConverter.class)
    private Status status;

    @ColumnDefault("false")
    private Boolean isRecruit;

    @ColumnDefault("false")
    private Boolean isOnlyStudent; // 대학 인증된 학생만 모집할 경우

    private String docDeadLine; // 서류 접수일(마감일)

    private String docResultDate; // 서류 결과 발표일

    private String interviewStartDate; // 면접 시작일

    private String interviewEndDate; // 면접 종료일

    private String finalResultDate; // 최종 발표일

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ClubFile> clubFileList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Member> memberList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ReviewAccess> reviewAccessList = new ArrayList<>();

}
