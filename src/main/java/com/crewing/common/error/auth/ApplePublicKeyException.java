package com.crewing.common.error.auth;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class ApplePublicKeyException extends BusinessException {
    public ApplePublicKeyException() {
        super(ErrorCode.APPLE_PUBLIC_KEY_ERROR);
    }
}
