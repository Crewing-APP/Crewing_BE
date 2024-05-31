package com.crewing.file.repository;

import com.crewing.club.entity.Club;
import com.crewing.file.entity.ClubFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubFileRepository extends JpaRepository<ClubFile,Long> {
    List<ClubFile> findByClub(Club club);
    void deleteAllByImageUrl(String imageUrl);
}
