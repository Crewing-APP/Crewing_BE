package com.crewing.external;

import com.crewing.user.entity.SocialType;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Component
public class OauthApi {
    private final GoogleOauth googleOauth;
    private final NaverOauth naverOauth;
    private final KakaoOauth kakaoOauth;


    public Map<String, Object> getOauthUserInfo(String accessToken, SocialType socialType) {
        String BEARER = "Bearer ";
        if (socialType.equals(SocialType.GOOGLE)) {
            return googleOauth.getOauthUserInfo(accessToken);
        } else if (socialType.equals(SocialType.NAVER)) {
            return naverOauth.getOauthUserInfo(BEARER + accessToken);
        } else if (socialType.equals(SocialType.KAKAO)) {
            return kakaoOauth.getOauthUserInfo(BEARER + accessToken);
        }
        return null;
    }

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
