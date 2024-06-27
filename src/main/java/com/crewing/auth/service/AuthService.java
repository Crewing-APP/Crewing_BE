package com.crewing.auth.service;

import com.crewing.auth.dto.LoginDTO.LoginResponse;
import com.crewing.auth.dto.SignUpDTO.BasicSignUpRequest;
import com.crewing.auth.dto.SignUpDTO.OauthSignUpRequest;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.jwt.service.JwtService;
import com.crewing.common.error.user.UserNotFoundException;
import com.crewing.external.OauthApi;
import com.crewing.user.entity.Interest;
import com.crewing.user.entity.Role;
import com.crewing.user.entity.SocialType;
import com.crewing.user.entity.User;
import com.crewing.user.repository.InterestRepository;
import com.crewing.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final InterestRepository interestRepository;
    private final OauthApi oauthApi;
    private final PasswordEncoder passwordEncoder;

    public LoginResponse loginOauth(String oauthAccessToken, SocialType socialType) {
        OAuthAttributes attributes = OAuthAttributes.of(socialType,
                oauthApi.getOauthUserInfo(oauthAccessToken, socialType));

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

    public TokenResponse signUpOauth(OauthSignUpRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        List<Interest> interests = new ArrayList<>();
        request.getInterests().forEach(
                interest -> {
                    Interest save = Interest.builder().interest(interest).build();
                    interests.add(save);
                }
        );
        interestRepository.saveAll(interests);

        user.signUpOauth(request.getBirth(), request.getGender(), request.getName(), interests);

        TokenResponse token = getToken(user.getEmail());
        user.setRefreshToken(token.getRefreshToken());

        userRepository.save(user);

        return token;
    }

    public TokenResponse signUpBasic(BasicSignUpRequest request){
        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .birth(request.getBirth())
                .nickname(request.getNickname())
                .gender(request.getGender())
                .role(Role.USER)
                .build();

        List<Interest> interests = new ArrayList<>();
        request.getInterests().forEach(
                interest -> {
                    Interest save = Interest.builder().interest(interest).build();
                    interests.add(save);
                }
        );
        interestRepository.saveAll(interests);

        user.updateInterests(interests);

        TokenResponse token = getToken(user.getEmail());
        user.updateRefreshToken(token.getRefreshToken());

        userRepository.save(user);

        return token;
    }

    public TokenResponse getDevToken(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException();
        }
        User user = User.builder()
                .email(email)
                .role(Role.ADMIN)
                .nickname("dev")
                .name("dev")
                .password("1234")
                .birth("dev")
                .build();

        user.passwordEncode(passwordEncoder);

        userRepository.save(user);

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(email, refreshToken);

        return TokenResponse.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }

    public TokenResponse getToken(String email){
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public TokenResponse reissuedRefreshToken(String refreshToken) {
        User user = userRepository.findByRefreshToken(refreshToken).orElseThrow(
                UserNotFoundException::new);
        String reissuedRefreshToken = jwtService.createRefreshToken();
        String accessToken = jwtService.createAccessToken(user.getEmail());

        user.setRefreshToken(reissuedRefreshToken);

        userRepository.save(user);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(reissuedRefreshToken)
                .build();
    }
}
