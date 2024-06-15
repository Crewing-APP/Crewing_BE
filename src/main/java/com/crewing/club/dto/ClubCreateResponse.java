package com.crewing.club.dto;

import com.crewing.club.entity.Status;
import com.crewing.file.entity.ClubFile;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ClubCreateResponse {
    private Long clubId;
    private String name;
    private String oneLiner;
    private String introduction;
    private String profile;
    private List<ClubFile.ImageInfo> images;
    private String application;
    private int category;
    private Status status;
    private boolean isRecruit;
    private String recruitStartDate;
    private String recruitEndDate;
}
