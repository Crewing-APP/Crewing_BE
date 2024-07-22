package com.crewing.review.repository;

import com.crewing.club.entity.Club;
import com.crewing.review.entity.Review;
import com.crewing.user.entity.User;
import feign.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByClub(Pageable pageable, Club club);

    @Query("SELECT AVG(r.rate) FROM Review r, Club c WHERE r.club = :club")
    Optional<Float> findAverageRateByClubId(@Param("club") Club club);

    List<Review> findAllByClubAndRate(Club club, int rate);

    Optional<Review> findByClubAndUser(Club club, User user);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Review r SET r.user = null WHERE r.user.id in :userIds")
    void updateReviewUserToNullByUserIdsIn(List<Long> userIds);
}
