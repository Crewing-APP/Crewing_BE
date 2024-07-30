package com.crewing.user.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PointEvent {
    private PointHistoryType type;
    private Long userId;
    private int point; // + -> 포인트 적립 , - -> 포인트 사용
}
