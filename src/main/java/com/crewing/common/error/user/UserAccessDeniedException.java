package com.crewing.common.error.user;


import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class UserAccessDeniedException extends BusinessException {
    public UserAccessDeniedException() {
        super(ErrorCode.USER_ACCESS_DENIED);
    }
}
