package com.crewing.common.error.applicant;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class ApplicantAccessDeniedException extends BusinessException {
    public ApplicantAccessDeniedException() {
        super(ErrorCode.APPLICANT_ACCESS_DENIED);
    }
}
