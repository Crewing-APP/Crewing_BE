package com.crewing.review.repository;

import com.crewing.club.entity.Club;
import com.crewing.review.entity.ReviewAccess;
import com.crewing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewAccessRepository extends JpaRepository<ReviewAccess, Long> {
    Boolean existsByUserAndClub(User user, Club club);
}
