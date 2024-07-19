package com.crewing.auth.service;

import com.crewing.auth.dto.SignUpDTO.BasicSignUpRequest;
import com.crewing.auth.dto.SignUpDTO.OauthSignUpRequest;
import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import com.crewing.auth.jwt.service.JwtService;
import com.crewing.common.error.auth.NotVerifiedEmailException;
import com.crewing.common.error.user.UserNotFoundException;
import com.crewing.user.entity.Interest;
import com.crewing.user.entity.Role;
import com.crewing.user.entity.User;
import com.crewing.user.repository.InterestRepository;
import com.crewing.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpService {
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final JwtService jwtService;

    /**
     * Oauth 추가 회원가입
     */
    public TokenResponse signUpOauth(OauthSignUpRequest request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        List<Interest> interests = new ArrayList<>();
        request.getInterests().forEach(
                interest -> {
                    Interest save = Interest.builder().interest(interest).build();
                    interests.add(save);
                }
        );
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
                .build();

        List<Interest> interests = new ArrayList<>();
        request.getInterests().forEach(
                interest -> {
                    Interest save = Interest.builder().interest(interest).build();
                    interests.add(save);
                }
        );
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
