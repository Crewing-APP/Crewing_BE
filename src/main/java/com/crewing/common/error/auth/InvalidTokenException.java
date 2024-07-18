package com.crewing.common.error.auth;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
