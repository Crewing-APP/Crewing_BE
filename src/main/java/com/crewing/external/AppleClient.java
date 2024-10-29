package com.crewing.external;

import com.crewing.auth.dto.AppleDto;
import com.crewing.auth.dto.ApplePublicKeyResponse;
import com.crewing.auth.dto.AppleTokenResponseDto;
import com.crewing.common.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "appleClient", url = "https://appleid.apple.com/auth", configuration = FeignConfig.class)
public interface AppleClient {
    @GetMapping(value = "/keys")
    ApplePublicKeyResponse findAppleAuthPublicKeys();

    @PostMapping(value = "/token", consumes = "application/x-www-form-urlencoded")
    AppleTokenResponseDto findAppleToken(@RequestBody AppleDto.AppleTokenRequestDto request);

    @PostMapping(value = "/revoke", consumes = "application/x-www-form-urlencoded")
    void revoke(AppleDto.AppleRevokeRequest request);
}
