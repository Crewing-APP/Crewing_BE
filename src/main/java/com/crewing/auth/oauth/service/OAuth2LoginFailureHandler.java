package com.crewing.auth.oauth.service;

import com.crewing.common.error.ErrorCode;
import com.crewing.common.error.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OAuth2LoginFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        response.getWriter().write(
                ErrorResponse.of(ErrorCode.USER_ACCESS_DENIED, request.getRequestURI()).convertToJson()
        );
        log.info("[AUTH] : 소셜 로그인에 실패했습니다. 에러 메시지 : {}", exception.getMessage());
    }

}
