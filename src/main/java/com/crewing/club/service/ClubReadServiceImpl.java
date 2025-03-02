package com.crewing.club.service;

import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.dto.ClubListInfoResponse;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.club.repository.ClubRepository;
import com.crewing.club.repository.ClubRepositoryCustom;
import com.crewing.common.error.club.ClubNotFoundException;
import com.crewing.common.error.user.UserAccessDeniedException;
import com.crewing.common.util.RedisUtil;
import com.crewing.file.entity.ClubFile;
import com.crewing.member.entity.Member;
import com.crewing.member.repository.MemberRepository;
import com.crewing.review.entity.Review;
import com.crewing.review.repository.ReviewAccessRepository;
import com.crewing.review.repository.ReviewRepository;
import com.crewing.user.entity.Interest;
import com.crewing.user.entity.Role;
import com.crewing.user.entity.User;
import com.crewing.user.repository.InterestRepository;
import com.crewing.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClubReadServiceImpl implements ClubReadService{
    private final ClubRepository clubRepository;
    private final ClubRepositoryCustom clubRepositoryCustomImpl;
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final ReviewAccessRepository reviewAccessRepository;
    private final RedisUtil redisUtil;
    private static final long EXPIRE = 3600L;
    private final InterestRepository interestRepository;

    // 동아리 상세 정보 조회
    @Override
    @Transactional
    public ClubInfoResponse getClubInfo(Long clubId, User user) {
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        List<ClubFile> clubFileList = club.getClubFileList();
        List<ClubFile.ImageInfo> imageInfoList = clubFileList.stream().map(ClubFile::toDto).toList();
        return ClubInfoResponse.builder().
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
                // 회원이거나 포인트 구매 이력이 있으면 리뷰 열람 가능
                    isReviewAccess(memberRepository.existsByUserAndClub(user,club)||reviewAccessRepository.existsByUserAndClub(user,club)).
                    build();
    }

    // 모든 동아리 정보 조회
    @Override
    @Transactional
    public ClubListResponse getAllClubInfo(Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByStatus(Status.ACCEPT,pageRequest);
        Page<ClubListInfoResponse> clubInfoPages = clubPage.map(club ->
                ClubListInfoResponse.toClubListInfoResponse(club,reviewRepository.findAverageRateByClubId(club).orElse(0f)));

        return getClubListResponse(clubInfoPages);
    }

    // 카테고리별 동아리 정보 조회
    @Override
    @Transactional
    public ClubListResponse getAllFilterClubInfo(Pageable pageable,int category) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByCategoryAndStatus(category, Status.ACCEPT, pageRequest);
        Page<ClubListInfoResponse> clubInfoPages = clubPage.map(club ->
                ClubListInfoResponse.toClubListInfoResponse(club,reviewRepository.findAverageRateByClubId(club).orElse(0f)));

        return getClubListResponse(clubInfoPages);
    }

    // 검색어를 통한 동아리 정보 조회
    @Override
    @Transactional
    public ClubListResponse getAllSearchClubInfo(Pageable pageable,String search, int category) {
        String keyword = search.replaceAll("\\s", "");
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));

        Page<Club> clubPage = category == -1 ? clubRepository.findAllByKeywordAndStatus(keyword,Status.ACCEPT,pageRequest)
                : clubRepository.findAllByKeywordAndStatusAndCategory(keyword, Status.ACCEPT, pageRequest, category);

        Page<ClubListInfoResponse> clubInfoPages = clubPage.map(club ->
                ClubListInfoResponse.toClubListInfoResponse(club,reviewRepository.findAverageRateByClubId(club).orElse(0f)));
        return getClubListResponse(clubInfoPages);
    }

    @Override
    @Transactional
    public ClubListResponse getAllStatusClubInfo(Pageable pageable,Status status, User user) {
        if(!user.getRole().equals(Role.ADMIN)){
            throw new UserAccessDeniedException();
        }
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByStatus(status, pageRequest);
        Page<ClubListInfoResponse> clubInfoPages = clubPage.map(club ->
                ClubListInfoResponse.toClubListInfoResponse(club,reviewRepository.findAverageRateByClubId(club).orElse(0f)));

        return getClubListResponse(clubInfoPages);
    }

    @Override
    @Transactional
    public ClubListResponse getAllMyClubInfo(Pageable pageable, User user) {
        List<Member> memberList = memberRepository.findAllByUser(user);
        List<Long> clubIds = new ArrayList<>();
        memberList.forEach(member->clubIds.add(member.getClub().getClubId()));

        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"clubId"));
        Page<Club> clubPage = clubRepository.findAllByClubIdIn(clubIds, pageRequest);
        Page<ClubListInfoResponse> clubInfoPages = clubPage.map(club ->
                ClubListInfoResponse.toClubListInfoResponse(club,reviewRepository.findAverageRateByClubId(club).orElse(0f)));

        return getClubListResponse(clubInfoPages);
    }

    @Override
    @Transactional
    public ClubListResponse getAllRecommendedClubInfo(Pageable pageable, String search, User user) {
        String cacheKey = makeCacheKey(user, pageable.getPageNumber());
        ClubListResponse cachedData = redisUtil.getData(cacheKey, ClubListResponse.class);
        log.info("cacheKey : {}, cachedData : {}", cacheKey, cachedData);
        if(cachedData != null){
            return cachedData;
        }

        User loginedUser = userRepository.findById(user.getId()).get();
        List<Interest> interestList = loginedUser.getInterests();
        List<Integer> categories = new ArrayList<>();
        for(Interest interest : interestList){
            categories.add(setCategory(interest.getInterest()));
        }

        Page<ClubListInfoResponse> clubList = null;
        if(search == null || search.isEmpty()){ // 전체 목록 조회
            clubList = clubRepositoryCustomImpl.findAllClubsWithAverageRating(categories,Status.ACCEPT,user.getBirth(),user.getGender(),pageable);
        }
        else{
            String keyword = search.replaceAll("\\s", "");
            clubList = clubRepositoryCustomImpl.findAllClubsWithAverageRatingByKeyword(categories,Status.ACCEPT,user.getBirth(),keyword, user.getGender(),pageable);
        }
        for(ClubListInfoResponse clubInfo : clubList){
            Club club = clubRepository.findById(clubInfo.getClubId()).get();
            List<Review> reviewList = club.getReviewList();
            clubInfo.setLatestReview(reviewList.isEmpty() ? null : reviewList.get(reviewList.size()-1).getReview());
        }
        ClubListResponse result = getClubListResponse(clubList);
        redisUtil.setData(cacheKey, result, EXPIRE);
        return result;
    }

    @Override
    @Transactional
    public List<ClubListInfoResponse> getAllRecommendedClubInfoLegacy(String search, User user) {
        User loginedUser = userRepository.findById(user.getId()).get();
        List<Interest> interestList = loginedUser.getInterests();
        List<Integer> categories = new ArrayList<>();
        for(Interest interest : interestList){
            categories.add(setCategory(interest.getInterest()));
        }

        List<ClubListInfoResponse> clubList = null;
        if(search == null || search.isEmpty()){ // 전체 목록 조회
            clubList = clubRepository.findAllClubsWithAverageRatingLegacy(
                    categories,
                    Status.ACCEPT,
                    user.getBirth(),
                    user.getGender());
            log.info("size = {}", clubList.size());
        }
        else{
            String keyword = search.replaceAll("\\s", "");
            clubList = clubRepository.findAllClubsWithAverageRatingAndKeywordLegacy(
                    categories,
                    Status.ACCEPT,
                    user.getBirth(),
                    keyword,
                    user.getGender());
        }
        for(ClubListInfoResponse clubInfo : clubList){
            Club club = clubRepository.findById(clubInfo.getClubId()).get();
            List<Review> reviewList = club.getReviewList();
            clubInfo.setLatestReview(reviewList.isEmpty() ? null : reviewList.get(reviewList.size()-1).getReview());
        }
        return clubList;
    }

    public String makeCacheKey(User user, int pageNum){
        List<Interest> interestList = interestRepository.findAllByUserId(user.getId());
        List<Integer> list = new ArrayList<>();
        for(Interest interest : interestList){
            list.add(setCategory(interest.getInterest()));
        }
        Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        for(int num : list){
            sb.append(num);
        }

        return sb.toString() + "_" + (user.getGender().equals("녀") ? "F" :  "M") + "_" + user.getBirth() + "_" + pageNum;
    }

    private static ClubListResponse getClubListResponse(Page<ClubListInfoResponse> clubInfoPages) {
        return ClubListResponse.builder()
                .pageNum(clubInfoPages.getNumber())
                .pageSize(clubInfoPages.getSize())
                .totalCnt(clubInfoPages.getTotalElements())
                .clubs(clubInfoPages.getContent())
                .build();
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
