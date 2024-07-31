package com.crewing.auth.mail.service;

import com.crewing.common.error.auth.AuthCodeNotFoundException;
import com.crewing.common.error.auth.InvalidAuthCodeException;
import com.crewing.common.util.RedisUtil;
import com.crewing.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private static final String REDIS_KEY = "EMAIL_VERIFY:";

    private final JavaMailSender mailSender;
    private int authNumber;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    //임의의 6자리 양수를 반환합니다.
    private void makeRandomNumber() {
        Random r = new Random();
        String randomNumber = "";
        for (int i = 0; i < 6; i++) {
            randomNumber += Integer.toString(r.nextInt(10));
        }

        authNumber = Integer.parseInt(randomNumber);
    }


    //mail을 어디서 보내는지, 어디로 보내는지 , 인증 번호를 html 형식으로 어떻게 보내는지 작성합니다.
    @Async
    public void sendSignUpEmail(String email) {
        makeRandomNumber();
        String setFrom = "maildevgogo@gmail.com"; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = email;
        String title = "Crewing 회원 가입 인증 이메일 입니다."; // 이메일 제목
        String content =
                "Crewing에 방문해주셔서 감사합니다." +
                        "<br><br>" +
                        "회원가입 인증 번호는 " + authNumber + "입니다." +
                        "<br>" +
                        "인증번호를 제대로 입력해주세요";
        mailSend(setFrom, toMail, title, content);
    }

    //이메일을 전송합니다.
    private void mailSend(String setFrom, String toMail, String title, String content) {
        redisUtil.deleteData(toMail);
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");//이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content, true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
            // 이러한 경우 MessagingException이 발생
            e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
        }
        redisUtil.setDataExpire(REDIS_KEY + toMail, Integer.toString(authNumber), 60 * 5L);
    }

    public boolean verifySignUpEmail(String email, String inputAuthNum) {
        String redisKey = REDIS_KEY + email;
        String authCode = redisUtil.getData(redisKey);

        if (authCode == null) {
            throw new AuthCodeNotFoundException();
        } else if (!authCode.equals(inputAuthNum)) {
            throw new InvalidAuthCodeException();
        }

        redisUtil.deleteData(redisKey);

        return true;
    }
}
