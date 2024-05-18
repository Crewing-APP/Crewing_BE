package com.crewing.auth.api;

import com.crewing.auth.dto.LoginDTO.OauthLoginRequest;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.external.OauthApi.GoogleOauth;
import java.util.Map;
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
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final GoogleOauth googleOauth;

    @PostMapping("/oauth/login/{socialType}")
    public ResponseEntity<TokenResponse> loginOauth(@RequestBody OauthLoginRequest request,
                                                    @PathVariable String socialType) {

        return ResponseEntity.ok().build();
    }

    @PostMapping("/oauth/signUp")
    public ResponseEntity<TokenResponse> signUpOauth() {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/test")
    public void test() {
        Map<String, Object> oauthUserInfo = googleOauth.getOauthUserInfo(
                "ya29.a0AXooCgsdJzZYZaHkubLlIyt-E98tTDVxbe_0-U5_ihcV1A50YYXpYazbKyR8LfIZoFpv3s3vgA-cwB8vhsfv0Gqj4zrvy8BrBR7bLb_HbIIfGLPo0I84sqLq2Wcok6ePEt-LAtg_lhYUoB0p2ei9J5NSVYpZcUqGNfnxaCgYKAUcSARASFQHGX2Mi2lePUYuXKg9jPH1xZhbHMw0171");

        log.info("asdasdas {}", oauthUserInfo.get("id"));
//        log.info("asdasdasd {} ", info.getResult());
    }
}
