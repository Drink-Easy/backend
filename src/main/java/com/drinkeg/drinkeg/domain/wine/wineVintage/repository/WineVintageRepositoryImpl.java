package com.drinkeg.drinkeg.domain.wine.wineVintage.repository;

import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.drinkeg.drinkeg.domain.wine.domain.QWine.wine;
import static com.drinkeg.drinkeg.domain.wine.wineVintage.domain.QWineVintage.wineVintage;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineVintageRepositoryImpl implements WineVintageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public WineVintage findByWineIdAndVintageYearFetch(Long wineId, int vintageYear) {
        return queryFactory.selectFrom(wineVintage)
                .join(wineVintage.wine, wine).fetchJoin()
                .where(wineVintage.wine.id.eq(wineId)
                        .and(wineVintage.vintageYear.eq(vintageYear)))
                .fetchOne();
    }
}
