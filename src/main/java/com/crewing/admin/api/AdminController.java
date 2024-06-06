package com.crewing.admin.api;

import com.crewing.admin.service.AdminService;
import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.club.dto.ClubChangeStatusRequest;
import com.crewing.club.dto.ClubCreateResponse;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.service.ClubReadService;
import com.crewing.club.service.ClubService;
import com.crewing.user.dto.UserDTO.UserInfoResponse;
import com.crewing.user.dto.UserDTO.UserInfoResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

//todo : 동아리 관련 관리 기능 (신청 받기 , 동아리 리스트 조회 등등)
@Tag(name = "관리자", description = "관리자 권한으로 유저 및 동아리를 관리합니다")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;
    private final ClubReadService clubReadService;
    private final ClubService clubService;

    @Operation(summary = "모든 유저 조회 ! ", description = "모든 유저를 조회한다")
    @GetMapping("/users")
    public ResponseEntity<UserInfoResponses> getAllUserInfo() {
        UserInfoResponses responses = adminService.getAllUserInfo();

        return ResponseEntity.ok().body(responses);
    }

    @Operation(summary = "특정 유저 조회", description = "특정 유저를 조회한다")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserInfoResponse> getAllUserInfo(@PathVariable Long userId) {
        UserInfoResponse response = adminService.getUserInfo(userId);

        return ResponseEntity.ok().body(response);
    }


    @Operation(summary = "수락 신청 동아리 목록 조회", description = "WAIT 상태인 동아리 목록 조회, 페이징으로 조회 가능, 관리자만 조회")
    @GetMapping("/clubs")
    public ResponseEntity<ClubListResponse> getAllWaitClub(@PageableDefault(size = 10) Pageable pageable,
                                                           @RequestParam String status,
                                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ClubListResponse clubList = clubReadService.getAllStatusClubInfo(pageable, status,
                principalDetails.getUser());
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "동아리 상태 변경", description = "동아리 상태 변경(WAIT,HOLD,RETURN,ACCEPT) , 관리자만 가능")
    @PatchMapping("/clubs")
    public ResponseEntity<ClubCreateResponse> changeStatus(@RequestBody ClubChangeStatusRequest clubChangeStatusRequest,
                                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ClubCreateResponse clubInfo = clubService.changeStatus(clubChangeStatusRequest, principalDetails.getUser());
        return ResponseEntity.ok().body(clubInfo);
    }
}
