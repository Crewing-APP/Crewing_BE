package com.crewing.common.error.auth;


import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class NotVerifiedEmailException extends BusinessException {
    public NotVerifiedEmailException() {
        super(ErrorCode.NOT_VERIFIED_EMAIL);
    }
}
