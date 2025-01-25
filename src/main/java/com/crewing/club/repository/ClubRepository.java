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

    @Query(value = "select " +
            "new com.crewing.club.dto.ClubListInfoResponse(c.clubId, c.name, c.oneLiner, AVG(r.rate), COUNT(r), '', c.profile, " +
            "c.category, c.status, c.isRecruit, c.isOnlyStudent, c.docDeadLine, c.docResultDate, c.interviewStartDate, " +
            "c.interviewEndDate, c.finalResultDate) " +
            "FROM Club c LEFT JOIN c.reviewList r LEFT JOIN c.memberList m LEFT JOIN m.user u " +
            "WHERE c.isRecruit = true AND c.status = :clubStatus AND c.category IN :categories " +
            "GROUP BY c.clubId " +
            "HAVING AVG(r.rate) >= 2.0 " +  // 리뷰 평점 2.0 이상만 선택
            "ORDER BY AVG(r.rate) DESC, " +
            "SUM(CASE WHEN u.birth = :birth THEN 1 ELSE 0 END) DESC, " +
            "SUM(CASE WHEN u.gender = :gender THEN 1 ELSE 0 END) DESC")
    List<ClubListInfoResponse> findAllClubsWithAverageRatingLegacy(@Param("categories") List<Integer> categories,
                                                                   @Param("clubStatus") Status clubStatus,
                                                                   @Param("birth") String birth,
                                                                   @Param("gender") String gender);

    @Query(value = "select " +
            "new com.crewing.club.dto.ClubListInfoResponse(c.clubId, c.name, c.oneLiner, AVG(r.rate), COUNT(r), '', c.profile, " +
            "c.category, c.status, c.isRecruit, c.isOnlyStudent, c.docDeadLine, c.docResultDate, c.interviewStartDate, " +
            "c.interviewEndDate, c.finalResultDate) " +
            "FROM Club c LEFT JOIN c.reviewList r LEFT JOIN c.memberList m LEFT JOIN m.user u " +
            "WHERE c.isRecruit = true AND c.status = :clubStatus AND c.category IN :categories " +
            "AND (c.name LIKE %:keyword%) " +  // 키워드 검색 추가
            "GROUP BY c.clubId " +
            "HAVING AVG(r.rate) >= 2.0 " +  // 리뷰 평점 2.0 이상만 선택
            "ORDER BY AVG(r.rate) DESC, " +
            "SUM(CASE WHEN u.birth = :birth THEN 1 ELSE 0 END) DESC, " +
            "SUM(CASE WHEN u.gender = :gender THEN 1 ELSE 0 END) DESC")
    List<ClubListInfoResponse> findAllClubsWithAverageRatingAndKeywordLegacy(@Param("categories") List<Integer> categories,
                                                                       @Param("clubStatus") Status clubStatus,
                                                                       @Param("birth") String birth,
                                                                       @Param("gender") String gender,
                                                                       @Param("keyword") String keyword);


}
