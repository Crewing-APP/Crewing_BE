package com.crewing.common.error.external;

import com.crewing.common.error.EntityNotFoundException;
import com.crewing.common.error.ErrorCode;

public class StudentAuthCodeNotFoundException extends EntityNotFoundException {
    public StudentAuthCodeNotFoundException() {
        super(ErrorCode.STUDENT_AUTH_CODE_NOT_FOUND);
    }
}
