package com.crewing.auth.oauth.entity;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NaverOAuth2UserInfo extends OAuth2UserInfo {
    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
        log.info("asdasdasd {}", attributes.entrySet());
    }

    @Override
    public String getId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        return (String) response.get("id");
    }

    @Override
    public String getNickname() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return "NAVER" + getId();
        }

        return (String) response.get("nickname");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        return (String) response.get("profile_image");
    }
}
