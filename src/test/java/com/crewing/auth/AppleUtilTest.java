package com.crewing.auth;

import com.crewing.common.util.AppleAuthUtil;
import jakarta.transaction.Transactional;
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
        System.out.println(privateKey);
    }
}
