package com.crewing.user;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.crewing.common.error.BusinessException;
import com.crewing.file.service.FileService;
import com.crewing.user.dto.UserDTO.InterestInfo;
import com.crewing.user.dto.UserDTO.InterestUpdateRequest;
import com.crewing.user.entity.User;
import com.crewing.user.repository.InterestRepository;
import com.crewing.user.repository.UserRepository;
import com.crewing.user.service.UserService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    InterestRepository interestRepository;

    @Mock
    FileService fileService;

    /**
     * Update Interest Success Test
     */
    @Test
    @DisplayName("Update Interest Success Test")
    void updateInterestSuccessTest() {
        List<InterestInfo> interests = new ArrayList<>();
        interests.add(new InterestInfo("test1"));
        interests.add(new InterestInfo("test2"));

        User user = User.builder().build();

        InterestUpdateRequest request = InterestUpdateRequest.builder()
                .interests(interests)
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        userService.updateInterest(0L, request);

        verify(userRepository).save(user);
    }

    /**
     * Update User Profile Image Test
     */
    @Test
    @DisplayName("Update User Profile Image Success Test")
    void updateUserProfileImageSuccessTest() throws IOException {
        User user = User.builder()
                .profileImage("test")
                .build();

        MockMultipartFile image = new MockMultipartFile(
                "images", //name
                "test.png", //originalFilename
                "png",
                "image".getBytes()
        );

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(fileService.uploadFile(image)).willReturn("newImage");

        userService.updateUserProfileImage(0L, image);

        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Update User Profile Image Exception Test")
    void updateUserProfileImageExceptionTest() throws IOException {
        User user = User.builder()
                .profileImage("test")
                .build();

        MockMultipartFile image = new MockMultipartFile(
                "images", //name
                "test.png", //originalFilename
                "png",
                "image".getBytes()
        );

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
        given(fileService.uploadFile(image)).willThrow(IOException.class);

        Assertions.assertThrows(BusinessException.class,
                () -> userService.updateUserProfileImage(0L, image));
    }

    @Test
    @DisplayName("Delete User Success Test")
    void deleteUserTest() {
        User user = User.builder()
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(user));

        userService.deleteUser(0L);

        verify(userRepository).save(user);
    }
}
