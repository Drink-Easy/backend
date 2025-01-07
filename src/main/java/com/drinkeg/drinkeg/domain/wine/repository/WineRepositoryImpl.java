package com.drinkeg.drinkeg.domain.wine.repository;

import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.dto.response.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.drinkeg.drinkeg.domain.wine.domain.QWine.wine;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineRepositoryImpl implements WineRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override // todo : 테스팅 코드 작성하기
    public List<Wine> findRecommendWinesBy(List<String> wineArea, List<String> wineSort, Long price) {
        return queryFactory.selectFrom(wine)
                .where(
                        wineAreaIn(wineArea),
                        wineSortIn(wineSort),
                        winePriceLessThan(price),
                        wine.vivinoRating.goe(4.0f))
                .limit(20)
                .fetch();
    }

    // 홈하면 인기 와인 반환 시 사용
    @Override
    public List<HomeWineResponse> findMostLikedWines() {
//        return queryFactory.select(new QHomeWineResponse(
//                        wine.id,
//                        wine.imageUrl,
//                        wine.name.as("wineName"),
//                        wine.sort,
//                        wine.price.multiply(1400).divide(100).multiply(100),
//                        wine.vivinoRating
//                ))
//                .from(wine)
//                .leftJoin(wineWishlist).on(wineWishlist.wine.eq(wine))
//                .groupBy(wine.id)
//                .orderBy(
//                        // 먼저 wineWishlist의 개수를 기준으로 내림차순 정렬
//                        wineWishlist.count().desc(),
//                        // wineWishlist의 개수가 같은 경우 vivinoRating 순으로 정렬
//                        wine.vivinoRating.desc()
//                )
//                .limit(10)
//                .fetch();
        return null;
    }

    private BooleanExpression wineAreaIn(List<String> wineAreaList) {
        return wineAreaList != null && !wineAreaList.isEmpty() ? wine.area.in(wineAreaList) : null;
    }

    private BooleanExpression wineSortIn(List<String> wineSortList) {
        return wineSortList != null && !wineSortList.isEmpty() ? wine.sort.in(wineSortList) : null;
    }

    private BooleanExpression winePriceLessThan(Long price) {
        return price == null || price < 50000 ? wine.price.loe(50000L) : wine.price.loe(price);
    }
}