package com.crewing.club.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    IT("IT/데이터"),
    PHOTO("사진/촬영"),
    HUMANITY("인문학/독서"),
    TRAVEL("여행"),
    SPORTS("스포츠"),
    CULTURE("문화/예술"),
    DANCE("댄스"),
    MUSIC("음악/악기"),
    VOLUNTEER("봉사활동"),
    ETC("기타");

    private final String key;
}
