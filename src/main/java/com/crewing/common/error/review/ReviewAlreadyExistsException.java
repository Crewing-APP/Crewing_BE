package com.crewing.common.error.review;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class ReviewAlreadyExistsException extends BusinessException{
    public ReviewAlreadyExistsException() {
        super(ErrorCode.REVIEW_ALREADY_EXISTS);
    }
}
