package com.crewing.auth.service;

import com.crewing.auth.dto.LoginDTO.LoginResponse;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.jwt.service.JwtService;
import com.crewing.external.OauthApi.GoogleOauth;
import com.crewing.external.OauthApi.KakaoOauth;
import com.crewing.external.OauthApi.NaverOauth;
import com.crewing.user.entity.Role;
import com.crewing.user.entity.SocialType;
import com.crewing.user.entity.User;
import com.crewing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final GoogleOauth googleOauth;
    private final NaverOauth naverOauth;
    private final KakaoOauth kakaoOauth;
    private final UserRepository userRepository;

    private final JwtService jwtService;

    public LoginResponse loginOauth(String oauthAccessToken, String social) {
        SocialType socialType = SocialType.valueOf(social);
        OAuthAttributes attributes = OAuthAttributes.of(socialType,
                googleOauth.getOauthUserInfo(oauthAccessToken));

        User user = getUser(attributes, socialType);

        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(user.getEmail(), refreshToken);

        TokenResponse tokenResponse = TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken)
                .build();

        if (user.getRole().equals(Role.GUEST)) {
            return LoginResponse.builder()
                    .tokenResponse(tokenResponse)
                    .needSignUp(true)
                    .build();
        }

        return LoginResponse.builder()
                .tokenResponse(tokenResponse)
                .build();
    }

    private User getUser(OAuthAttributes attributes, SocialType socialType) {
        User findUser = userRepository.findBySocialTypeAndSocialId(socialType, attributes.getOauth2UserInfo().getId())
                .orElse(null);

        if (findUser == null) {
            return saveUser(attributes, socialType);
        }

        return findUser;
    }

    private User saveUser(OAuthAttributes attributes, SocialType socialType) {
        User createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        return userRepository.save(createdUser);
    }


}
