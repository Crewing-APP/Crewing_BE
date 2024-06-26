package com.crewing.auth.api;

import com.crewing.auth.dto.SignUpDTO.BasicSignUpRequest;
import com.crewing.auth.dto.SignUpDTO.OauthSignUpRequest;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.auth.service.SignUpService;
import com.crewing.mail.dto.EmailDTO.EmailSendResponse;
import com.crewing.mail.dto.EmailDTO.EmailVerifyRequest;
import com.crewing.mail.dto.EmailDTO.SignUpEmailVerifyResponse;
import com.crewing.mail.service.MailService;
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
@RequestMapping("/api/v1/singUp")
public class SignUpController {
    private final SignUpService signUpService;
    private final MailService mailService;

    @Operation(summary = "추가 회원가입", description = "추가정보가 필요한 유저 회원가입")
    @PostMapping("/oauth")
    public ResponseEntity<TokenResponse> signUpOauth(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                     @RequestBody OauthSignUpRequest request) {
        TokenResponse response = signUpService.signUpOauth(request, principalDetails.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "기본 회원가입", description = "기본 회원가입을 요청합니다")
    @PostMapping
    public ResponseEntity<TokenResponse> signUpBasic(@RequestBody BasicSignUpRequest request) {
        TokenResponse response = signUpService.signUpBasic(request);
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
        boolean response = mailService.verifySignUpEmail(request);

        return ResponseEntity.ok(
                SignUpEmailVerifyResponse.builder()
                        .email(request.getEmail())
                        .verifyResult(response)
                        .build()
        );
    }
}
