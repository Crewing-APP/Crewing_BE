package com.crewing.external;

import com.crewing.common.error.auth.AppleFeignException;
import com.crewing.common.error.auth.AuthCodeNotFoundException;
import com.crewing.common.error.external.DuplicatedEmailException;
import com.crewing.common.error.external.NotSupportedUniversityException;
import com.crewing.common.error.external.StudentInvalidAuthCodeException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    private String[] appleMethods = {"findAppleToken", "findAppleAuthPublicKeys", "revoke"};
    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();

        log.info("status code = {}", status);
        log.info("methodKey = {}", methodKey);

        if (status == 400) {
            if (methodKey.contains("verifyEmail")) {
                return new StudentInvalidAuthCodeException();
            }
            else if(Arrays.stream(appleMethods).anyMatch(methodKey::contains)) {  // Apple Token 요청 예외
                String errorMessage;
                try {
                    errorMessage = response.body() != null ? Util.toString(response.body().asReader()) : "Unknown error";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return new AppleFeignException("Apple 서버 요청 실패: " + errorMessage,status);
            }
        } else if (status == 404) {
            if (methodKey.contains("createStudentVerification")) {
                return new NotSupportedUniversityException();
            } else if (methodKey.contains("verifyEmail")) {
                return new AuthCodeNotFoundException();
            }
        } else if (status == 409) {
            if (methodKey.contains("verifyEmail")) {
                return new DuplicatedEmailException();
            }
        }
        return new Exception();
    }
}
