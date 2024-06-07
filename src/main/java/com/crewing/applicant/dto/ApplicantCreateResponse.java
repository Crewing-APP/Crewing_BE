package com.crewing.applicant.dto;

import com.crewing.applicant.entity.Status;
import com.crewing.member.dto.MemberInfoResponse;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ApplicantCreateResponse {
    private Long applicantId;
    private Long clubId;
    private Status status;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private MemberInfoResponse.UserInfo user;
}
