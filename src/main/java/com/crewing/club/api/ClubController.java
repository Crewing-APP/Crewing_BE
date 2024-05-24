package com.crewing.club.api;

import com.crewing.club.dto.ClubCreateRequest;
import com.crewing.club.dto.ClubUpdateRequest;
import com.crewing.club.entity.Club;
import com.crewing.club.service.ClubServiceImpl;
import com.crewing.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/club")
public class ClubController {
    private final ClubServiceImpl clubServiceImpl;

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
}
