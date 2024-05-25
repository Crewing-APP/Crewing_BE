package com.crewing.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //Common
    INVALID_INPUT_VALUE("C01", "Invalid Input Value.", HttpStatus.BAD_REQUEST.value()),
    METHOD_NOT_ALLOWED("C02", "Invalid Method Type.", HttpStatus.METHOD_NOT_ALLOWED.value()),
    ENTITY_NOT_FOUND("C03", "Entity Not Found.", HttpStatus.NOT_FOUND.value()),
    INTERNAL_SERVER_ERROR("C04", "Internal Server Error.", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    FILE_NOT_UPLOAD("C05", "Internal Server Error.", HttpStatus.BAD_REQUEST.value()),
    //User
    USER_ACCESS_DENIED("U01", "User Access is Denied.", HttpStatus.UNAUTHORIZED.value()),
    USER_NOT_FOUND("U02", "User is not Found.", HttpStatus.BAD_REQUEST.value()),
    //Club
    CLUB_NOT_FOUND("CL01", "Club is not Found.", HttpStatus.NOT_FOUND.value()),
    CLUB_ACCESS_DENIED("CL02","No permission to club",HttpStatus.FORBIDDEN.value()),
    //Review
    REVIEW_NOT_FOUND("R01", "Review is not Found.", HttpStatus.NOT_FOUND.value()),
    REVIEW_ACCESS_DENIED("R02","No permission to review",HttpStatus.FORBIDDEN.value()),
    REVIEW_ALREADY_EXISTS("R03","A review already exists for this club",HttpStatus.BAD_REQUEST.value()),
    //File
    FILE_FAILED_S3_UPLOAD("F01","File upload to S3 failed",HttpStatus.INTERNAL_SERVER_ERROR.value());

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
