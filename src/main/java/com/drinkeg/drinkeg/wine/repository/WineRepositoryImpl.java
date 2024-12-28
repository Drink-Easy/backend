package com.drinkeg.drinkeg.wine.repository;

import com.drinkeg.drinkeg.dto.HomeDTO.QHomeWineDTO;
import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.dto.HomeDTO.HomeWineDTO;
import com.drinkeg.drinkeg.wine.dto.response.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.drinkeg.drinkeg.tastingNote.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.wine.domain.QWine.wine;
import static com.drinkeg.drinkeg.wineWishlist.domain.QWineWishlist.wineWishlist;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineRepositoryImpl implements WineRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    // 선택한 와인의 전체 리뷰 볼 때 사용
    @Override
    public List<WineReviewDTO> findWineReviewsByWineIdAndMemberId(Long wineId, boolean orderByLatest) {

        List<WineReviewDTO> recentReviews = queryFactory
                .select(new QWineReviewDTO(
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


    // 선택한 와인 정보와 최근 리뷰 3개 가져오기
    @Override
    public WineResponseWithThreeReviewsDTO findWineResponseByWineId(Long wineId, Long memberId) {

        // Wine 데이터를 가져옴
        WineResponseDTO wineResponseDTO = queryFactory
                .select(new QWineResponseDTO(
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
        List<WineReviewDTO> recentReviews = queryFactory
                .select(new QWineReviewDTO(
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

        return new WineResponseWithThreeReviewsDTO(wineResponseDTO, recentReviews);
    }

    // 홈하면 추천 와인 반환 시 사용
    @Override
    public List<HomeWineDTO> findRecommendWinesByMember(Member member) {
        List<String> wineSortList = member.getWineSort();
        List<String> wineAreaList = member.getWineArea();

        // maxPrice가 null이면 가격 제한을 10달러로
        Long maxPrice = member.getMonthPriceMax() != null ? member.getMonthPriceMax() / 1400 : 100;

        // BooleanBuilder로 동적 조건 생성
        // 기본 조건 평점 4점 이상으로 설정
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(wine.vivinoRating.goe(4));

        // wineSortList가 빈 리스트면 조건에서 제외
        BooleanBuilder sortCondition = new BooleanBuilder();
        if (!wineSortList.isEmpty()) {
            wineSortList.forEach(sort -> sortCondition.or(wine.sort.lower().containsIgnoreCase(sort)));
            condition.and(sortCondition);
        }

        // wineAreaList가 빈 리스트면 조건에서 제외
        BooleanBuilder areaCondition = new BooleanBuilder();
        if (!wineAreaList.isEmpty()) {
            wineAreaList.forEach(area -> areaCondition.or(wine.area.lower().containsIgnoreCase(area)));
            condition.and(areaCondition);
        }

        // 쿼리 실행 후 반환
        return queryFactory.select(new QHomeWineDTO(
                        wine.id,
                        wine.imageUrl,
                        wine.name.as("wineName"),
                        wine.sort,
                        wine.price.multiply(1400).divide(100).multiply(100)
                ))
                .from(wine)
                .where(
                        wine.price.loe(maxPrice)  // 가격 조건
                                .and(condition)  // sort와 area 조건을 모두 포함
                )
                .orderBy(
                        wine.vivinoRating
                                .add(new CaseBuilder()
                                        .when(sortCondition.and(areaCondition)) // 두 조건이 모두 일치하면 0.4
                                        .then(0.4)
                                        .when(sortCondition.or(areaCondition)) // 하나라도 일치하면 0.2
                                        .then(0.2)
                                        .otherwise(0.0))
                                .desc() // 내림차순 정렬
                )
                .limit(20)
                .fetch();

    }

    // 홈하면 인기 와인 반환 시 사용
    @Override
    public List<HomeWineDTO> findMostLikedWines() {
        return queryFactory.select(new QHomeWineDTO(
                        wine.id,
                        wine.imageUrl,
                        wine.name.as("wineName"),
                        wine.sort,
                        wine.price.multiply(1400).divide(100).multiply(100)
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

    // 검색한 와인들 정보 반환 시 사용
    @Override
    public List<SearchWineResponseDTO> findWinesWithLikeStatus(String searchName, Long memberId) {
        return queryFactory
                .select(new QSearchWineResponseDTO(
                        wine.id,
                        wine.name,
                        wine.imageUrl,
                        wine.sort,
                        wine.area,
                        wine.variety,
                        wine.vivinoRating,
                        wine.price,
                        wineWishlist.id.isNotNull() // memberId와 wineId에 따라 isLiked 여부
                ))
                .from(wine)
                .leftJoin(wineWishlist)
                .on(wineWishlist.wine.eq(wine).and(wineWishlist.member.id.eq(memberId)))
                .where(wine.name.containsIgnoreCase(searchName))
                .orderBy(wine.name.asc())
                .fetch();
    }
}