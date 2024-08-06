package com.crewing.auth.handler;

import com.crewing.common.error.ErrorCode;
import com.crewing.common.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        log.error("[AUTH] : 권한이 없는 사용자 접근 {}", accessDeniedException.getMessage(), accessDeniedException);

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(401);
        response.getWriter().write(
                ErrorResponse.of(ErrorCode.USER_ACCESS_DENIED, request.getRequestURI()).convertToJson()
        );
    }
}
