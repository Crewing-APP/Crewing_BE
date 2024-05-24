package com.crewing.club.entity;

import com.crewing.common.entity.BaseTimeEntity;
import com.crewing.file.entity.File;
import com.crewing.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<File> fileList = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Member> memberList = new ArrayList<>();
}
