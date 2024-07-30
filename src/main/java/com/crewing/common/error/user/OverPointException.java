package com.crewing.common.error.user;


import com.crewing.common.error.EntityNotFoundException;
import com.crewing.common.error.ErrorCode;

public class OverPointException extends EntityNotFoundException {

    public OverPointException() {
        super(ErrorCode.OVER_POINT);
    }
}
