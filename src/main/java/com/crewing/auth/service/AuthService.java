package com.crewing.auth.service;

import com.crewing.auth.dto.LoginDTO.EmailLoginResponse;
import com.crewing.auth.dto.LoginDTO.LoginResponse;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.jwt.service.JwtService;
import com.crewing.auth.mail.service.MailService;
import com.crewing.auth.oauth.entity.OAuthAttributes;
import com.crewing.common.error.auth.InvalidTokenException;
import com.crewing.common.error.user.UserNotFoundException;
import com.crewing.external.OauthApi;
import com.crewing.user.entity.Role;
import com.crewing.user.entity.SocialType;
import com.crewing.user.entity.User;
import com.crewing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final OauthApi oauthApi;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Transactional
    public TokenResponse loginBasic(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserNotFoundException();
        }

        return getToken(user);
    }

    /**
     * Email 인증을 통한 로그인
     */
    @Transactional
    public EmailLoginResponse loginEmail(String email, String authNumber) {
        boolean verifyResult = mailService.verifySignUpEmail(email, authNumber);

        if (!verifyResult) {
            return EmailLoginResponse.builder()
                    .verifyResult(false)
                    .build();
        }

        log.info("redis end");

        User user = getUserEmail(email);

        TokenResponse tokenResponse = getToken(user);

        if (user.getRole().equals(Role.GUEST)) {
            return EmailLoginResponse.builder()
                    .tokenResponse(tokenResponse)
                    .needSignUp(true)
                    .verifyResult(verifyResult)
                    .build();
        }

        return EmailLoginResponse.builder()
                .tokenResponse(tokenResponse)
                .needSignUp(false)
                .verifyResult(verifyResult)
                .build();
    }

    private TokenResponse getToken(User user) {
        String accessToken = jwtService.createAccessToken(user.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(user, refreshToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Email 유저 조회
     */
    private User getUserEmail(String email) {
        User user = userRepository.findByEmailAndDeleteAt(email).orElse(null);

        if (user == null) {
            return userRepository.save(User.builder()
                    .email(email)
                    .nickname("User")
                    .role(Role.GUEST)
                    .build());
        }

        if (user.getDeleteAt() != null) {
            user.setDeleteAt(null);
            return userRepository.save(user);
        }

        return user;
    }

    /**
     * Oauth 토큰을 통한 로그인
     */
    @Transactional
    public LoginResponse loginOauth(String oauthAccessToken, SocialType socialType) {
        OAuthAttributes attributes = OAuthAttributes.of(socialType,
                oauthApi.getOauthUserInfo(oauthAccessToken, socialType));

        User user = getUserOauth(attributes, socialType);

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
                .needSignUp(false)
                .build();
    }

    /**
     * Oauth 유저 정보를 통한 유저 조회
     */
    private User getUserOauth(OAuthAttributes attributes, SocialType socialType) {
        User findUser = userRepository.findBySocialTypeAndSocialId(socialType, attributes.getOauth2UserInfo().getId())
                .orElse(null);

        if (findUser == null) {
            return saveUser(attributes, socialType);
        }

        if (findUser.getDeleteAt() != null) {
            findUser.setDeleteAt(null);

            return userRepository.save(findUser);
        }

        return findUser;
    }

    /**
     * Oauth 유저 저장
     */
    private User saveUser(OAuthAttributes attributes, SocialType socialType) {
        User createdUser = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        return userRepository.save(createdUser);
    }

    /**
     * 리프레쉬 토큰을 통한 토큰 재발급
     */
    @Transactional
    public TokenResponse reissuedRefreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new InvalidTokenException();
        }

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

    /**
     * 개발용 토큰 발급
     */
    @Transactional
    public TokenResponse getDevToken(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException();
        }
        User user = User.builder()
                .email(email)
                .role(Role.ADMIN)
                .nickname("de1v")
                .name("dev1")
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
}
