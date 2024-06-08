package com.crewing.common.error.applicant;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class ApplicantAlreadyExistsException extends BusinessException {
    public ApplicantAlreadyExistsException() {
        super(ErrorCode.APPLICANT_ALREADY_EXISTS);
    }
}
