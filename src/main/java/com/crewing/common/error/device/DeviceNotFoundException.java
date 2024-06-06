package com.crewing.common.error.device;

import com.crewing.common.error.EntityNotFoundException;
import com.crewing.common.error.ErrorCode;

public class DeviceNotFoundException extends EntityNotFoundException {
    public DeviceNotFoundException() {
        super(ErrorCode.DEVICE_NOT_FOUND_EXCEPTION);
    }
}
