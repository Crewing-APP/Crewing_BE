package com.crewing.user;

import com.crewing.user.entity.Role;
import com.crewing.user.entity.User;
import com.crewing.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    private User user;

    @BeforeEach
    void before() {
        user = User.builder()
                .name("test")
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    @AfterEach
    void after() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Find By User Success Test")
    void findByUserSuccessTest() {
        Optional<User> user = userRepository.findById(1L);

        Assertions.assertTrue(user.isPresent());
    }

    @Test
    @DisplayName("Find By User Fail Test (Delete At)")
    void findByUserFailTest() {
        User user = userRepository.findById(1L).get();
        user.delete();
        userRepository.save(user);

        Optional<User> afterUser = userRepository.findById(1L);

        Assertions.assertFalse(afterUser.isPresent());
    }

    @Test
    @DisplayName("Find All By Delete At After Year")
    void findAllByDeleteAtAfterYear() {
        User user = User.builder()
                .email("test")
                .role(Role.USER)
                .build();
        user.setDeleteAt(LocalDate.of(2024, 1, 1));

        userRepository.save(user);

        List<User> allByDeleteAtAfterYear = userRepository.findAllByDeleteAtBeforeTime(
                LocalDate.of(2026, 1, 1));

        Assertions.assertEquals("test", allByDeleteAtAfterYear.get(0).getEmail());
    }
}
