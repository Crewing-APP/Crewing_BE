package com.crewing.club.repository;

import com.crewing.club.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club,Long> {
    List<Club> findAllByCategory(int category);

}
