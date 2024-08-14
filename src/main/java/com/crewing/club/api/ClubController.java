package com.crewing.club.api;

import com.crewing.club.entity.Status;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.club.dto.ClubChangeStatusRequest;
import com.crewing.club.dto.ClubCreateRequest;
import com.crewing.club.dto.ClubCreateResponse;
import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.dto.ClubUpdateRequest;
import com.crewing.club.service.ClubReadService;
import com.crewing.club.service.ClubService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import java.io.IOException;
import java.util.List;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
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


@Tag(name = "club", description = "동아리 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/club")
public class ClubController {
    private final ClubService clubService;
    private final ClubReadService clubReadService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "동아리 생성", description = "최초 동아리 생성, 생성자는 매니저 자동 임명")
    @PostMapping(value = "/create",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ClubCreateResponse> create(@RequestPart("content") ClubCreateRequest clubCreateRequest,
                                                     @RequestPart(value = "profile", required = false) MultipartFile profile,
                                                     @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails)
            throws IOException {
        ClubCreateResponse response = clubService.createClub(clubCreateRequest, principalDetails.getUser(), profile,
                images);
        log.info("[CLUB] create club_id={},user_id={},club_name={}", response.getClubId(),
                principalDetails.getUser().getId(), response.getName());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "동아리 수정", description = "동아리 정보 수정, 매니저만 가능")
    @PatchMapping(value = "/edit/{clubId}",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,MediaType.APPLICATION_JSON_VALUE})
    @Parameter(name = "clubId", description = "동아리 아이디")
    public ResponseEntity<ClubCreateResponse> update(@PathVariable Long clubId,
                                                     @RequestPart(value = "content") ClubUpdateRequest clubUpdateRequest,
                                                     @RequestPart(value = "profile", required = false) MultipartFile profile,
                                                     @RequestPart(value = "images", required = false) List<MultipartFile> images,
                                                     @RequestPart(value = "deletedImages", required = false) ClubUpdateRequest.DeletedImages deletedImages,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails)
            throws IOException {
        ClubCreateResponse response = clubService.updateClub(clubId, clubUpdateRequest, principalDetails.getUser(),
                profile, images, deletedImages);
        log.info("[CLUB] update club_id={},user_id={},club_name={}", response.getClubId(),
                principalDetails.getUser().getId(), response.getName());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "동아리 삭제", description = "동아리 삭제, 매니저만 가능")
    @DeleteMapping("/delete/{clubId}")
    @Parameter(name = "clubId", description = "동아리 아이디",required = true)
    public ResponseEntity<String> delete(@PathVariable Long clubId, @AuthenticationPrincipal PrincipalDetails principalDetails){
        clubService.deleteClub(clubId,principalDetails.getUser());
        return ResponseEntity.ok().body("Delete successful!");
    }

    @Operation(summary = "동아리 상세 조회", description = "동아리 상세 정보 조회")
    @GetMapping("/{clubId}")
    @Parameter(name = "clubId", description = "동아리 아이디",required = true)
    public ResponseEntity<ClubInfoResponse> getClubInfo(@PathVariable Long clubId, @AuthenticationPrincipal PrincipalDetails principalDetails){
        ClubInfoResponse clubInfo = clubReadService.getClubInfo(clubId,principalDetails.getUser());
        return ResponseEntity.ok().body(clubInfo);
    }

    @Operation(summary = "나의 동아리 목록 조회", description = "나의 활동중인 동아리 목록 조회, 페이징으로 조회 가능")
    @GetMapping("/my")
    public ResponseEntity<ClubListResponse> getAllMyClub(@PageableDefault(size = 10) Pageable pageable, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ClubListResponse clubList = clubReadService.getAllMyClubInfo(pageable, principalDetails.getUser());
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "추천 동아리 목록 조회", description = "추천 동아리 목록 조회, search가 비어있을 경우 전체 목록 조회, 페이징으로 조회 가능")
    @GetMapping("/clubs/recommend")
    public ResponseEntity<ClubListResponse> getAllRecommendClub(@PageableDefault(size = 10) Pageable pageable, @RequestParam String search, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ClubListResponse clubList = clubReadService.getAllRecommendedClubInfo(pageable, search, principalDetails.getUser());
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "동아리 목록 조회", description = "동아리 목록 조회, 페이징으로 조회 가능")
    @GetMapping("/clubs")
    public ResponseEntity<ClubListResponse> getAllClub(@PageableDefault(size = 10) Pageable pageable) {
        ClubListResponse clubList = clubReadService.getAllClubInfo(pageable);
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "카테고리별 동아리 목록 조회", description = "카테고리별 동아리 목록 조회, 페이징으로 조회 가능")
    @GetMapping("/clubs/category")
    @Parameter(name = "category", description = "동아리 카테고리", required = true)
    public ResponseEntity<ClubListResponse> getAllClubByCategory(@PageableDefault(size = 10) Pageable pageable,@RequestParam int category){
        ClubListResponse clubList = clubReadService.getAllFilterClubInfo(pageable,category);
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "검색어별 동아리 목록 조회", description = "제목 검색으로 동아리 목록 조회, category 값이 -1일 경우 전체 동아리에서 조회, 페이징으로 조회 가능")
    @GetMapping("/clubs/search")
    @Parameter(name = "search", description = "검색어", required = true)
    public ResponseEntity<ClubListResponse> getAllClubBySearch(@PageableDefault(size = 10) Pageable pageable, @RequestParam String search, @RequestParam int category){
        ClubListResponse clubList = clubReadService.getAllSearchClubInfo(pageable,search,category);
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "상태별 동아리 목록 조회", description = "상태별 동아리 목록 조회, 페이징으로 조회 가능, 관리자만 조회")
    @GetMapping("/clubs/status")
    @Parameter(name = "status", description = "동아리 상태", required = true)
    public ResponseEntity<ClubListResponse> getAllStatusClub(@PageableDefault(size = 10) Pageable pageable, @RequestParam Status status, @AuthenticationPrincipal PrincipalDetails principalDetails){
        ClubListResponse clubList = clubReadService.getAllStatusClubInfo(pageable,status,principalDetails.getUser());
        return ResponseEntity.ok().body(clubList);
    }

    @Operation(summary = "동아리 상태 변경", description = "동아리 상태 변경(WAIT,HOLD,RETURN,ACCEPT) , 관리자만 가능")
    @PatchMapping("/status")
    public ResponseEntity<ClubCreateResponse> changeStatus(@RequestBody ClubChangeStatusRequest clubChangeStatusRequest,
                                                           @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ClubCreateResponse clubInfo = clubService.changeStatus(clubChangeStatusRequest, principalDetails.getUser());
        return ResponseEntity.ok().body(clubInfo);
    }
}
