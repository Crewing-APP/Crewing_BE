package com.crewing.club.repository;

import com.crewing.club.dto.ClubListInfoResponse;
import com.crewing.club.entity.QClub;
import com.crewing.club.entity.Status;
import com.crewing.member.entity.QMember;
import com.crewing.review.entity.QReview;
import com.crewing.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
    private final String IF_SAME_SUM = "SUM(CASE WHEN {0} = {1} THEN 1 ELSE 0 END)";

    @Override
    public Page<ClubListInfoResponse> findAllClubsWithAverageRating(List<Integer> categories, Status clubStatus, String birth, String gender, Pageable pageable) {
        QClub club = QClub.club;
        QReview review = QReview.review1;
        QMember member = QMember.member;
        QUser user = QUser.user;

        List<ClubListInfoResponse> results =  queryFactory
                .select(createClubListInfoProjection(club, review))
                .from(club)
                .leftJoin(club.reviewList, review)
                .leftJoin(club.memberList, member)
                .leftJoin(member.user, user)
                .where(getWhere(club,clubStatus,categories))
                .groupBy(club.clubId)
                .having(review.rate.avg().goe(2.0d))  // 리뷰 평균 평점이 2.0 이상인 경우만
                .orderBy(review.rate.avg().desc(),
                        Expressions.stringTemplate(IF_SAME_SUM, user.birth, birth).desc(),
                        Expressions.stringTemplate(IF_SAME_SUM, user.gender,gender).desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(club.count())
                .from(club)
                .where(getWhere(club,clubStatus,categories))
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
                .select(createClubListInfoProjection(club, review))
                .from(club)
                .leftJoin(club.reviewList, review)
                .leftJoin(club.memberList, member)
                .leftJoin(member.user, user)
                .where(getWhere(club,clubStatus,categories).and(Expressions.stringTemplate("REPLACE({0}, ' ', '')", club.name).like("%" + keyword + "%")))
                .groupBy(club.clubId)
                .having(review.rate.avg().goe(2.0d))  // 리뷰 평균 평점이 2.0 이상인 경우만
                .orderBy(review.rate.avg().desc(),
                        Expressions.stringTemplate(IF_SAME_SUM, user.birth, birth).desc(),
                        Expressions.stringTemplate(IF_SAME_SUM, user.gender,gender).desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(club.count())
                .from(club)
                .where(getWhere(club,clubStatus,categories).and(Expressions.stringTemplate("REPLACE({0}, ' ', '')", club.name).like("%" + keyword + "%")))
                .fetchOne();

        // Handle potential null value
        long totalElements = total != null ? total : 0;

        return new PageImpl<>(results, pageable, totalElements);
    }

    private ConstructorExpression<ClubListInfoResponse> createClubListInfoProjection(QClub club, QReview review) {
        NumberTemplate<Long> reviewCount = Expressions.numberTemplate(Long.class, "COUNT(DISTINCT {0})", review.reviewId);

        return Projections.constructor(ClubListInfoResponse.class,
                club.clubId,
                club.name,
                club.oneLiner,
                review.rate.avg(),
                reviewCount,
                Expressions.asString(""),
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
        );
    }

    BooleanBuilder getWhere(QClub club, Status clubStatus, List<Integer> categories) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(club.isRecruit.isTrue());
        whereClause.and(club.status.eq(clubStatus));
        if (categories != null && !categories.isEmpty()) {
            whereClause.and(club.category.in(categories));
        }

        return whereClause;
    }
}
