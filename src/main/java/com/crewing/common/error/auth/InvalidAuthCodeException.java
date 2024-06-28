package com.crewing.common.error.auth;


import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class InvalidAuthCodeException extends BusinessException {
    public InvalidAuthCodeException() {
        super(ErrorCode.INVALID_AUTH_CODE);
    }
}
