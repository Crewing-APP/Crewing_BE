package com.crewing.review.repository;

import com.crewing.club.entity.Club;
import com.crewing.review.entity.Review;
import com.crewing.user.entity.User;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByClub(Pageable pageable, Club club);
    Page<Review> findAllByUser(Pageable pageable,User user);

    @Query("SELECT AVG(r.rate) FROM Review r, Club c WHERE r.club = :club")
    Optional<Float> findAverageRateByClubId(@Param("club") Club club);

    List<Review> findAllByClubAndRate(Club club, int rate);
    Optional<Review> findByClubAndUser(Club club, User user);
}
