package com.crewing.admin.api;

import com.crewing.admin.service.AdminService;
import com.crewing.user.dto.UserDTO.UserInfoResponse;
import com.crewing.user.dto.UserDTO.UserInfoResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//todo : 동아리 관련 관리 기능 (신청 받기 , 동아리 리스트 조회 등등)
@Tag(name = "관리자", description = "관리자 권한으로 유저 및 동아리를 관리합니다")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/admin")
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "모든 유저 조회", description = "모든 유저를 조회한다")
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
}
