package com.crewing.auth.api;

import com.crewing.auth.dto.LoginDTO.EmailLoginRequest;
import com.crewing.auth.dto.LoginDTO.EmailLoginResponse;
import com.crewing.auth.dto.LoginDTO.LoginRequest;
import com.crewing.auth.dto.LoginDTO.LoginResponse;
import com.crewing.auth.dto.LoginDTO.OauthLoginRequest;
import com.crewing.auth.dto.SignUpDTO.RefreshRequest;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.service.AuthService;
import com.crewing.user.entity.SocialType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "인증", description = "사용자 식별 및 토큰을 관리 합니다.")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "어드민 로그인", description = "어드민 전용 로그인")
    public ResponseEntity<TokenResponse> loginAdmin(@RequestBody LoginRequest request) {

        return ResponseEntity.ok().body(new TokenResponse());
    }

    @Operation(summary = "개발용 토큰 발급", description = "해당 이메일에 맞는 어드민 계정 생성 및 토큰 발급 , 비밀번호 1234 고정")
    @GetMapping("/test/{email}")
    public ResponseEntity<TokenResponse> login(@PathVariable String email) {
        return ResponseEntity.ok().body(authService.getDevToken(email));
    }

    @Operation(summary = "Oauth 로그인", description = "Oauth Token을 통한 로그인")
    @PostMapping("/login/oauth/{socialType}")
    public ResponseEntity<LoginResponse> loginOauth(@RequestBody OauthLoginRequest request,
                                                    @PathVariable SocialType socialType) {
        LoginResponse response = authService.loginOauth(request.getOauthAccessToken(), socialType);

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Email 로그인", description = "Email 인증을 통한 로그인")
    @PostMapping("/login/email")
    public ResponseEntity<EmailLoginResponse> loginEmail(@RequestBody EmailLoginRequest request) {
        EmailLoginResponse response = authService.loginEmail(request.getEmail(), request.getAuthNumber());

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "토큰 재발급", description = "refresh 토큰을 통한 토큰 재발급")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshRequest request) {
        TokenResponse tokenResponse = authService.reissuedRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok().body(tokenResponse);
    }

}
