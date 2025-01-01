package com.drinkeg.drinkeg.domain.wine.repository;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wine.dto.response.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.domain.wine.domain.QWine.wine;
import static com.drinkeg.drinkeg.domain.wineWishlist.domain.QWineWishlist.wineWishlist;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineRepositoryImpl implements WineRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    // 검색한 와인 PreviewResponse 반환 시 사용
    @Override
    public List<WinePreviewResponse> findSearchWines(String searchName) {
        return queryFactory
                .select(new QWinePreviewResponse(
                        wine.id,
                        wine.name,
                        wine.imageUrl,
                        wine.sort,
                        wine.area,
                        wine.variety,
                        wine.vivinoRating,
                        wine.price
                ))
                .from(wine)
                .where(wine.name.containsIgnoreCase(searchName))
                .orderBy(wine.name.asc())
                .fetch();
    }

    // 선택한 와인 정보와 최근 리뷰 3개 가져오기
    @Override
    public WineWithThreeReviewsResponse findWineResponseByWineId(Long wineId, Long memberId) {

        // Wine 데이터를 가져옴
        WineResponse wineResponse = queryFactory
                .select(new QWineResponse(
                        wine.id.as("wineId"),
                        wine.name,
                        wine.imageUrl,
                        wine.price.multiply(1400).divide(100).multiply(100).as("price"),
                        wine.sort,
                        wine.area,
                        wine.variety,
                        wine.vivinoRating,

                        wine.wineNote.avgSugarContent,
                        wine.wineNote.avgAcidity,
                        wine.wineNote.avgTannin,
                        wine.wineNote.avgBody,
                        wine.wineNote.avgAlcohol,

                        wine.wineNote.wineNoteNose,

                        wine.wineNote.avgMemberRating,
                        wineWishlist.id.isNotNull().as("isLiked") // memberId와 wineId에 따라 isLiked 여부
                ))
                .from(wine)
                .leftJoin(wine.wineNote)
                .leftJoin(wineWishlist)
                .on(wineWishlist.wine.eq(wine).and(wineWishlist.member.id.eq(memberId)))
                .where(wine.id.eq(wineId))
                .fetchOne();

        // 최근 생성된 3개의 TastingNote
        List<WineReviewResponse> recentReviews = queryFactory
                .select(new QWineReviewResponse(
                        tastingNote.member.name,
                        tastingNote.review,
                        tastingNote.rating,
                        tastingNote.createdAt
                ))
                .from(tastingNote)
                .where(tastingNote.wine.id.eq(wineId))
                .orderBy(tastingNote.createdAt.desc()) // 최신순 정렬
                .limit(3) // 상위 3개 제한
                .fetch();

        return new WineWithThreeReviewsResponse(wineResponse, recentReviews);
    }


    // 선택한 와인의 전체 리뷰 볼 때 사용
    @Override
    public List<WineReviewResponse> findWineReviewsByWineIdAndMemberId(Long wineId, boolean orderByLatest) {

        List<WineReviewResponse> recentReviews = queryFactory
                .select(new QWineReviewResponse(
                        tastingNote.member.name,
                        tastingNote.review,
                        tastingNote.rating,
                        tastingNote.createdAt
                ))
                .from(tastingNote)
                .where(tastingNote.wine.id.eq(wineId))
                .orderBy(orderByLatest? tastingNote.createdAt.desc()
                        : tastingNote.rating.desc()) // 최신순 정렬 or 별점 내림차순 정렬
                .fetch();

        return recentReviews;
    }


    // 홈하면 추천 와인 반환 시 사용
    @Override
    public List<HomeWineResponse> findRecommendWinesByMember(Member member) {
        List<String> wineSortList = member.getWineSort();
        List<String> wineAreaList = member.getWineArea();
        // maxPrice가 null이면 가격 제한을 100달러로
        Long maxPrice = member.getMonthPriceMax() != null ? member.getMonthPriceMax() / 1400 : 100;

        if(wineAreaList.isEmpty() && wineSortList.isEmpty()){
            return queryFactory.select(new QHomeWineResponse(
                            wine.id,
                            wine.imageUrl,
                            wine.name.as("wineName"),
                            wine.sort,
                            wine.price.multiply(1400).divide(100).multiply(100),
                            wine.vivinoRating
                    ))
                    .from(wine)
                    .where(
                            wine.price.loe(maxPrice)
                    )
                    .orderBy(
                            wine.vivinoRating.desc()
                    )
                    .limit(20)
                    .fetch();
        }

        BooleanBuilder condition = new BooleanBuilder();
        condition.and(wine.vivinoRating.goe(4));

        // wineSortList가 빈 리스트면 기본값 처리
        if (!wineSortList.isEmpty()) {
            wineSortList.forEach(sort -> condition.or(wine.sort.isNotNull().and(wine.sort.lower().containsIgnoreCase(sort))));
        }

        // wineAreaList가 빈 리스트면 기본값 처리
        if (!wineAreaList.isEmpty()) {
            wineAreaList.forEach(area -> condition.or(wine.area.isNotNull().and(wine.area.lower().containsIgnoreCase(area))));
        }

        // 쿼리 실행 후 반환
        return queryFactory.select(new QHomeWineResponse(
                        wine.id,
                        wine.imageUrl,
                        wine.name.as("wineName"),
                        wine.sort,
                        wine.price.multiply(1400).divide(100).multiply(100),
                        wine.vivinoRating
                ))
                .from(wine)
                .where(condition)
                .orderBy(
                        wine.vivinoRating.desc() // 내림차순 정렬
                )
                .limit(20)
                .fetch();
    }


    // 홈하면 인기 와인 반환 시 사용
    @Override
    public List<HomeWineResponse> findMostLikedWines() {
        return queryFactory.select(new QHomeWineResponse(
                        wine.id,
                        wine.imageUrl,
                        wine.name.as("wineName"),
                        wine.sort,
                        wine.price.multiply(1400).divide(100).multiply(100),
                        wine.vivinoRating
                ))
                .from(wine)
                .leftJoin(wineWishlist).on(wineWishlist.wine.eq(wine))
                .groupBy(wine.id)
                .orderBy(
                        // 먼저 wineWishlist의 개수를 기준으로 내림차순 정렬
                        wineWishlist.count().desc(),
                        // wineWishlist의 개수가 같은 경우 vivinoRating 순으로 정렬
                        wine.vivinoRating.desc()
                )
                .limit(10)
                .fetch();
    }
}