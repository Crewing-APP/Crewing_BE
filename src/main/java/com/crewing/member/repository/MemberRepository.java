package com.crewing.member.repository;

import com.crewing.club.entity.Club;
import com.crewing.member.entity.Member;
import com.crewing.member.entity.Role;
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
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberIdAndRole(Long memberId, Role role);

    Optional<Member> findByClubAndUserAndRole(Club club, User user, Role role);

    Optional<Member> findByClubAndUser(Club club, User user);

    Page<Member> findByClub_ClubId(Long clubId, Pageable pageable);

    List<Member> findAllByRole(Role role);

    List<Member> findAllByUser(User user);

    List<Member> findAllByClubAndRole(Club club, Role role);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE Member m WHERE m.user.id in :userIds")
    void deleteAllByUserIdsIn(List<Long> userIds);


    void deleteAllByUserId(Long userId);
}
