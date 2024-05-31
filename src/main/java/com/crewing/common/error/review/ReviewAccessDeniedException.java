package com.crewing.common.error.review;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class ReviewAccessDeniedException extends BusinessException {
    public ReviewAccessDeniedException() {
        super(ErrorCode.REVIEW_ACCESS_DENIED);
    }
}
