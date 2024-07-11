package com.crewing.club.service;

import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.dto.ClubListInfoResponse;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.entity.Category;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.club.repository.ClubRepository;
import com.crewing.common.error.club.ClubNotFoundException;
import com.crewing.common.error.user.UserAccessDeniedException;
import com.crewing.common.util.PaginationUtils;
import com.crewing.file.entity.ClubFile;
import com.crewing.member.entity.Member;
import com.crewing.member.repository.MemberRepository;
import com.crewing.review.repository.ReviewRepository;
import com.crewing.user.entity.Interest;
import com.crewing.user.entity.Role;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClubReadServiceImpl implements ClubReadService{
    private final ClubRepository clubRepository;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    // 동아리 상세 정보 조회
    @Override
    @Transactional
    public ClubInfoResponse getClubInfo(Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        List<ClubFile> clubFileList = club.getClubFileList();
        List<ClubFile.ImageInfo> imageInfoList = clubFileList.stream().map(ClubFile::toDto).toList();
        return  ClubInfoResponse.builder().
                    name(club.getName()).
                    clubId(club.getClubId()).
                    oneLiner(club.getOneLiner()).
                    introduction(club.getIntroduction()).
                    reviewAvg(reviewRepository.findAverageRateByClubId(club).orElse(0f)).
                    reviewNum(club.getReviewList().size()).
                    profile(club.getProfile()).
                    category(club.getCategory()).
                    application(club.getApplication()).
                    images(imageInfoList).
                    status(club.getStatus()).
                    isRecruit(club.getIsRecruit()).
                    isOnlyStudent(club.getIsOnlyStudent()).
                    docDeadLine(club.getDocDeadLine()).
                    docResultDate(club.getDocResultDate()).
                    interviewStartDate(club.getInterviewStartDate()).
                    interviewEndDate(club.getInterviewEndDate()).
                    finalResultDate(club.getFinalResultDate()).
                    build();
    }

    // 모든 동아리 정보 조회
    @Override
    @Transactional
    public ClubListResponse getAllClubInfo(Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByStatus(Status.ACCEPT,pageRequest);
        Page<ClubListInfoResponse> clubInfoPages = clubPage.map(this::toClubListInfoResponse);

        return getClubListResponse(clubInfoPages);
    }

    // 카테고리별 동아리 정보 조회
    @Override
    @Transactional
    public ClubListResponse getAllFilterClubInfo(Pageable pageable,int category) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByCategoryAndStatus(category, Status.ACCEPT, pageRequest);
        Page<ClubListInfoResponse> clubInfoPages = clubPage.map(this::toClubListInfoResponse);

        return getClubListResponse(clubInfoPages);
    }

    // 검색어를 통한 동아리 정보 조회
    @Override
    @Transactional
    public ClubListResponse getAllSearchClubInfo(Pageable pageable,String search, int category) {
        String keyword = search.replaceAll("\\s", "");
        Page<Club> clubPage = null;
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        if(category==-1)
            clubPage = clubRepository.findAllByKeywordAndStatus(keyword,Status.ACCEPT,pageRequest);
        else {
            clubPage = clubRepository.findAllByKeywordAndStatusAndCategory(keyword, Status.ACCEPT, pageRequest, category);
        }
        Page<ClubListInfoResponse> clubInfoPages = clubPage.map(this::toClubListInfoResponse);
        return getClubListResponse(clubInfoPages);
    }

    @Override
    @Transactional
    public ClubListResponse getAllStatusClubInfo(Pageable pageable,String status, User user) {
        if(!user.getRole().equals(Role.ADMIN)){
            throw new UserAccessDeniedException();
        }
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByStatus(Status.valueOf(status), pageRequest);
        Page<ClubListInfoResponse> clubInfoPages = clubPage.map(this::toClubListInfoResponse);

        return getClubListResponse(clubInfoPages);
    }

    @Override
    @Transactional
    public ClubListResponse getAllMyClubInfo(Pageable pageable, User user) {
        List<Member> memberList = memberRepository.findAllByUser(user);
        List<Long> clubIds = new ArrayList<>();
        for(Member member : memberList){
            clubIds.add(member.getClub().getClubId());
        }
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByClubIdIn(clubIds, pageRequest);
        Page<ClubListInfoResponse> clubInfoPages = clubPage.map(this::toClubListInfoResponse);

        return getClubListResponse(clubInfoPages);
    }

    @Override
    @Transactional
    public ClubListResponse getAllRecommendedClubInfo(Pageable pageable, String search, User user) {
        User loginedUser = userRepository.findById(user.getId()).get();
        List<Interest> interestList = loginedUser.getInterests();
        List<Integer> categories = new ArrayList<>();
        for(Interest interest : interestList){
            categories.add(setCategory(interest.getInterest()));
        }

        List<ClubListInfoResponse> clubList = null;
        if(search == null || search.isEmpty()){ // 전체 목록 조회
            clubList = clubRepository.findAllClubsWithAverageRating(categories,Status.ACCEPT,user.getBirth());
        }
        else{
            String keyword = search.replaceAll("\\s", "");
            clubList = clubRepository.findAllClubsWithAverageRatingByKeyword(categories,Status.ACCEPT,user.getBirth(),keyword);
        }
        Page<ClubListInfoResponse> clubsPage = PaginationUtils.listToPage(clubList, pageable);
        return getClubListResponse(clubsPage);
    }

    private static ClubListResponse getClubListResponse(Page<ClubListInfoResponse> clubInfoPages) {
        return ClubListResponse.builder()
                .pageNum(clubInfoPages.getNumber())
                .pageSize(clubInfoPages.getSize())
                .totalCnt(clubInfoPages.getTotalElements())
                .clubs(clubInfoPages.getContent())
                .build();
    }

    private ClubListInfoResponse toClubListInfoResponse(Club club) {
        return ClubListInfoResponse.builder().
                name(club.getName()).
                clubId(club.getClubId()).
                oneLiner(club.getOneLiner()).
                reviewAvg(reviewRepository.findAverageRateByClubId(club).orElse(0f)).
                reviewNum(club.getReviewList().size()).
                profile(club.getProfile()).
                category(club.getCategory()).
                status(club.getStatus()).
                isRecruit(club.getIsRecruit()).
                isOnlyStudent(club.getIsOnlyStudent()).
                docDeadLine(club.getDocDeadLine()).
                docResultDate(club.getDocResultDate()).
                interviewStartDate(club.getInterviewStartDate()).
                interviewEndDate(club.getInterviewEndDate()).
                finalResultDate(club.getFinalResultDate()).
                build();

    }

    private int setCategory(String interest){
        switch (interest){
            case "IT/데이터" : return 0;
            case "사진/촬영" : return 1;
            case "인문학/독서" : return 2;
            case "여행" : return 3;
            case "스포츠" : return 4;
            case "문화/예술" : return 5;
            case "댄스" : return 6;
            case "음악/악기" : return 7;
            case "봉사활동" : return 8;
            case "기타" : return 9;
            default: return 10;
        }
    }
}
