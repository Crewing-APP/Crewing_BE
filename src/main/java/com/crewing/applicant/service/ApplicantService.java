package com.crewing.applicant.service;

import com.crewing.applicant.dto.*;
import com.crewing.applicant.entity.Status;
import com.crewing.club.dto.ClubListInfoResponse;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.member.dto.MemberInfoResponse;
import com.crewing.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ApplicantService {
    ApplicantCreateResponse createApplicant(ApplicantCreateRequest request, User user);
    ApplicantCreateResponse createApplicantByManager(ApplicantEnrollRequest request, User manager);
    List<ApplicantCreateResponse> changeApplicantStatus(ApplicantsChangeStatusRequest request, User user);
    ApplicantListResponse getAllApplicantInfo(Pageable pageable,Long clubId,User user);
    ApplicantListResponse getAllStatusApplicantInfo(Pageable pageable, Long clubId, Status status, User user);
    void deleteApplicants(ApplicantsDeleteRequest applicantsDeleteRequest, User user);
    MemberInfoResponse registerApplicants(ApplicantRegisterRequest applicantRegisterRequest, User user);
    List<ClubListInfoResponse> getAllMyApplicantClubInfo(User user);
}
