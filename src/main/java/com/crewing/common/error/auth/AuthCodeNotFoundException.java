package com.crewing.common.error.auth;


import com.crewing.common.error.EntityNotFoundException;
import com.crewing.common.error.ErrorCode;

public class AuthCodeNotFoundException extends EntityNotFoundException {

    public AuthCodeNotFoundException() {
        super(ErrorCode.AUTH_CODE_NOT_FOUND);
    }
}
