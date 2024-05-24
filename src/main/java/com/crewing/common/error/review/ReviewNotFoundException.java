package com.crewing.common.error.review;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class ReviewNotFoundException extends BusinessException {
    public ReviewNotFoundException() {
        super(ErrorCode.REVIEW_NOT_FOUND);
    }
}
