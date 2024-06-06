package com.crewing.club.dto;

import com.crewing.club.entity.Status;
import com.crewing.file.entity.ClubFile;
import lombok.*;

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
    private boolean recruitment;
}
