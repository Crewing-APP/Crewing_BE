package com.crewing.common.error.club;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class ClubNotFoundException extends BusinessException {
    public ClubNotFoundException() {
        super(ErrorCode.CLUB_NOT_FOUND);
    }
}
