package com.crewing.common.error.review;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class ReviewNotPurchaseWithPointException extends BusinessException {
    public ReviewNotPurchaseWithPointException() {
        super(ErrorCode.REVIEW_NOT_PURCHASE_WITH_POINT);
    }
}
