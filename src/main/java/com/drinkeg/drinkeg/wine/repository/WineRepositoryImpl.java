package com.drinkeg.drinkeg.wine.repository;

import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.dto.HomeDTO.QRecommendWineDTO;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.wine.dto.response.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.drinkeg.drinkeg.domain.QWineWishlist.wineWishlist;
import static com.drinkeg.drinkeg.tastingNote.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.wine.domain.QWine.wine;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineRepositoryImpl implements WineRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<WineReviewResponseDTO> findWineReviewsById(Long wineId) {
        return queryFactory
                .select(new QWineReviewResponseDTO(
                        tastingNote.member.name,
                        tastingNote.review,
                        tastingNote.satisfaction
                ))
                .from(tastingNote)
                .join(tastingNote.wine, wine)
                .where(wine.id.eq(wineId))
                .fetch();
    }



    @Override
    public WineResponseWithThreeReviewsDTO findWineResponseByWineId(Long wineId) {

        // Wine 데이터를 가져옴
        WineResponseDTO wineResponseDTO = queryFactory
                .select(new QWineResponseDTO(
                        wine.id.as("wineId"),
                        wine.name,
                        wine.imageUrl,
                        wine.price.multiply(1300).divide(100).multiply(100).as("price"),
                        wine.sort,
                        wine.area,
                        wine.satisfaction,

                        wine.wineNote.avgSugarContent.as("avgSugarContent"),
                        wine.wineNote.avgAcidity.as("avgAcidity"),
                        wine.wineNote.avgTannin.as("avgTannin"),
                        wine.wineNote.avgBody.as("avgBody"),
                        wine.wineNote.avgAlcohol.as("avgAlcohol"),

                        wine.wineNote.wineNoteNose,

                        wine.wineNote.avgSatisfaction.as("avgSatisfaction")
                ))
                .from(wine)
                .leftJoin(wine.wineNote)
                .where(wine.id.eq(wineId))
                .fetchOne();

        // 최근 생성된 3개의 TastingNote
        Optional<List<WineReviewResponseDTO>> recentReviews = Optional.ofNullable(queryFactory
                .select(new QWineReviewResponseDTO(
                        tastingNote.member.name,
                        tastingNote.review,
                        tastingNote.satisfaction
                ))
                .from(tastingNote)
                .where(tastingNote.wine.id.eq(wineId))
                .orderBy(tastingNote.createdAt.desc()) // 최신순 정렬
                .limit(3) // 상위 3개 제한
                .fetch());

        return new WineResponseWithThreeReviewsDTO(wineResponseDTO, recentReviews.orElse(null));
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

        return queryFactory.select(new QRecommendWineDTO(
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
                .limit(20)
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