package com.crewing.auth.oauth.service;

import com.crewing.auth.jwt.service.JwtService;
import com.crewing.auth.oauth.entity.CustomOAuth2User;
import com.crewing.user.entity.Role;
import com.crewing.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        log.info("[AUTH] : OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
            if (oAuth2User.getRole() == Role.GUEST) {
                log.info("[AUTH] : 추가 회원가입 폼 진입");
                String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
                String refreshToken = jwtService.createRefreshToken();
                jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);

                jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken, oAuth2User.getRole());


            } else {
                loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
            }
        } catch (Exception e) {
            throw e;
        }

    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken, oAuth2User.getRole());

    }

}
