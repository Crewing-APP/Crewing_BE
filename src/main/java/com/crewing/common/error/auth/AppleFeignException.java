package com.crewing.common.error.auth;

import lombok.Getter;

@Getter
public class AppleFeignException extends RuntimeException {
    private final String message;
    private final int status;

    public AppleFeignException(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
