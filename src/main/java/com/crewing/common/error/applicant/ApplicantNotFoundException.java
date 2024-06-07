package com.crewing.common.error.applicant;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class ApplicantNotFoundException extends BusinessException {
    public ApplicantNotFoundException() {
        super(ErrorCode.APPLICANT_NOT_FOUND);
    }
}
