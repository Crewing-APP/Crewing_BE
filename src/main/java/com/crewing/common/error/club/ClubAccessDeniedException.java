package com.crewing.common.error.club;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class ClubAccessDeniedException extends BusinessException {
    public ClubAccessDeniedException() {
        super(ErrorCode.CLUB_ACCESS_DENIED);
    }
}
