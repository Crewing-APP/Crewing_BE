package com.crewing.member.service;

import com.crewing.club.entity.Club;
import com.crewing.club.repository.ClubRepository;
import com.crewing.common.error.club.ClubNotFoundException;
import com.crewing.common.error.member.*;
import com.crewing.common.error.user.UserNotFoundException;
import com.crewing.member.dto.MemberCreateRequest;
import com.crewing.member.dto.MemberInfoResponse;
import com.crewing.member.dto.MemberListResponse;
import com.crewing.member.entity.Member;
import com.crewing.member.entity.Role;
import com.crewing.member.repository.MemberRepository;
import com.crewing.user.entity.User;
import com.crewing.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public MemberInfoResponse createMember(MemberCreateRequest memberCreateRequest, User manager) {
        Club club = checking(memberCreateRequest.getClubId(),manager);
        // user 조회
        User user = userRepository.findById(memberCreateRequest.getUserId()).orElseThrow(UserNotFoundException::new);
        // 이미 멤버일 경우
        if(memberRepository.findByClubAndUser(club,user).isPresent()){
            throw new MemberAlreadyExistsException();
        }
        Member member = memberRepository.save(Member.builder()
                .club(club)
                .user(user)
                .role(Role.MEMBER)
                .build());

        return member.toMemberInfoResponse();
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId, User manager) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        memberRepository.findByClubAndUserAndRole(member.getClub(),manager,Role.MANAGER).orElseThrow(MemberAccessDeniedException::new);
        memberRepository.deleteById(memberId);
    }

    @Override
    @Transactional
    public MemberInfoResponse assignManager(User manager, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        if(member.getRole().equals(Role.MANAGER)) // 이미 매니저인 경우
            throw new MemberFailedAssignManagerException();
        memberRepository.findByClubAndUserAndRole(member.getClub(),manager,Role.MANAGER).orElseThrow(MemberAccessDeniedException::new);
        // member -> manager
        return memberRepository.save(member.toBuilder().role(Role.MANAGER).build()).toMemberInfoResponse();

    }

    @Override
    @Transactional
    public MemberInfoResponse deleteManager(Long managerId, User manager) {
        Member member = memberRepository.findByMemberIdAndRole(managerId,Role.MANAGER).orElseThrow(MemberNotFoundException::new);
        memberRepository.findByClubAndUserAndRole(member.getClub(),manager,Role.MANAGER).orElseThrow(MemberAccessDeniedException::new);
        // 매니저가 1명 이하면 매니저 삭제 불가
        if(memberRepository.findAllByRole(Role.MANAGER).size() <= 1){
            throw new MemberFailedDeleteManagerException();
        }

        return memberRepository.save(member.toBuilder().role(Role.MEMBER).build()).toMemberInfoResponse();
    }

    @Override
    @Transactional
    public MemberListResponse getAllMemberInfo(Pageable pageable, Long clubId, User manager) {
        checking(clubId,manager);

        Sort sort = Sort.by(Sort.Order.desc("role").with(Sort.NullHandling.NULLS_LAST));
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort.and(Sort.by(Sort.Direction.ASC,"memberId")));
        Page<Member> memberPage = memberRepository.findByClub_ClubId(clubId, pageRequest);
        Page<MemberInfoResponse> responsePage = memberPage.map(Member::toMemberInfoResponse);
        return MemberListResponse.builder()
                .pageNum(memberPage.getNumber())
                .pageSize(memberPage.getSize())
                .totalCnt(memberPage.getTotalPages())
                .members(responsePage.getContent())
                .build();
    }

    // 존재하는 동아리인지, 로그인한 유저가 매니저가 맞는지 검사
    public Club checking(Long clubId, User manager){
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        memberRepository.findByClubAndUserAndRole(club,manager, Role.MANAGER).orElseThrow(MemberAccessDeniedException::new);
        return club;
    }
}
