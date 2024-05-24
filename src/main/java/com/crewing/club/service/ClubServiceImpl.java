package com.crewing.club.service;

import com.crewing.club.dto.ClubCreateRequest;
import com.crewing.club.dto.ClubUpdateRequest;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.club.repository.ClubRepository;
import com.crewing.common.error.BusinessException;
import com.crewing.common.error.EntityNotFoundException;
import com.crewing.common.error.ErrorCode;
import com.crewing.common.error.club.ClubAccessDeniedException;
import com.crewing.common.error.club.ClubNotFoundException;
import com.crewing.member.entity.Member;
import com.crewing.member.entity.Role;
import com.crewing.member.repository.MemberRepository;
import com.crewing.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClubServiceImpl implements ClubService{
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Club createClub(ClubCreateRequest clubCreateRequest, User user, String profile){
        // 동아리 생성
        Club result = clubRepository.save(Club.builder().
                name(clubCreateRequest.getName()).
                oneLiner(clubCreateRequest.getOneLiner()).
                introduction(clubCreateRequest.getIntroduction()).
                profile(profile).
                application(clubCreateRequest.getApplication()).
                status(Status.UNDEFINED).
                build());
        // 매니저 임명
        memberRepository.save(Member.builder().
                user(user).
                club(result).
                role(Role.MANAGER).
                build());

        return result;

    }

    @Override
    @Transactional
    public Club updateClub(Long clubId, ClubUpdateRequest clubUpdateRequest, User user, String profile){
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        Member member = memberRepository.findByClubAndUserAndRole(club,user, Role.MANAGER).orElseThrow(ClubAccessDeniedException::new);

        Club newClub = club.toBuilder().
                name(clubUpdateRequest.getName()).
                oneLiner(clubUpdateRequest.getOneLiner()).
                introduction(clubUpdateRequest.getIntroduction()).
                application(clubUpdateRequest.getApplication()).
                profile(profile).
                build();

        return clubRepository.save(newClub);
    }

    @Override
    @Transactional
    public void deleteClub(Long clubId, User user){
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        Member member = memberRepository.findByClubAndUserAndRole(club,user, Role.MANAGER).orElseThrow(ClubAccessDeniedException::new);

        clubRepository.delete(club);
    }

    @Override
    @Transactional
    public Club changeStatus(Long clubId, User user, Status status) {
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        Club newClub = club.toBuilder().
                status(status).
                build();
        return clubRepository.save(newClub);
    }

}
