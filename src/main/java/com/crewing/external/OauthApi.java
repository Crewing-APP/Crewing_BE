package com.crewing.external;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

public class OauthApi {
    @FeignClient(name = "GoogleOauth", url = "https://www.googleapis.com/oauth2")
    public interface GoogleOauth {
        @GetMapping("/v1/userinfo")
        Map<String, Object> getOauthUserInfo(@RequestParam String access_token);
    }

    @FeignClient(name = "NaverOauth", url = "https://openapi.naver.com")
    public interface NaverOauth {
        @GetMapping("/v1/nid/me")
        Map<String, Object> getOauthUserInfo(@RequestHeader("Authorization") String access_token);
    }

    @FeignClient(name = "KakaoOauth", url = "https://kapi.kakao.com")
    public interface KakaoOauth {
        @GetMapping("/v2/user/me")
        Map<String, Object> getOauthUserInfo(@RequestHeader("Authorization") String access_token);
    }
}
