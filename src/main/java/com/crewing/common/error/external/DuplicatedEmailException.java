package com.crewing.common.error.external;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class DuplicatedEmailException extends BusinessException {
    public DuplicatedEmailException() {
        super(ErrorCode.DUPLICATED_EMAIL_EXCEPTION);
    }
}
