package com.crewing.mail.service;


import com.crewing.auth.jwt.service.JwtService;
import com.crewing.common.util.RedisUtil;
import com.crewing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailVerifyService {
    private final RedisUtil redisUtil;
    private final JwtService jwtService;
    private final UserRepository userRepository;

}
