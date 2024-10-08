package com.crewing.applicant.api;

import com.crewing.applicant.dto.*;
import com.crewing.applicant.entity.Status;
import com.crewing.applicant.service.ApplicantService;
import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.club.dto.ClubListInfoResponse;
import com.crewing.member.dto.MemberInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "applicant", description = "지원자 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/applicant")
public class ApplicantController {
    private final ApplicantService applicantService;

    @Operation(summary = "동아리 지원", description = "서류 제출 후 사용자가 지원 확인")
    @PostMapping("/create")
    public ResponseEntity<ApplicantCreateResponse> createApplicant(@RequestBody ApplicantCreateRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ApplicantCreateResponse response = applicantService.createApplicant(request,principalDetails.getUser());
        return ResponseEntity.ok().body(response);
    }
    @Operation(summary = "운영진의 지원자 등록하기", description = "동아리 운영진만 가능, 직접 회원 코드를 입력하여 지원자 등록 기능")
    @PostMapping("/create/manager")
    public ResponseEntity<ApplicantCreateResponse> createApplicantByManager(@RequestBody ApplicantEnrollRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ApplicantCreateResponse response = applicantService.createApplicantByManager(request,principalDetails.getUser());
        return ResponseEntity.ok().body(response);
    }
    @Operation(summary = "동아리 지원자 상태 변경", description = "동아리 운영진만 가능, WAIT(서류 제출 지원자), DOC(서류 합격), INTERVIEW(최종 합격), DOC_FAIL(서류 불합격), FINAL_FAIL(최종 불합격)")
    @PatchMapping("/status")
    public ResponseEntity<List<ApplicantCreateResponse>> changeApplicantStatus(@RequestBody ApplicantsChangeStatusRequest statusRequest, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<ApplicantCreateResponse> response= applicantService.changeApplicantStatus(statusRequest,principalDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "동아리 지원자 목록 조회", description = "동아리 운영진만 가능, WAIT - DOC - INTERVIEW - DOC_FAIL - FINAL_FAIL 순으로 페이징 조회(기본값 : 10개씩)")
    @Parameter(name = "clubId", description = "동아리 아이디",required = true)
    @GetMapping("/applicants/{clubId}")
    public ResponseEntity<ApplicantListResponse> getAllApplicants(@PageableDefault(size = 10) Pageable pageable, @PathVariable Long clubId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ApplicantListResponse response = applicantService.getAllApplicantInfo(pageable,clubId,principalDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "동아리 지원자 상태별 목록 조회", description = "동아리 운영진만 가능, 페이징 조회(기본값 : 10개씩)")
    @Parameters({
            @Parameter(name = "clubId", description = "동아리 아이디", required = true),
            @Parameter(name = "status", description = "지원자 상태 : WAIT(서류 제출 지원자),DOC(서류 합격),INTERVIEW(면접 합격),DOC_FAIL(서류 불합격),FINAL_FAIL(최종 불합격)", required = true)
    })
    @GetMapping("/applicants/status/{clubId}")
    public ResponseEntity<ApplicantListResponse> getAllStatusApplicants(@PageableDefault(size = 10) Pageable pageable, @PathVariable Long clubId, @RequestParam Status status, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ApplicantListResponse response = applicantService.getAllStatusApplicantInfo(pageable,clubId,status,principalDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "동아리 지원자들 삭제 처리", description = "동아리 운영진만 가능, 지원자에서 삭제 처리")
    @PostMapping("/delete")
    public ResponseEntity<String> deleteApplicants(@RequestBody ApplicantsDeleteRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        applicantService.deleteApplicants(request,principalDetails.getUser());
        return ResponseEntity.ok().body("Delete successful");
    }

    @Operation(summary = "동아리 지원자 합격", description = "면접합격한 지원자만 가능, 운영진이 아닌 사용자가 등록 처리, 지원자 -> 동아리 회원으로 전환됨")
    @PostMapping("/register")
    public ResponseEntity<MemberInfoResponse> registerApplicant(@RequestBody ApplicantRegisterRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails){
        MemberInfoResponse response =  applicantService.registerApplicants(request,principalDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "내가 지원한 동아리 목록", description = "로그인한 사용자의 지원한 동아리 목록, 페이징 x")
    @GetMapping("/applicants/my")
    public ResponseEntity<List<ClubListInfoResponse>> getMyAllApplicantClub(@AuthenticationPrincipal PrincipalDetails principalDetails){
        List<ClubListInfoResponse> response =  applicantService.getAllMyApplicantClubInfo(principalDetails.getUser());
        return ResponseEntity.ok().body(response);
    }
}
