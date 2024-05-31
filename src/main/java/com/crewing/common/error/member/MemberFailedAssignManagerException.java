package com.crewing.common.error.member;


import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class MemberFailedAssignManagerException extends BusinessException{
    public MemberFailedAssignManagerException() {
        super(ErrorCode.MEMBER_FAILED_ASSIGN_MANAGER);
    }
}
