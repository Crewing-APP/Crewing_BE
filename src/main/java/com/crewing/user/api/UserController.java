package com.crewing.user.api;

import com.crewing.auth.entity.PrincipalDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "유 API")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/user")
public class UserController {

    @GetMapping("/test")
    public void test(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("user = {}", principalDetails.getUser().getRole());
    }
}
