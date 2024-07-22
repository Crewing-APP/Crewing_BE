package com.crewing.user.repository;

import com.crewing.user.entity.Interest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface InterestRepository extends JpaRepository<Interest, Long> {
    void deleteAllByUserId(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Interest i SET i.user = null WHERE i.user.id in :userIds")
    void updateInterestUserToNullByUserIdsIn(List<Long> userIds);
}
