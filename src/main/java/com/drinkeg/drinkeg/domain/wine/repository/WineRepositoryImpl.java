package com.drinkeg.drinkeg.domain.wine.repository;

import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.dto.response.WinePreviewResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.drinkeg.drinkeg.domain.wine.domain.QWine.wine;
import static com.drinkeg.drinkeg.domain.wineWishlist.domain.QWineWishlist.wineWishlist;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineRepositoryImpl implements WineRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Wine> findMostLikedWines() {
        return queryFactory.selectFrom(wine)
                .leftJoin(wineWishlist).on(wineWishlist.wine.eq(wine))
                .groupBy(wine.id)
                .orderBy(wineWishlist.count().desc(), wine.vivinoRating.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public Page<Wine> searchByNameWithPaging(String searchName, Pageable pageable) {
        List<Wine> wines = queryFactory.selectFrom(wine)
                .where(
                        wine.name.containsIgnoreCase(searchName)
                                .or(wine.nameEng.containsIgnoreCase(searchName))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(wine.name.asc())
                .fetch();

        long total = queryFactory.select(wine.count())
                .from(wine)
                .where(
                        wine.name.containsIgnoreCase(searchName)
                                .or(wine.nameEng.containsIgnoreCase(searchName))
                )
                .fetchOne();

        return new PageImpl<>(wines, pageable, total);
    }

    @Override
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

    private BooleanExpression wineAreaIn(List<String> wineAreaList) {
        return wineAreaList != null && !wineAreaList.isEmpty() ? wine.country.in(wineAreaList) : null;
    }

    private BooleanExpression wineSortIn(List<String> wineSortList) {
        return wineSortList != null && !wineSortList.isEmpty() ? wine.sort.in(wineSortList) : null;
    }

    private BooleanExpression winePriceLessThan(Long price) {
        return price == null || price < 50000 ? wine.price.loe(50000L) : wine.price.loe(price);
    }
}