package com.crewing.auth.service;

import com.crewing.auth.dto.SignUpDTO.BasicSignUpRequest;
import com.crewing.auth.dto.SignUpDTO.SignUpRequest;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.jwt.service.JwtService;
import com.crewing.common.error.auth.NotVerifiedEmailException;
import com.crewing.common.error.user.UserNotFoundException;
import com.crewing.user.entity.Interest;
import com.crewing.user.entity.Role;
import com.crewing.user.entity.User;
import com.crewing.user.repository.InterestRepository;
import com.crewing.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SignUpService {
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Oauth 추가 회원가입
     */
    @Transactional
    public TokenResponse signUp(SignUpRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        List<Interest> interests = request.getInterests().stream()
                .map(interest -> Interest.builder().interest(interest).build()).toList();

        interestRepository.saveAll(interests);

        user.signUpOauth(request.getBirth(), request.getGender(), request.getName(), interests);

        TokenResponse token = jwtService.getToken(user.getEmail(), user.getRole());
        user.setRefreshToken(token.getRefreshToken());

        userRepository.save(user);

        return token;
    }

    /**
     * 기본 회원가입
     */
    @Transactional
    public TokenResponse signUpBasic(BasicSignUpRequest request) {
        if (!request.isVerified()) {
            throw new NotVerifiedEmailException();
        }

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .birth(request.getBirth())
                .nickname(request.getNickname())
                .gender(request.getGender())
                .role(Role.USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        List<Interest> interests = request.getInterests().stream().map(interest -> Interest.builder()
                        .interest(interest)
                        .user(user)
                        .build())
                .toList();

        interestRepository.saveAll(interests);

        user.updateInterests(interests);

        TokenResponse token = jwtService.getToken(user.getEmail(), user.getRole());
        user.updateRefreshToken(token.getRefreshToken());

        userRepository.save(user);

        return token;
    }

    public boolean checkDuplicateEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
