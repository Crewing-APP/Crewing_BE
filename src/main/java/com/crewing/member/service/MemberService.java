package com.crewing.member.service;

import com.crewing.member.dto.MemberCreateRequest;
import com.crewing.member.dto.MemberInfoResponse;
import com.crewing.member.dto.MemberListResponse;
import com.crewing.user.entity.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    public MemberInfoResponse createMember(MemberCreateRequest memberCreateRequest, User manager);
    public void deleteMember(Long memberId, User manager);
    public MemberInfoResponse assignManager(User manager, Long memberId);
    public MemberInfoResponse deleteManager(Long managerId, User manager);
    public MemberListResponse getAllMemberInfo(Pageable pageable, Long clubId, User manager);
}
