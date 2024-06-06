package com.crewing.member.repository;

import com.crewing.club.entity.Club;
import com.crewing.member.entity.Member;
import com.crewing.member.entity.Role;
import com.crewing.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByMemberIdAndRole(Long memberId, Role role);
    Optional<Member> findByClubAndUserAndRole(Club club, User user, Role role);
    Optional<Member> findByClubAndUser(Club club, User user);
    Page<Member> findByClub_ClubId(Long clubId, Pageable pageable);
    List<Member> findAllByRole(Role role);
    List<Member> findAllByUser(User user);
}
