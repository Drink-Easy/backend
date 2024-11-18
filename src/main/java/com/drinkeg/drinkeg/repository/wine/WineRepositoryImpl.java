package com.drinkeg.drinkeg.repository.wine;

import com.drinkeg.drinkeg.dto.WineDTO.response.WineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineReviewResponseDTO;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.drinkeg.drinkeg.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.domain.QWine.wine;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineRepositoryImpl implements WineRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<WineReviewResponseDTO> findWineReviewsById(Long wineId) {
        return queryFactory
                .select(Projections.constructor(WineReviewResponseDTO.class,
                        tastingNote.member.name,
                        tastingNote.satisfaction,
                        tastingNote.review))
                .from(tastingNote)
                .join(tastingNote.wine, wine)
                .where(wine.id.eq(wineId))
                .fetch();
    }

    @Override
    public WineResponseDTO findWineResponseByWineId(Long wineId) {
        return queryFactory
                .select(Projections.constructor(WineResponseDTO.class,
                        wine.id.as("wineId"),
                        wine.name,
                        wine.imageUrl,
                        wine.price.multiply(1300).divide(100).multiply(100).as("price"),
                        wine.sort,
                        wine.area,
                        wine.wineNote.avgSugarContent.as("sugarContent"),
                        wine.wineNote.avgAcidity.as("acidity"),
                        wine.wineNote.avgTannin.as("tannin"),
                        wine.wineNote.avgBody.as("body"),
                        wine.wineNote.avgAlcohol.as("alcohol"),
                        wine.wineNote.nose,
                        new CaseBuilder()
                                .when(wine.wineNote.avgSatisfaction.eq(0.0F))
                                .then(wine.satisfaction)
                                .otherwise(wine.wineNote.avgSatisfaction).as("satisfaction")
                ))
                .from(wine)
                .leftJoin(wine.wineNote)
                .where(wine.id.eq(wineId))
                .fetchOne();
    }
}
