package com.crewing.auth.oauth.entity;

import com.crewing.user.entity.Role;
import com.crewing.user.entity.SocialType;
import com.crewing.user.entity.User;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

    private OAuth2UserInfo oauth2UserInfo; // 소셜 타입별 로그인 유저 정보(닉네임, 이메일, 프로필 사진 등등)

    @Builder
    private OAuthAttributes(OAuth2UserInfo oauth2UserInfo) {
        this.oauth2UserInfo = oauth2UserInfo;
    }

    /**
     * SocialType에 맞는 메소드 호출하여 OAuthAttributes 객체 반환 파라미터 : userNameAttributeName -> OAuth2 로그인 시 키(PK)가 되는 값 /
     * attributes : OAuth 서비스의 유저 정보들 소셜별 of 메소드(ofGoogle, ofKaKao, ofNaver)들은 각각 소셜 로그인 API에서 제공하는 회원의 식별값(id),
     * attributes, nameAttributeKey를 저장 후 build
     */
    public static OAuthAttributes of(SocialType socialType, Map<String, Object> attributes) {

        if (socialType == SocialType.NAVER) {
            return ofNaver(attributes);
        }
        if (socialType == SocialType.KAKAO) {
            return ofKakao(attributes);
        }
        return ofGoogle(attributes);
    }

    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofNaver(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    public User toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        return User.builder()
                .socialType(socialType)
                .socialId(oauth2UserInfo.getId())
                .email(UUID.randomUUID() + "@socialUser.com")
                .nickname(oauth2UserInfo.getNickname())
                .profileImage(oauth2UserInfo.getImageUrl())
                .role(Role.GUEST)
                .build();
    }

}
