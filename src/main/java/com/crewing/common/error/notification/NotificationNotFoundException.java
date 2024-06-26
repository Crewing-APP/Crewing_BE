package com.crewing.common.error.notification;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;

public class NotificationNotFoundException extends BusinessException {
    public NotificationNotFoundException() {
        super(ErrorCode.NOTIFICATION_NOT_FOUND);
    }
}
