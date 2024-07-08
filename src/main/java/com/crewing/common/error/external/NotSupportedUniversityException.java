package com.crewing.common.error.external;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class NotSupportedUniversityException extends BusinessException {
    public NotSupportedUniversityException() {
        super(ErrorCode.NOT_SUPPORTED_UNIVERSITY);
    }
}
