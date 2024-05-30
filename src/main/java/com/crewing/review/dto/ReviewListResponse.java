package com.crewing.review.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class ReviewListResponse {
    private Long clubId;
    private float reviewAvg;
    private int five;
    private int four;
    private int three;
    private int two;
    private int one;
    private int pageNum;
    private int pageSize;
    private Long totalCnt;
    private List<ReviewResponse> reviews;
}
