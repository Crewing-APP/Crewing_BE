package com.crewing.auth;

import com.crewing.auth.dto.AppleTokenResponseDto;
import com.crewing.common.util.AppleAuthUtil;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.PrivateKey;

@SpringBootTest
@Transactional
public class AppleUtilTest {
    @Autowired
    private AppleAuthUtil appleAuthUtil;

    @Test
    @DisplayName("private key 생성")
    void getPrivateKey() throws IOException {
        PrivateKey privateKey = appleAuthUtil.getPrivateKey();
        Assertions.assertThat(privateKey).isNotNull();
    }

    @Test
    @DisplayName("애플 토큰 요청")
    void getAppleToken(){
        AppleTokenResponseDto response = appleAuthUtil.getAppleToken("exampleauthorizationcode33");
        System.out.println(response.error());
    }
}
