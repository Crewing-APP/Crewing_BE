package com.crewing.member.api;

import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.member.dto.MemberCreateRequest;
import com.crewing.member.dto.MemberInfoResponse;
import com.crewing.member.dto.MemberListResponse;
import com.crewing.member.service.MemberService;
import com.crewing.member.service.MemberServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "member", description = "동아리 회원 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "동아리 회원 생성",description = "회원 임명, 동아리 매니저만 가능")
    @PostMapping("/create")
    public ResponseEntity<MemberInfoResponse> create(@RequestBody MemberCreateRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        MemberInfoResponse memberInfoResponse = memberService.createMember(request,principalDetails.getUser());
        return ResponseEntity.ok().body(memberInfoResponse);
    }

    @Operation(summary = "동아리 회원 삭제",description = "회원 탈퇴, 동아리 매니저만 가능")
    @DeleteMapping("/{memberId}")
    @Parameter(name = "memberId", description = "회원 아이디", required = true)
    public ResponseEntity<String> delete(@PathVariable Long memberId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        memberService.deleteMember(memberId,principalDetails.getUser());
        return ResponseEntity.ok().body("Delete successful");
    }

    @Operation(summary = "동아리 매니저 지정",description = "동아리 일반 회원을 매니저로 임명, 동아리 매니저만 가능")
    @PatchMapping("/manager/{memberId}")
    @Parameter(name = "memberId",description = "회원 아이디",required = true)
    public ResponseEntity<MemberInfoResponse> assignManager(@PathVariable Long memberId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        MemberInfoResponse memberInfoResponse = memberService.assignManager(principalDetails.getUser(), memberId);
        return ResponseEntity.ok().body(memberInfoResponse);
    }

    @Operation(summary = "동아리 매니저 삭제",description = "동아리 매니저를 일반 회원으로 강등, 동아리 매니저만 가능")
    @GetMapping("/manager/{memberId}")
    @Parameter(name = "memberId",description = "회원 아이디",required = true)
    public ResponseEntity<MemberInfoResponse> deleteManager(@PathVariable Long memberId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        MemberInfoResponse memberInfoResponse = memberService.deleteManager(memberId, principalDetails.getUser());
        return ResponseEntity.ok().body(memberInfoResponse);
    }

    @Operation(summary = "동아리 회원 목록 조회",description = "동아리 회원 목록 조회, 동아리 매니저만 가능")
    @GetMapping("/members/{clubId}")
    @Parameter(name = "clubId", description = "동아리 아이디", required = true)
    public ResponseEntity<MemberListResponse> getAllMembers(@PageableDefault(size = 10) Pageable pageable, @PathVariable Long clubId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        MemberListResponse memberListResponse = memberService.getAllMemberInfo(pageable, clubId, principalDetails.getUser());
        return ResponseEntity.ok().body(memberListResponse);
    }
}
