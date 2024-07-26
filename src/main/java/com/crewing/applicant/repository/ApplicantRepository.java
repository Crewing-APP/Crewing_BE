package com.crewing.applicant.repository;

import com.crewing.applicant.entity.Applicant;
import com.crewing.applicant.entity.Status;
import com.crewing.club.entity.Club;
import com.crewing.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    Page<Applicant> findAllByClub(Club club, Pageable pageable);

    Page<Applicant> findAllByClubAndStatus(Club club, Status status, Pageable pageable);

    Optional<Applicant> findByUserAndClub(User user, Club club);

    Optional<Applicant> findByUserAndClubAndStatus(User user, Club club, Status status);

    List<Applicant> findAllByApplicantIdIn(List<Long> applicantIds);

    void deleteByApplicantIdIn(List<Long> applicantIds);

    List<Applicant> findAllByUser(User user);

    void deleteAllByUserId(Long userId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE Applicant a WHERE a.user.id in :userIds")
    void deleteAllByUserIdsIn(List<Long> userIds);
}
