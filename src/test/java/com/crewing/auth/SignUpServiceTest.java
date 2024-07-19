package com.crewing.auth;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import com.crewing.auth.dto.SignUpDTO.BasicSignUpRequest;
import com.crewing.auth.dto.SignUpDTO.OauthSignUpRequest;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.jwt.service.JwtService;
import com.crewing.auth.service.SignUpService;
import com.crewing.common.error.auth.NotVerifiedEmailException;
import com.crewing.user.entity.Role;
import com.crewing.user.entity.User;
import com.crewing.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SignUpServiceTest {

    @InjectMocks
    SignUpService signUpService;

    @Mock
    UserRepository userRepository;

    @Mock
    JwtService jwtService;

    /**
     * SignUp Oauth (오어스 추가 회원가입)
     */

    @Test
    @DisplayName("SignUp Oauth Success Test")
    void signUpOauthSuccessTest() {
        List<String> interests = new ArrayList<>();
        interests.add("test1");
        interests.add("test2");

        OauthSignUpRequest request = OauthSignUpRequest.builder()
                .birth("2000-05-04")
                .name("test")
                .gender("man")
                .nickname("test")
                .interests(interests)
                .build();

        User user = User.builder()
                .email("test@test.com")
                .role(Role.GUEST)
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(jwtService.getToken("test@test.com", Role.USER)).willReturn(
                TokenResponse.builder()
                        .accessToken("eyj...")
                        .refreshToken("eyjr...")
                        .role(Role.USER)
                        .build()
        );

        TokenResponse response = signUpService.signUpOauth(request, 0L);

        Assertions.assertEquals("eyj...", response.getAccessToken());
        Assertions.assertEquals("eyjr...", response.getRefreshToken());
        Assertions.assertEquals(Role.USER, response.getRole());
    }

    /**
     * SignUp Basic (기본 회원가입)
     */
    @Test
    @DisplayName("SignUp Basic Success Test")
    void signUpBasicSuccessTest() {
        List<String> interests = new ArrayList<>();
        interests.add("test1");
        interests.add("test2");

        BasicSignUpRequest request = BasicSignUpRequest.builder()
                .birth("2000-05-04")
                .nickname("test")
                .verified(true)
                .gender("man")
                .name("test")
                .email("test@test.com")
                .interests(interests)
                .build();

        given(jwtService.getToken("test@test.com", Role.USER)).willReturn(
                TokenResponse.builder()
                        .accessToken("eyj...")
                        .refreshToken("eyjr...")
                        .role(Role.USER)
                        .build()
        );

        TokenResponse response = signUpService.signUpBasic(request);

        Assertions.assertEquals("eyj...", response.getAccessToken());
        Assertions.assertEquals("eyjr...", response.getRefreshToken());
        Assertions.assertEquals(Role.USER, response.getRole());
    }

    @Test
    @DisplayName("SignUp Basic Not Verified Exception Test")
    void signUpBasicNotVerifiedExceptionTest() {
        List<String> interests = new ArrayList<>();
        interests.add("test1");
        interests.add("test2");

        BasicSignUpRequest request = BasicSignUpRequest.builder()
                .birth("2000-05-04")
                .nickname("test")
                .verified(false) // email 인증 여부
                .gender("man")
                .name("test")
                .email("test@test.com")
                .interests(interests)
                .build();

        Assertions.assertThrows(NotVerifiedEmailException.class, () -> signUpService.signUpBasic(request));
    }
}
