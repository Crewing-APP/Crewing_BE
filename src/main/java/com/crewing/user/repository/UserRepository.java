package com.crewing.user.repository;

import com.crewing.user.entity.SocialType;
import com.crewing.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u from User u where u.id = :userId and u.deleteAt is null")
    Optional<User> findById(Long userId);

    @Query("SELECT u from User u where u.email = :email and u.deleteAt is null")
    Optional<User> findByEmail(String email);

    @Query("SELECT u from User u where u.email = :email")
    Optional<User> findByEmailAndDeleteAt(String email);

    @Query("SELECT u from User u where u.refreshToken = :refreshToken and u.deleteAt is null")
    Optional<User> findByRefreshToken(String refreshToken);

    //    @Query("SELECT u from User u where u.socialType = :socialType and u.socialId = :socialId and u.deleteAt is null")
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    @Query("SELECT u FROM User u WHERE u.deleteAt < :time")
    List<User> findAllByDeleteAtBeforeTime(LocalDate time);

    boolean existsByEmail(String email);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE User u WHERE u.deleteAt < :time")
    void deleteAllByDeleteAtBeforeTime(LocalDate time);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE User u WHERE u.id in :userIds ")
    void deleteAllIdsIn(List<Long> userIds);

}
