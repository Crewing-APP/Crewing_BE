package com.crewing.external;

import com.crewing.external.StudentVerifyDTO.StudentCreateVerificationRequest;
import com.crewing.external.StudentVerifyDTO.StudentVerifyRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
public class StudentVerifyApi {

    @Component
    @FeignClient(name = "StudentVerifyApi", url = "https://ruuniv-server.xyz/v1")
    public interface StudentVerifyApiClient {
        @PostMapping("/verification/email")
        void createStudentVerification(@RequestHeader String apiKey,
                                       @RequestBody StudentCreateVerificationRequest request);

        @PostMapping("/verification/email/verify")
        void verifyEmail(@RequestHeader String apiKey, @RequestBody StudentVerifyRequest request);
    }

}
