package com.crewing.common.error.member;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class MemberNotFoundException extends BusinessException {
    public MemberNotFoundException() {
        super(ErrorCode.MEMBER_NOT_FOUND);
    }
}
