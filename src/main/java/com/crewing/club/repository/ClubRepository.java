package com.crewing.club.repository;

import com.crewing.club.dto.ClubListInfoResponse;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club,Long>, ClubRepositoryCustom{
    Page<Club> findAllByStatus(Status status, Pageable pageable);
    int countAllByStatus(Status status);
    Page<Club> findAllByCategoryAndStatus(int category, Status status,Pageable pageable);

    @Query("SELECT c FROM Club c WHERE REPLACE(c.name, ' ', '') LIKE %:keyword% AND c.status = :status AND c.category = :category")
    Page<Club> findAllByKeywordAndStatusAndCategory(@Param("keyword") String keyword, @Param("status") Status status, Pageable pageable,@Param("category") int category);

    @Query("SELECT c FROM Club c WHERE REPLACE(c.name, ' ', '') LIKE %:keyword% AND c.status = :status")
    Page<Club> findAllByKeywordAndStatus(@Param("keyword") String keyword, @Param("status") Status status, Pageable pageable);

    Page<Club> findAllByClubIdIn(List<Long> clubIds, Pageable pageable);
}
