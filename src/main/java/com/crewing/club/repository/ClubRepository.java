package com.crewing.club.repository;

import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club,Long> {
    Page<Club> findAll(int category, Pageable pageable);
    Page<Club> findAllByCategory(int category, Pageable pageable);
    Page<Club> findAllByNameContaining(String search, Pageable pageable);
}
