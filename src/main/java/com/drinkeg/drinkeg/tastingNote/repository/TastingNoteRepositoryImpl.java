package com.drinkeg.drinkeg.tastingNote.repository;

import com.drinkeg.drinkeg.tastingNote.domain.QTastingNote;
import com.drinkeg.drinkeg.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.tastingNote.dto.response.QTastingNoteResponseDTO;
import com.drinkeg.drinkeg.tastingNote.dto.response.TastingNoteResponseDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.drinkeg.drinkeg.tastingNote.domain.QTastingNote.tastingNote;
import static com.drinkeg.drinkeg.tastingNote.domain.QTastingNoteNose.tastingNoteNose;
import static com.drinkeg.drinkeg.wine.domain.QWine.wine;

@Repository
@RequiredArgsConstructor
@Transactional
public class TastingNoteRepositoryImpl implements TastingNoteRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<TastingNoteResponseDTO> findTastingNoteWithWineAndNoseByTastingNoteIdAndUsername(Long tastingNoteId, String username) {
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
                new TastingNoteResponseDTO(
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
                        tastingNote.getSatisfaction(),
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
}