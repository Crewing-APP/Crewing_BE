package com.crewing.user.api;

import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.user.dto.UserDTO.InterestUpdateRequest;
import com.crewing.user.dto.UserDTO.UserInfoResponse;
import com.crewing.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "유저", description = "유저 정보를 관리 합니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @Operation(summary = "유저 정보 조회", description = "로그인 유저의 정보를 조회합니다")
    @GetMapping
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserInfoResponse userInfo = userService.getUserInfo(principalDetails.getId());

        return ResponseEntity.ok().body(userInfo);
    }

    @Operation(summary = "유저 관심사 수정", description = "로그인 유저의 관심사를 수정합니다")
    @PatchMapping("/interest")
    public ResponseEntity<?> updateInterest(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @RequestBody InterestUpdateRequest request) {
        userService.updateInterest(principalDetails.getId(), request);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 닉네임 수정", description = "로그인 유저의 닉네임을 수정합니다")
    @PatchMapping("/{nickname}")
    public ResponseEntity<?> updateUserNickname(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @PathVariable String nickname) {
        userService.updateUserNickname(principalDetails.getId(), nickname);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 프로필 사진 수정", description = "로그인 유저의 프로필 사진을 수정합니다")
    @PatchMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserProfileImage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                    @RequestBody MultipartFile image) {

        userService.updateUserProfileImage(principalDetails.getId(), image);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 삭제", description = "로그인 유저를 삭제합니다")
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.deleteUser(principalDetails.getId());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public void test(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("user = {}", principalDetails.getUser().getRole());
    }

}
