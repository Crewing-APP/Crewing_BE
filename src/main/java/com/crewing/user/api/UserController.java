package com.crewing.user.api;

import com.crewing.auth.dto.AppleDto;
import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.user.dto.UserDTO.InterestUpdateRequest;
import com.crewing.user.dto.UserDTO.UserInfoResponse;
import com.crewing.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.crewing.auth.dto.AppleDto.*;

@Tag(name = "유저", description = "유저 정보를 관리 합니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "유저 정보 조회", description = "로그인 유저의 정보를 조회합니다")
    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        UserInfoResponse userInfo = userService.getUserInfo(principalDetails.getId());

        return ResponseEntity.ok().body(userInfo);
    }

    @Operation(summary = "유저 성별 수정", description = "로그인 유저의 성별을 수정합니다")
    @PatchMapping("/me/gender/{gender}")
    public ResponseEntity<Void> updateGender(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                             @PathVariable String gender) {
        userService.updateGender(principalDetails.getId(), gender);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 나이 수정", description = "로그인 유저의 성별을 수정합니다")
    @PatchMapping("/me/birth/{birth}")
    public ResponseEntity<Void> updateBirth(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                            @PathVariable String birth) {
        userService.updateBirth(principalDetails.getId(), birth);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 관심사 수정", description = "로그인 유저의 관심사를 수정합니다")
    @PatchMapping("/me/interest")
    public ResponseEntity<Void> updateInterest(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                               @RequestBody InterestUpdateRequest request) {
        userService.updateInterest(principalDetails.getId(), request);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 닉네임 수정", description = "로그인 유저의 닉네임을 수정합니다")
    @PatchMapping("/me/{nickname}")
    public ResponseEntity<?> updateUserNickname(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @PathVariable String nickname) {
        userService.updateUserNickname(principalDetails.getId(), nickname);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 프로필 사진 수정", description = "로그인 유저의 프로필 사진을 수정합니다")
    @PatchMapping(path = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateUserProfileImage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                    @RequestBody MultipartFile image) {

        userService.updateUserProfileImage(principalDetails.getId(), image);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저 삭제", description = "로그인 유저를 삭제합니다")
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.deleteUser(principalDetails.getId());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "애플 유저 삭제", description = "애플 로그인 유저를 삭제합니다")
    @PostMapping("/me/apple")
    public ResponseEntity<?> deleteAppleUser(@RequestBody @Valid AppleCodeRequestDto appleCodeRequestDto, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        userService.deleteAppleUser(principalDetails.getId(),appleCodeRequestDto);

        return ResponseEntity.ok().build();
    }

//    @Operation(summary = "유저 학생 인증 메일 발송", description = "학생 인증 메일을 발송합니다")
//    @PostMapping("/me/verification/email")
//    public ResponseEntity<Void> createStudentVerification(
//            @RequestBody StudentCreateVerificationRequest request) {
//        log.info(request.getEmail());
//        userService.createStudentEmailVerification(request);
//
//        return ResponseEntity.ok().build();
//    }
//
//    @Operation(summary = "유저 학생 인증 메일 검증", description = "학생 인증 메일을 검증하고 , 학생을 인증합니다.")
//    @PostMapping("/me/verification")
//    public ResponseEntity<Void> verifyEmail(@AuthenticationPrincipal PrincipalDetails principalDetails,
//                                            @RequestBody StudentVerifyRequest request) {
//        log.info(request.getEmail());
//        userService.verifyEmail(principalDetails.getId(), request);
//
//        return ResponseEntity.ok().build();
//    }

}
