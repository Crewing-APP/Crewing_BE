package com.crewing.club.repository;

import com.crewing.club.entity.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club,Long> {
    Page<Club> findAllByCategory(int category, Pageable pageable);
    Page<Club> findAllByNameContaining(String search, Pageable pageable);
}
