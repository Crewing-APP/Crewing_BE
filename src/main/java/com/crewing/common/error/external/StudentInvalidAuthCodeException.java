package com.crewing.common.error.external;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class StudentInvalidAuthCodeException extends BusinessException {
    public StudentInvalidAuthCodeException() {
        super(ErrorCode.STUDENT_INVALID_AUTH_CODE_EXCEPTION);
    }
}
