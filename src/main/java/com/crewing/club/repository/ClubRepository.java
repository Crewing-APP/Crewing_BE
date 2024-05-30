package com.crewing.club.repository;

import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club,Long> {
    Page<Club> findAllByStatus(Status status, Pageable pageable);
    Page<Club> findAllByCategoryAndStatus(int category, Status status,Pageable pageable);
    Page<Club> findAllByNameContainingAndStatus(String search, Status status, Pageable pageable);
}
