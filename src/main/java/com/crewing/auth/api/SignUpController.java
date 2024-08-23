package com.crewing.auth.api;

import com.crewing.auth.dto.SignUpDTO.BasicSignUpRequest;
import com.crewing.auth.dto.SignUpDTO.EmailDuplicateCheckResponse;
import com.crewing.auth.dto.SignUpDTO.SignUpRequest;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.auth.mail.dto.EmailDTO.EmailVerifyRequest;
import com.crewing.auth.mail.dto.EmailDTO.SignUpEmailVerifyResponse;
import com.crewing.auth.mail.service.MailService;
import com.crewing.auth.service.SignUpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원가입", description = "회원가입을 관리합니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/signUp")
public class SignUpController {
    private final SignUpService signUpService;
    private final MailService mailService;

    @Operation(summary = "ID/PW 회원가입", description = "기본 회원가입을 요청합니다")
    @PostMapping("/basic")
    public ResponseEntity<TokenResponse> signUpBasic(@RequestBody BasicSignUpRequest request) {
        TokenResponse response = signUpService.signUpBasic(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "추가 회원가입", description = "추가정보가 필요한 유저 회원가입")
    @PostMapping
    public ResponseEntity<TokenResponse> signUp(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @RequestBody SignUpRequest request) {
        TokenResponse response = signUpService.signUp(request, principalDetails.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "인증 메일 발송", description = "인증 메일을 발송합니다")
    @PostMapping("/verification/{email}")
    public ResponseEntity<Void> createEmailVerification(@PathVariable String email) {
        mailService.sendSignUpEmail(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "인증 메일 검증", description = "인증 메일을 검증합니다")
    @PostMapping("/verification/email/verify")
    public ResponseEntity<SignUpEmailVerifyResponse> verifyEmail(@RequestBody EmailVerifyRequest request) {
        boolean response = mailService.verifySignUpEmail(request.getEmail(), request.getAuthNum());

        return ResponseEntity.ok(
                SignUpEmailVerifyResponse.builder()
                        .email(request.getEmail())
                        .verifyResult(response)
                        .build()
        );
    }

    @Operation(summary = "이메일 중복 체크", description = "이메일 중복을 체크합니다")
    @GetMapping("/verification/duplicate/{email}")
    public ResponseEntity<EmailDuplicateCheckResponse> checkDuplicatedEmail(@PathVariable String email) {
        boolean duplicated = signUpService.checkDuplicateEmail(email);

        EmailDuplicateCheckResponse response = EmailDuplicateCheckResponse.builder()
                .email(email)
                .duplicate(duplicated)
                .build();

        return ResponseEntity.ok(response);
    }
}
