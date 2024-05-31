package com.crewing.common.error.member;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class MemberAccessDeniedException  extends BusinessException {
    public MemberAccessDeniedException() {
        super(ErrorCode.MEMBER_ACCESS_DENIED);
    }
}
