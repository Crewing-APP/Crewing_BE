package com.crewing.admin.service;

import com.crewing.common.error.user.UserNotFoundException;
import com.crewing.user.dto.UserDTO;
import com.crewing.user.dto.UserDTO.UserInfoResponse;
import com.crewing.user.dto.UserDTO.UserInfoResponses;
import com.crewing.user.entity.User;
import com.crewing.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final UserRepository userRepository;

    public UserInfoResponses getAllUserInfo() {
        List<User> users = userRepository.findAll();

        return UserInfoResponses.toDTO(users);
    }

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        return UserDTO.UserInfoResponse.toDTO(user);
    }
}
