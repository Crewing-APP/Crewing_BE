package com.crewing.user.repository;

import com.crewing.user.entity.SocialType;
import com.crewing.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u from User u where u.id = :userId and u.deleteAt is null")
    Optional<User> findById(Long userId);

    @Query("SELECT u from User u where u.email = :email and u.deleteAt is null")
    Optional<User> findByEmail(String email);

    @Query("SELECT u from User u where u.refreshToken = :refreshToken and u.deleteAt is null")
    Optional<User> findByRefreshToken(String refreshToken);

    @Query("SELECT u from User u where u.socialType = :socialType and u.socialId = :socialId and u.deleteAt is null")
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    boolean existsByEmail(String email);

}
