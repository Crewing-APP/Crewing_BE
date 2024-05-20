package com.crewing.auth.api;

import com.crewing.auth.dto.LoginDTO.LoginResponse;
import com.crewing.auth.dto.LoginDTO.OauthLoginRequest;
import com.crewing.auth.dto.SignUpDTO.OauthSignUpRequest;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.auth.service.AuthService;
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

@Tag(name = "Auth", description = "회원가입 및 로그인 API")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "개발용 토큰 발급", description = "디비에 레코드 추가하고 진행할것")
    @GetMapping("/test/{email}")
    public ResponseEntity<TokenResponse> login(@PathVariable String email) {
        return ResponseEntity.ok().body(authService.getDevToken(email));
    }

    @Operation(summary = "로그인", description = "Oauth Token을 통한 로그인")
    @PostMapping("/oauth/login/{socialType}")
    public ResponseEntity<LoginResponse> loginOauth(@RequestBody OauthLoginRequest request,
                                                    @PathVariable String socialType) {
        LoginResponse response = authService.loginOauth(request.getAccessToken(), socialType);

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "추가 회원가입", description = "추가정보가 필요한 유저 회원가입")
    @PostMapping("/oauth/signUp")
    public ResponseEntity<TokenResponse> signUpOauth(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                     @RequestBody OauthSignUpRequest request) {
        authService.signUpOauth(request, principalDetails.getId());
        return ResponseEntity.ok().build();
    }

}
