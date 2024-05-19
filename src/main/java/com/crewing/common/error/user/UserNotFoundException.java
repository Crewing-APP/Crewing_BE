package com.crewing.common.error.user;


import com.crewing.common.error.EntityNotFoundException;
import com.crewing.common.error.ErrorCode;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
