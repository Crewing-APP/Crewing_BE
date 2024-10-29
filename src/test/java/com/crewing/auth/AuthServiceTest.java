package com.crewing.auth;

import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;

import com.crewing.auth.dto.LoginDTO.EmailLoginResponse;
import com.crewing.auth.dto.LoginDTO.LoginResponse;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.jwt.service.JwtService;
import com.crewing.auth.mail.service.MailService;
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

    @Mock
    MailService mailService;

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

        LoginResponse test = authService.loginOauth("eyzasd....","", google);

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

        LoginResponse test = authService.loginOauth("eyzasd...","", naver);

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

        LoginResponse test = authService.loginOauth("eyzasd...","", kakao);

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

        LoginResponse test = authService.loginOauth("eyzasd...","", kakao);

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

    @Test
    @DisplayName("Login Email Success Test (Valid Auth Number) (need Sign Up) (Not WithDraw User)")
    void loginEmailValidAuthNumberNeedSignUpNotWithDrawUserTest() {
        String email = "test@test.com";
        String authNumber = "123456";
        User user = User.builder()
                .email(email)
                .nickname("User")
                .role(Role.GUEST)
                .build();
        String accessToken = "eyj..";
        String refreshToken = "eyj,,..";

        given(mailService.verifySignUpEmail(email, authNumber)).willReturn(true);
        given(userRepository.findByEmailAndDeleteAt(email)).willReturn(Optional.empty());
        given(userRepository.save(any())).willReturn(user);
        given(jwtService.createAccessToken(email)).willReturn(accessToken);
        given(jwtService.createRefreshToken()).willReturn(refreshToken);

        EmailLoginResponse response = authService.loginEmail(email, authNumber);

        Assertions.assertEquals(accessToken, response.getTokenResponse().getAccessToken());
        Assertions.assertEquals(refreshToken, response.getTokenResponse().getRefreshToken());
        Assertions.assertTrue(response.isNeedSignUp());
        Assertions.assertTrue(response.isVerifyResult());
    }

    @Test
    @DisplayName("Login Email Success Test (Invalid Auth Number)")
    void loginEmailInValidAuthNumber() {
        String email = "test@test.com";
        String authNumber = "123456";

        given(mailService.verifySignUpEmail(email, authNumber)).willReturn(false);

        EmailLoginResponse response = authService.loginEmail(email, authNumber);

        Assertions.assertFalse(response.isVerifyResult());
    }

    @Test
    @DisplayName("Login Email Success Test (Valid Auth Number) (Not need Sign Up) (Not WithDraw User)")
    void loginEmailValidAuthNumberNotNeedSignUpNotWithDrawUserTest() {
        String email = "test@test.com";
        String authNumber = "123456";
        User user = User.builder()
                .email(email)
                .nickname("User")
                .role(Role.USER)
                .build();
        String accessToken = "eyj..";
        String refreshToken = "eyj,,..";

        given(mailService.verifySignUpEmail(email, authNumber)).willReturn(true);
        given(userRepository.findByEmailAndDeleteAt(email)).willReturn(Optional.of(user));
        given(jwtService.createAccessToken(email)).willReturn(accessToken);
        given(jwtService.createRefreshToken()).willReturn(refreshToken);

        EmailLoginResponse response = authService.loginEmail(email, authNumber);

        Assertions.assertEquals(accessToken, response.getTokenResponse().getAccessToken());
        Assertions.assertEquals(refreshToken, response.getTokenResponse().getRefreshToken());
        Assertions.assertFalse(response.isNeedSignUp());
        Assertions.assertTrue(response.isVerifyResult());
    }

    @Test
    @DisplayName("Login Email Success Test (Valid Auth Number) (Not need Sign Up) (WithDraw User)")
    void loginEmailValidAuthNumberNotNeedSignUpWithDrawUserTest() {
        String email = "test@test.com";
        String authNumber = "123456";
        User user = User.builder()
                .email(email)
                .nickname("User")
                .role(Role.USER)
                .build();
        user.delete();

        String accessToken = "eyj..";
        String refreshToken = "eyj,,..";

        given(mailService.verifySignUpEmail(email, authNumber)).willReturn(true);
        given(userRepository.findByEmailAndDeleteAt(email)).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);
        given(jwtService.createAccessToken(email)).willReturn(accessToken);
        given(jwtService.createRefreshToken()).willReturn(refreshToken);

        EmailLoginResponse response = authService.loginEmail(email, authNumber);

        Assertions.assertEquals(accessToken, response.getTokenResponse().getAccessToken());
        Assertions.assertEquals(refreshToken, response.getTokenResponse().getRefreshToken());
        Assertions.assertFalse(response.isNeedSignUp());
        Assertions.assertTrue(response.isVerifyResult());
    }
}
