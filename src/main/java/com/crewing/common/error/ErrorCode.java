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
    OVER_POINT("U03", "Point Over.", HttpStatus.BAD_REQUEST.value()),
    //Auth
    NOT_VERIFIED_EMAIL("A01", "Mail is not Verified", HttpStatus.BAD_REQUEST.value()),
    AUTH_CODE_NOT_FOUND("A02", "AuthCode is not Found", HttpStatus.BAD_REQUEST.value()),
    INVALID_AUTH_CODE("A03", "AuthCode is Invalid", HttpStatus.BAD_REQUEST.value()),
    INVALID_TOKEN("A04", "Token is Invalid", HttpStatus.BAD_REQUEST.value()),
    //Club
    CLUB_NOT_FOUND("CL01", "Club is not Found.", HttpStatus.NOT_FOUND.value()),
    CLUB_ACCESS_DENIED("CL02", "No permission to club", HttpStatus.FORBIDDEN.value()),
    //Review
    REVIEW_NOT_FOUND("R01", "Review is not Found.", HttpStatus.NOT_FOUND.value()),
    REVIEW_ACCESS_DENIED("R02", "No permission to review", HttpStatus.FORBIDDEN.value()),
    REVIEW_ALREADY_EXISTS("R03", "A review already exists for this club", HttpStatus.BAD_REQUEST.value()),
    //File
    FILE_FAILED_S3_UPLOAD("F01", "File upload to S3 failed", HttpStatus.INTERNAL_SERVER_ERROR.value()),
    //Member
    MEMBER_ACCESS_DENIED("M01", "No permission to member", HttpStatus.FORBIDDEN.value()),
    MEMBER_NOT_FOUND("M02", "Member is not Found.", HttpStatus.NOT_FOUND.value()),
    MEMBER_FAILED_DELETE_MANAGER("M03", "Manager cannot be deleted. There must be at least one manager.",
            HttpStatus.BAD_REQUEST.value()),
    MEMBER_ALREADY_EXISTS("M04", "Member already exists.", HttpStatus.BAD_REQUEST.value()),
    MEMBER_FAILED_ASSIGN_MANAGER("M05", "Member is already manager.", HttpStatus.BAD_REQUEST.value()),
    //Applicant
    APPLICANT_NOT_FOUND("AP01", "Applicant is not Found.", HttpStatus.NOT_FOUND.value()),
    APPLICANT_ACCESS_DENIED("AP02", "No permission to applicant", HttpStatus.FORBIDDEN.value()),
    APPLICANT_ALREADY_EXISTS("AP03", "Applicant already exists.", HttpStatus.BAD_REQUEST.value()),
    //Device
    DEVICE_NOT_FOUND_EXCEPTION("D01", "Device is not Found.", HttpStatus.NOT_FOUND.value()),
    // Notification
    NOTIFICATION_NOT_FOUND("N01", "Notification is not Found.", HttpStatus.NOT_FOUND.value()),
    //External
    NOT_SUPPORTED_UNIVERSITY("E01", "University is Not Supported", HttpStatus.BAD_REQUEST.value()),
    STUDENT_INVALID_AUTH_CODE_EXCEPTION("E02", "Invalid Student Auth Code", HttpStatus.BAD_REQUEST.value()),
    STUDENT_AUTH_CODE_NOT_FOUND("E03", "Student Auth Code is Not Found", HttpStatus.NOT_FOUND.value()),
    DUPLICATED_EMAIL_EXCEPTION("E04", "Email is Duplicated", HttpStatus.CONFLICT.value());

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(String code, String message, int status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
