package com.crewing.club.dto;

import com.crewing.file.entity.File;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ClubInfoResponse {
    private Long clubId;
    private String name;
    private String oneLiner;
    private String introduction;
    private float reviewAvg;
    private int reviewNum;
    private String profile;
    private List<File> fileList;
    private String application;
    private int category;
}
