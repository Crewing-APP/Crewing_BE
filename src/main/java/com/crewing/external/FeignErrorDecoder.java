package com.crewing.external;

import com.crewing.common.error.auth.AuthCodeNotFoundException;
import com.crewing.common.error.external.DuplicatedEmailException;
import com.crewing.common.error.external.NotSupportedUniversityException;
import com.crewing.common.error.external.StudentInvalidAuthCodeException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
