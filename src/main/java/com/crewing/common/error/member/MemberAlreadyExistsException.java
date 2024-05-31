package com.crewing.common.error.member;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class MemberAlreadyExistsException extends BusinessException {
    public MemberAlreadyExistsException() {
        super(ErrorCode.MEMBER_ALREADY_EXISTS                              );
    }
}
