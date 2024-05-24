package com.crewing.member.repository;

import com.crewing.club.entity.Club;
import com.crewing.member.entity.Member;
import com.crewing.member.entity.Role;
import com.crewing.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    Optional<Member> findByClubAndUser(Club club, User user);
    Optional<Member> findByClubAndUserAndRole(Club club, User user, Role role);

}
