package com.crewing.club.api;

import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.club.dto.ClubChangeStatusRequest;
import com.crewing.club.dto.ClubCreateRequest;
import com.crewing.club.dto.ClubCreateResponse;
import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.dto.ClubUpdateRequest;
import com.crewing.club.service.ClubReadServiceImpl;
import com.crewing.club.service.ClubServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/club")
public class ClubController {
    private final ClubServiceImpl clubServiceImpl;
    private final ClubReadServiceImpl clubReadService;
    private final ClubReadServiceImpl clubReadServiceImpl;

    @Operation(summary = "동아리 생성", description = "최초 동아리 생성, 생성자는 매니저 자동 임명")
    @PostMapping("/create")
    public ResponseEntity<ClubCreateResponse> create(@RequestPart("content") ClubCreateRequest clubCreateRequest,
                                                     @RequestPart(value = "profile", required = false) MultipartFile profile,
                                                     @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails)
            throws IOException {
        ClubCreateResponse response = clubServiceImpl.createClub(clubCreateRequest, principalDetails.getUser(), profile,
                images);
        log.info("[CLUB] create club_id={},user_id={},club_name={}", response.getClubId(),
                principalDetails.getUser().getId(), response.getName());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "동아리 수정", description = "동아리 정보 수정, 매니저만 가능")
    @PatchMapping("/edit/{clubId}")
    public ResponseEntity<ClubCreateResponse> update(@PathVariable Long clubId,
                                                     @RequestPart(value = "content") ClubUpdateRequest clubUpdateRequest,
                                                     @RequestPart(value = "profile", required = false) MultipartFile profile,
                                                     @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                     @RequestPart(value = "deletedImages", required = false) List<String> deletedImages,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails)
            throws IOException {
        ClubCreateResponse response = clubServiceImpl.updateClub(clubId, clubUpdateRequest, principalDetails.getUser(),
                profile, images, deletedImages);
        log.info("[CLUB] update club_id={},user_id={},club_name={}", response.getClubId(),
                principalDetails.getUser().getId(), response.getName());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "동아리 삭제", description = "동아리 삭제, 매니저만 가능")
    @DeleteMapping("/delete/{clubId}")
    public ResponseEntity<String> delete(@PathVariable Long clubId,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        clubServiceImpl.deleteClub(clubId, principalDetails.getUser());
        return ResponseEntity.ok().body("Delete successful!");
    }

    @Operation(summary = "동아리 상세 조회", description = "동아리 상세 정보 조회")
    @GetMapping("/{clubId}")
    public ResponseEntity<ClubInfoResponse> getClubInfo(@PathVariable Long clubId) {
        ClubInfoResponse clubInfo = clubReadService.getClubInfo(clubId);
        return ResponseEntity.ok().body(clubInfo);
    }

    @Operation(summary = "동아리 목록 조회", description = "동아리 목록 조회, 페이징으로 조회 가능")
    @GetMapping("/clubs")
    public ResponseEntity<ClubListResponse> getAllClub(@PageableDefault(size = 10) Pageable pageable) {
        ClubListResponse clubList = clubReadServiceImpl.getAllClubInfo(pageable);
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "카테고리별 동아리 목록 조회", description = "카테고리별 동아리 목록 조회, 페이징으로 조회 가능")
    @GetMapping("/clubs/category")
    public ResponseEntity<ClubListResponse> getAllClubByCategory(@PageableDefault(size = 10) Pageable pageable,
                                                                 @RequestParam int category) {
        ClubListResponse clubList = clubReadServiceImpl.getAllFilterClubInfo(pageable, category);
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "검색어별 동아리 목록 조회", description = "제목 검색으로 동아리 목록 조회, 페이징으로 조회 가능")
    @GetMapping("/clubs/search")
    public ResponseEntity<ClubListResponse> getAllClubBySearch(@PageableDefault(size = 10) Pageable pageable,
                                                               @RequestParam String search) {
        ClubListResponse clubList = clubReadServiceImpl.getAllSearchClubInfo(pageable, search);
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "수락 신청 동아리 목록 조회", description = "WAIT 상태인 동아리 목록 조회, 페이징으로 조회 가능, 관리자만 조회")
    @GetMapping("/clubs/status")
    public ResponseEntity<ClubListResponse> getAllWaitClub(@PageableDefault(size = 10) Pageable pageable,
                                                           @RequestParam String status,
                                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ClubListResponse clubList = clubReadServiceImpl.getAllStatusClubInfo(pageable, status,
                principalDetails.getUser());
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "동아리 상태 변경", description = "동아리 상태 변경(WAIT,HOLD,RETURN,ACCEPT) , 관리자만 가능")
    @PatchMapping("/status")
    public ResponseEntity<ClubCreateResponse> changeStatus(@RequestBody ClubChangeStatusRequest clubChangeStatusRequest,
                                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ClubCreateResponse clubInfo = clubServiceImpl.changeStatus(clubChangeStatusRequest, principalDetails.getUser());
        return ResponseEntity.ok().body(clubInfo);
    }


}
