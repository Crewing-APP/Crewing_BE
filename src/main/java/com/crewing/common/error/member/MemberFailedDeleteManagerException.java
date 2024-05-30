package com.crewing.common.error.member;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class MemberFailedDeleteManagerException extends BusinessException {
    public MemberFailedDeleteManagerException() {
        super(ErrorCode.MEMBER_FAILED_DELETE_MANAGER);
    }
}
