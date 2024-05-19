package com.crewing.auth.jwt.service;

import com.crewing.common.error.ErrorCode;
import com.crewing.common.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Slf4j
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(
                ErrorResponse.of(ErrorCode.USER_ACCESS_DENIED, request.getRequestURI())
                        .convertToJson()
        );

        log.info("[AUTH] : 로그인에 실패했습니다. 메시지 : {}", exception.getMessage());
    }

}
