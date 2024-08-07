package com.crewing.auth;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;

import com.crewing.auth.dto.SignUpDTO.SignUpRequest;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.jwt.service.JwtService;
import com.crewing.auth.service.SignUpService;
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
     * SignUp (추가 회원가입)
     */

    @Test
    @DisplayName("SignUp Success Test")
    void signUpSuccessTest() {
        List<String> interests = new ArrayList<>();
        interests.add("test1");
        interests.add("test2");

        SignUpRequest request = SignUpRequest.builder()
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

        TokenResponse response = signUpService.signUp(request, 0L);

        Assertions.assertEquals("eyj...", response.getAccessToken());
        Assertions.assertEquals("eyjr...", response.getRefreshToken());
        Assertions.assertEquals(Role.USER, response.getRole());
    }


}
