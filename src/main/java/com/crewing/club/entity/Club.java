package com.crewing.club.entity;

import com.crewing.common.entity.BaseTimeEntity;
import com.crewing.file.entity.ClubFile;
import com.crewing.member.entity.Member;
import com.crewing.review.entity.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
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

    private String introduction;

    private String oneLiner;

    private String application;

    private String profile;

    @Column(nullable = false)
    private int category;

    @Column(nullable = false)
    private Status status;

    @ColumnDefault("true")
    private boolean recruitment;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ClubFile> clubFileList = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Member> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

}
