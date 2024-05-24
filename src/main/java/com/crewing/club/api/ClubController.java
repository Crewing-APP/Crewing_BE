package com.crewing.club.api;

import com.crewing.club.dto.ClubCreateRequest;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.dto.ClubUpdateRequest;
import com.crewing.club.entity.Club;
import com.crewing.club.service.ClubReadServiceImpl;
import com.crewing.club.service.ClubServiceImpl;
import com.crewing.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/club")
public class ClubController {
    private final ClubServiceImpl clubServiceImpl;
    private final ClubReadServiceImpl clubReadService;
    private final ClubReadServiceImpl clubReadServiceImpl;

    @Operation(summary = "동아리 생성", description = "최초 동아리 생성, 생성자는 매니저 자동 임명")
    @PostMapping("/create")
    public ResponseEntity<Club> create(@RequestPart(value = "content") ClubCreateRequest clubCreateRequest, @RequestPart String profile, @AuthenticationPrincipal User user){
        Club club = clubServiceImpl.createClub(clubCreateRequest,user,profile);
        return ResponseEntity.ok().body(club);
    }

    @Operation(summary = "동아리 수정", description = "동아리 정보 수정, 매니저만 가능")
    @PatchMapping("/edit/{clubId}")
    public ResponseEntity<Club> update(@PathVariable Long clubId, @RequestPart(value = "content") ClubUpdateRequest clubUpdateRequest, @RequestPart String profile, @AuthenticationPrincipal User user){
        Club club = clubServiceImpl.updateClub(clubId,clubUpdateRequest,user,profile);
        return ResponseEntity.ok().body(club);
    }

    @Operation(summary = "동아리 삭제", description = "동아리 삭제, 매니저만 가능")
    @DeleteMapping("/delete/{clubId}")
    public ResponseEntity<String> delete(@PathVariable Long clubId, @AuthenticationPrincipal User user){
        clubServiceImpl.deleteClub(clubId,user);
        return ResponseEntity.ok().body("Delete successful!");
    }

    @Operation(summary = "동아리 목록 조회", description = "동아리 목록 조회, 페이징으로 조회 가능")
    @GetMapping("/clubs")
    public ResponseEntity<ClubListResponse> getAllClub(@PageableDefault(size = 10) Pageable pageable){
        ClubListResponse clubList = clubReadServiceImpl.getAllClubInfo(pageable);
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "카테고리별 동아리 목록 조회", description = "카테고리별 동아리 목록 조회, 페이징으로 조회 가능")
    @GetMapping("/clubs")
    public ResponseEntity<ClubListResponse> getAllClubByCategory(@PageableDefault(size = 10) Pageable pageable,@RequestParam int category){
        ClubListResponse clubList = clubReadServiceImpl.getAllFilterClubInfo(pageable,category);
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "검색어별 동아리 목록 조회", description = "제목 검색으로 동아리 목록 조회, 페이징으로 조회 가능")
    @GetMapping("/clubs")
    public ResponseEntity<ClubListResponse> getAllClubBySearch(@PageableDefault(size = 10) Pageable pageable, @RequestParam String search){
        ClubListResponse clubList = clubReadServiceImpl.getAllSearchClubInfo(pageable,search);
        return ResponseEntity.ok().body(clubList);
    }




}
