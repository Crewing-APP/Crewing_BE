package com.crewing.club.repository;

import com.crewing.club.dto.ClubListInfoResponse;
import com.crewing.club.entity.QClub;
import com.crewing.club.entity.Status;
import com.crewing.member.entity.QMember;
import com.crewing.review.entity.QReview;
import com.crewing.user.entity.QUser;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ClubRepositoryCustomImpl implements ClubRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ClubListInfoResponse> findAllClubsWithAverageRating(List<Integer> categories, Status clubStatus, String birth, String gender, Pageable pageable) {
        QClub club = QClub.club;
        QReview review = QReview.review1;
        QMember member = QMember.member;
        QUser user = QUser.user;

        List<ClubListInfoResponse> results =  queryFactory
                .select(Projections.constructor(ClubListInfoResponse.class,
                        club.clubId,
                        club.name,
                        club.oneLiner,
                        review.rate.avg(),
                        review.count(),
                        ExpressionUtils.as(Expressions.constant(""), "latestReview"),
                        club.profile,
                        club.category,
                        club.status,
                        club.isRecruit,
                        club.isOnlyStudent,
                        club.docDeadLine,
                        club.docResultDate,
                        club.interviewStartDate,
                        club.interviewEndDate,
                        club.finalResultDate
                ))
                .from(club)
                .leftJoin(club.reviewList, review)
                .leftJoin(club.memberList, member)
                .leftJoin(member.user, user)
                .where(club.isRecruit.isTrue()
                        .and(club.status.eq(clubStatus))
                        .and(club.category.in(categories)))
                .groupBy(club.clubId)
                .orderBy(review.rate.avg().desc(),
                        Expressions.stringTemplate("SUM(CASE WHEN {0} = {1} THEN 1 ELSE 0 END)", user.birth, birth).desc(),
                        Expressions.stringTemplate("SUM(CASE WHEN {0} = {1} THEN 1 ELSE 0 END)", user.gender,gender).desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(club.count())
                .from(club)
                .where(club.isRecruit.isTrue()
                        .and(club.status.eq(clubStatus))
                        .and(club.category.in(categories)))
                .fetchOne();

        // Handle potential null value
        long totalElements = total != null ? total : 0;

        return new PageImpl<>(results, pageable, totalElements);
    }

    @Override
    public Page<ClubListInfoResponse> findAllClubsWithAverageRatingByKeyword(List<Integer> categories, Status clubStatus, String birth, String keyword, String gender, Pageable pageable) {
        QClub club = QClub.club;
        QReview review = QReview.review1;
        QMember member = QMember.member;
        QUser user = QUser.user;

        List<ClubListInfoResponse> results =  queryFactory
                .select(Projections.constructor(ClubListInfoResponse.class,
                        club.clubId,
                        club.name,
                        club.oneLiner,
                        review.rate.avg(),
                        review.count(),
                        Expressions.asString(""),  // latestReview placeholder
                        club.profile,
                        club.category,
                        club.status,
                        club.isRecruit,
                        club.isOnlyStudent,
                        club.docDeadLine,
                        club.docResultDate,
                        club.interviewStartDate,
                        club.interviewEndDate,
                        club.finalResultDate
                ))
                .from(club)
                .leftJoin(club.reviewList, review)
                .leftJoin(club.memberList, member)
                .leftJoin(member.user, user)
                .where(club.isRecruit.isTrue()
                        .and(club.status.eq(clubStatus))
                        .and(club.category.in(categories))
                        .and(Expressions.stringTemplate("REPLACE({0}, ' ', '')", club.name).like("%" + keyword + "%")))
                .groupBy(club.clubId)
                .orderBy(review.rate.avg().desc(),
                        Expressions.stringTemplate("SUM(CASE WHEN {0} = {1} THEN 1 ELSE 0 END)", user.birth, birth).desc(),
                        Expressions.stringTemplate("SUM(CASE WHEN {0} = {1} THEN 1 ELSE 0 END)", user.gender,gender).desc())
                .fetch();

        Long total = queryFactory
                .select(club.count())
                .from(club)
                .where(club.isRecruit.isTrue()
                        .and(club.status.eq(clubStatus))
                        .and(club.category.in(categories))
                        .and(Expressions.stringTemplate("REPLACE({0}, ' ', '')", club.name).like("%" + keyword + "%")))
                .fetchOne();

        // Handle potential null value
        long totalElements = total != null ? total : 0;

        return new PageImpl<>(results, pageable, totalElements);
    }
}
