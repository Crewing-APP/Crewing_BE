package com.crewing.auth;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.crewing.auth.dto.LoginDTO.OauthLoginResponse;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.jwt.service.JwtService;
import com.crewing.auth.service.AuthService;
import com.crewing.common.error.auth.InvalidTokenException;
import com.crewing.common.error.user.UserNotFoundException;
import com.crewing.external.OauthApi;
import com.crewing.user.entity.Role;
import com.crewing.user.entity.SocialType;
import com.crewing.user.entity.User;
import com.crewing.user.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    AuthService authService;

    @Mock
    UserRepository userRepository;

    @Mock
    JwtService jwtService;

    @Mock
    OauthApi oauthApi;

    /**
     * Oauth Login Test
     */
    @Test
    @DisplayName("Oauth Google Login Success Test")
    void OauthGoogleLoginSuccessTest() {
        SocialType google = SocialType.GOOGLE;
        Map<String, Object> attribute = new HashMap<>();
        attribute.put("id", "test");
        attribute.put("name", "test");
        attribute.put("picture", "test");

        given(oauthApi.getOauthUserInfo("eyzasd....", google)).willReturn(attribute);

        given(jwtService.createAccessToken(any())).willReturn("testAccessToken");
        given(jwtService.createRefreshToken()).willReturn("test");

        given(userRepository.findBySocialTypeAndSocialId(any(), any())).willReturn(Optional.of(User.builder()
                .role(Role.USER)
                .build()));

        OauthLoginResponse test = authService.loginOauth("eyzasd....", google);

        Assertions.assertFalse(test.isNeedSignUp());
        Assertions.assertEquals("testAccessToken", test.getTokenResponse().getAccessToken());
    }

    @Test
    @DisplayName("Oauth Naver Login Success Test")
    void OauthNaverLoginSuccessTest() {
        SocialType naver = SocialType.NAVER;
        Map<String, Object> attribute = new HashMap<>();

        attribute.put("id", "test");
        attribute.put("nickname", "test");
        attribute.put("profile_image", "test");

        given(oauthApi.getOauthUserInfo("eyzasd...", naver)).willReturn(attribute);

        given(jwtService.createAccessToken(any())).willReturn("testAccessToken");
        given(jwtService.createRefreshToken()).willReturn("test");

        given(userRepository.findBySocialTypeAndSocialId(any(), any())).willReturn(Optional.of(User.builder()
                .role(Role.USER)
                .build()));

        OauthLoginResponse test = authService.loginOauth("eyzasd...", naver);

        Assertions.assertFalse(test.isNeedSignUp());
        Assertions.assertEquals("testAccessToken", test.getTokenResponse().getAccessToken());
    }

    @Test
    @DisplayName("Oauth KaKao Login Success Test")
    void OauthKaKaoLoginSuccessTest() {
        SocialType kakao = SocialType.KAKAO;
        Map<String, Object> attribute = new HashMap<>();
        attribute.put("id", "test");
        attribute.put("name", "test");
        attribute.put("picture", "test");

        given(oauthApi.getOauthUserInfo("eyzasd...", kakao)).willReturn(attribute);

        given(jwtService.createAccessToken(any())).willReturn("testAccessToken");
        given(jwtService.createRefreshToken()).willReturn("test");

        given(userRepository.findBySocialTypeAndSocialId(any(), any())).willReturn(Optional.of(User.builder()
                .role(Role.USER)
                .build()));

        OauthLoginResponse test = authService.loginOauth("eyzasd...", kakao);

        Assertions.assertFalse(test.isNeedSignUp());
        Assertions.assertEquals("testAccessToken", test.getTokenResponse().getAccessToken());
    }

    @Test
    @DisplayName("Oauth KaKao Login Success Test")
    void OauthLoginIsSignUpTest() {
        SocialType kakao = SocialType.KAKAO;
        Map<String, Object> attribute = new HashMap<>();
        attribute.put("id", "test");
        attribute.put("name", "test");
        attribute.put("picture", "test");

        given(oauthApi.getOauthUserInfo("eyzasd...", kakao)).willReturn(attribute);

        given(jwtService.createAccessToken(any())).willReturn("testAccessToken");
        given(jwtService.createRefreshToken()).willReturn("test");

        given(userRepository.findBySocialTypeAndSocialId(any(), any())).willReturn(Optional.of(User.builder()
                .role(Role.GUEST)
                .build()));

        OauthLoginResponse test = authService.loginOauth("eyzasd...", kakao);

        Assertions.assertTrue(test.isNeedSignUp());
    }

    /**
     * reissuedRefreshToken Test
     */

    @Test
    @DisplayName("reissuedRefreshToken Success Test")
    void reissuedRefreshTokenSuccessTest() {
        User test = User.builder()
                .refreshToken("eyj..test")
                .build();

        given(jwtService.isTokenValid("eyj..test")).willReturn(true);
        given(userRepository.findByRefreshToken("eyj..test")).willReturn(Optional.of(test));
        given(jwtService.createAccessToken(any())).willReturn("testAccessToken");
        given(jwtService.createRefreshToken()).willReturn("test");

        TokenResponse response = authService.reissuedRefreshToken("eyj..test");

        Assertions.assertEquals("test", response.getRefreshToken());
    }

    @Test
    @DisplayName("reissuedRefreshToken Invalid Token Exception Test")
    void reissuedRefreshTokenInvalidTokenExceptionTest() {
        given(jwtService.isTokenValid("eyj..test")).willReturn(false);

        Assertions.assertThrows(InvalidTokenException.class,
                () -> authService.reissuedRefreshToken("eyj..test"));
    }

    @Test
    @DisplayName("reissuedRefreshToken Not Match Token Exception Test")
    void reissuedRefreshTokenNotMatchTokenExceptionTest() {
        given(jwtService.isTokenValid("eyj..test")).willReturn(true);
        given(userRepository.findByRefreshToken("eyj..test")).willReturn(Optional.empty());
        Assertions.assertThrows(UserNotFoundException.class,
                () -> authService.reissuedRefreshToken("eyj..test"));
    }
}
