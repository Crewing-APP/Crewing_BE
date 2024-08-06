package com.crewing.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.crewing.auth.mail.service.MailService;
import com.crewing.common.util.RedisUtil;
import com.crewing.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
public class MailServiceTest {
    @InjectMocks
    MailService mailService;

    @Mock
    JavaMailSender javaMailSender;

    @Mock
    RedisUtil redisUtil;

    @Mock
    UserRepository userRepository;

    @Test
    @DisplayName("Verify SignUp Email Success Test")
    void verifySignUpEmailSuccessTest() {
        String email = "test@test.com";
        String redisKey = "REDIS";
        String authNumber = "123456";

        given(redisUtil.getData(any(), any())).willReturn(authNumber);

        boolean response = mailService.verifySignUpEmail(email, authNumber);

        Assertions.assertTrue(response);
    }

    @Test
    @DisplayName("Verify SignUp Email Fail Test (Invalid Auth Number)")
    void verifySignUpEmailFailInvalidAuthNumberTest() {
        String email = "test@test.com";
        String redisKey = "REDIS";
        String authNumber = "123456";

        given(redisUtil.getData(anyString(), any())).willReturn("654321");

        boolean response = mailService.verifySignUpEmail(email, authNumber);

        Assertions.assertFalse(response);
    }

    @Test
    @DisplayName("Verify SignUp Email Fail Test (Redis Data Is Null)")
    void verifySignUpEmailFailRedisDataIsNullTest() {
        String email = "test@test.com";
        String redisKey = "REDIS";
        String authNumber = "123456";

        given(redisUtil.getData(any(), any())).willReturn(null);

        boolean response = mailService.verifySignUpEmail(email, authNumber);

        Assertions.assertFalse(response);
    }
}
