package com.crewing.auth.api;

import com.crewing.auth.dto.LoginDTO.OauthLoginRequest;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth/")
public class AuthController {

    @PostMapping("/oauth/login/{socialType}")
    public ResponseEntity<TokenResponse> oauthLogin(@RequestBody OauthLoginRequest request,
                                                    @PathVariable String socialType) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public void test() {

    }
}
