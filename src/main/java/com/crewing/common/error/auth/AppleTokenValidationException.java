package com.crewing.common.error.auth;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class AppleTokenValidationException extends BusinessException {
    public AppleTokenValidationException() {
        super(ErrorCode.APPLE_TOKEN_VALIDATION_ERROR);
    }
}
