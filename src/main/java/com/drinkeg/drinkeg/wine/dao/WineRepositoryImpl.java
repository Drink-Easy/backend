package com.drinkeg.drinkeg.wine.dao;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.QSearchWineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineResponseDTO;
import com.drinkeg.drinkeg.wine.dto.response.WineReviewResponseDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.drinkeg.drinkeg.domain.QWine.wine;
import static com.drinkeg.drinkeg.domain.QWineWishlist.wineWishlist;
import static com.drinkeg.drinkeg.tastingNote.domain.QTastingNote.tastingNote;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineRepositoryImpl implements WineRepositoryCustom{

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
                        wine.wineNote.wineNoteNose,
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

    @Override
    public List<RecommendWineDTO> findRecommendWines(Member member) {
        List<String> wineSortList = member.getWineSort();
        List<String> wineAreaList = member.getWineArea();
        Long maxPrice = member.getMonthPriceMax()/1300;

        // BooleanBuilder로 동적 조건 생성
        BooleanBuilder sortCondition = new BooleanBuilder();
        wineSortList.forEach(sort -> sortCondition.or(wine.sort.lower().containsIgnoreCase(sort)));

        BooleanBuilder areaCondition = new BooleanBuilder();
        wineAreaList.forEach(area -> areaCondition.or(wine.area.lower().containsIgnoreCase(area)));

        return queryFactory.select(Projections.constructor(RecommendWineDTO.class,
                        wine.id,
                        wine.name,
                        wine.imageUrl
                ))
                .from(wine)
                .where(
                        wine.price.loe(maxPrice)
                                .and(sortCondition.or(areaCondition))
                )
                .orderBy(
                        wine.satisfaction
                                .add(new CaseBuilder()
                                        .when(sortCondition).then(0.2)
                                        .otherwise(0.0))
                                .add(new CaseBuilder()
                                        .when(areaCondition).then(0.2)
                                        .otherwise(0.0))
                                .desc()
                )
                .limit(10)
                .fetch();
    }

    @Override
    public List<SearchWineResponseDTO> findWinesWithLikeStatus(String searchName, Long memberId) {
        return queryFactory
                .select(new QSearchWineResponseDTO(
                        wine.id,
                        wine.name,
                        wine.imageUrl,
                        wine.sort,
                        wine.area,
                        wine.satisfaction,
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
