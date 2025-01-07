package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wine.repository.dto.SortType;
import com.drinkeg.drinkeg.domain.wine.repository.dto.WineNoteStatisticsAvgDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


import static com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.domain.tastingNote.domain.QTastingNoteNose.tastingNoteNose;
import static com.drinkeg.drinkeg.domain.wine.domain.QWine.wine;

@Repository
@RequiredArgsConstructor
@Transactional
public class TastingNoteRepositoryImpl implements TastingNoteRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public WineNoteStatisticsAvgDto findWineNoteStatisticsByWineId(Long wineId) {
        return queryFactory
                .select(Projections.fields(WineNoteStatisticsAvgDto.class,
                        tastingNote.sugarContent.avg().coalesce(0.0).floatValue().as("avgSugarContent"),
                        tastingNote.acidity.avg().coalesce(0.0).floatValue().as("avgAcidity"),
                        tastingNote.tannin.avg().coalesce(0.0).floatValue().as("avgTannin"),
                        tastingNote.body.avg().coalesce(0.0).floatValue().as("avgBody"),
                        tastingNote.alcohol.avg().coalesce(0.0).floatValue().as("avgAlcohol"),
                        tastingNote.rating.avg().coalesce(0.0).floatValue().as("avgMemberRating")
                ))
                .from(tastingNote)
                .where(tastingNote.wine.id.eq(wineId))
                .fetchOne();
    }

    @Override
    public List<String> findTopThreeNoseByWineId(Long wineId) {
        return queryFactory
                .select(tastingNoteNose.noseElement)
                .from(tastingNoteNose)
                .where(tastingNoteNose.tastingNote.wine.id.eq(wineId))
                .groupBy(tastingNoteNose.noseElement)
                .orderBy(tastingNoteNose.noseElement.count().desc())
                .orderBy(tastingNoteNose.noseElement.asc())
                .limit(3)
                .fetch();
    }

    @Override
    public Optional<TastingNoteResponse> findTastingNoteWithWineAndNoseByTastingNoteIdAndUsername(Long tastingNoteId, String username) {
        TastingNote tastingNote = queryFactory.selectFrom(QTastingNote.tastingNote)
                .leftJoin(QTastingNote.tastingNote.wine, wine).fetchJoin()
                .leftJoin(QTastingNote.tastingNote.noseList, tastingNoteNose).fetchJoin()
                .where(QTastingNote.tastingNote.id.eq(tastingNoteId)
                        .and(QTastingNote.tastingNote.member.username.eq(username)))
                .fetchOne();

        if (tastingNote == null) {
            return Optional.empty();
        }

        return Optional.of(
                new TastingNoteResponse(
                        tastingNote.getId(),
                        tastingNote.getWine().getId(),
                        tastingNote.getWine().getName(),
                        tastingNote.getWine().getSort(),
                        tastingNote.getWine().getArea(),
                        tastingNote.getWine().getImageUrl(),
                        tastingNote.getColor(),
                        tastingNote.getTasteDate(),
                        tastingNote.getSugarContent(),
                        tastingNote.getAcidity(),
                        tastingNote.getTannin(),
                        tastingNote.getBody(),
                        tastingNote.getAlcohol(),
                        tastingNote.getNoseList(),
                        tastingNote.getRating(),
                        tastingNote.getReview()
                )
        );
    }

    @Override
    public Optional<TastingNote> findTastingNoteWithNoseById(Long tastingNoteId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(tastingNote)
                        .leftJoin(tastingNote.noseList, tastingNoteNose).fetchJoin()
                        .where(tastingNote.id.eq(tastingNoteId))
                        .fetchOne()
        );
    }

    @Override
    public List<TastingNote> findTastingNotesWithWineAndNoseByUsername(String username) {
        return queryFactory.selectFrom(tastingNote)
                .leftJoin(tastingNote.wine, wine).fetchJoin()
                .leftJoin(tastingNote.noseList, tastingNoteNose).fetchJoin()
                .where(tastingNote.member.username.eq(username))
                .fetch();
    }


    @Override
    public List<TastingNote> findAllTastingNoteBy(Long wineId, SortType sort) {
        return queryFactory.selectFrom(tastingNote)
                .where(tastingNote.wine.id.eq(wineId))
                .orderBy(orderCondition(sort), tastingNote.id.desc())
                .fetch();
    }

    private OrderSpecifier<?> orderCondition(SortType sortType) {
        return switch (sortType) {
            case LATEST -> new OrderSpecifier<>(Order.DESC, tastingNote.updatedAt);
            case OLDEST -> new OrderSpecifier<>(Order.ASC, tastingNote.updatedAt);
            case HIGH_RATING -> new OrderSpecifier<>(Order.DESC, tastingNote.rating);
            case LOW_RATING -> new OrderSpecifier<>(Order.ASC, tastingNote.rating);
        };
    }
}