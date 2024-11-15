package com.crewing.auth;

import com.crewing.common.error.auth.AppleFeignException;
import com.crewing.common.util.AppleAuthUtil;
import com.crewing.common.util.KmsUtil;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.security.PrivateKey;

@SpringBootTest
@Transactional
public class AppleUtilTest {
    @Autowired
    private AppleAuthUtil appleAuthUtil;
    @Autowired
    private KmsUtil kmsUtil;
    @Value("${aws.kms.key-id}")

    @Test
    @DisplayName("private key 생성")
    void getPrivateKey() throws IOException {
        PrivateKey privateKey = appleAuthUtil.getPrivateKey();
        Assertions.assertThat(privateKey).isNotNull();
    }

    @Test
    @DisplayName("애플 토큰 요청 시 잘못된 토큰 파라미터")
    void getAppleToken_InvalidToken_AppleFeignException(){
        Assertions.assertThatThrownBy(()->appleAuthUtil.getAppleToken("exampleauthorizationcode33"))
                .isInstanceOf(AppleFeignException.class)
                .hasMessageContaining("Apple 서버 요청 실패");
    }

    @Test
    @DisplayName("토큰 암호화 후 복호화 검증")
    void tokenDecryption_After_Encryption() throws Exception {
        String token = "dfjalkdsjflaskdfjlj.test.test.testestest"; // 임의의 토큰
        String encryption = kmsUtil.encrypt(token);
        String decryption = kmsUtil.decrypt(encryption);
        Assertions.assertThat(decryption).isEqualTo(token);
    }
}
