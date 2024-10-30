package com.crewing.external;

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

@Component
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();

        log.info("status code = {}", status);
        log.info("methodKey = {}", methodKey);

        if (status == 400) {
            if (methodKey.contains("verifyEmail")) {
                return new StudentInvalidAuthCodeException();
            }
            else if (methodKey.contains("findAppleToken")) {  // Apple Token 요청 예외
                String errorMessage = null;
                try {
                    errorMessage = response.body() != null ? Util.toString(response.body().asReader()) : "Unknown error";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                return new RuntimeException("Apple Token 요청 실패: " + errorMessage);
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
