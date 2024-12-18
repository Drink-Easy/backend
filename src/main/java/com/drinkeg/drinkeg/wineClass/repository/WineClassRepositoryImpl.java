package com.drinkeg.drinkeg.wineClass.repository;

import com.drinkeg.drinkeg.wineClass.dto.QWineClassResponseDTO;
import com.drinkeg.drinkeg.wineClass.dto.WineClassResponseDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.drinkeg.drinkeg.wineClass.domain.QWineClass.wineClass;
import static com.drinkeg.drinkeg.wineClassProgress.domain.QWineClassProgress.wineClassProgress;


@Repository
@RequiredArgsConstructor
@Transactional
public class WineClassRepositoryImpl implements WineClassRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<WineClassResponseDTO> findWineClassListByMemberId(Long memberId) {
        return queryFactory
                .select(new QWineClassResponseDTO(
                        wineClass.id,
                        wineClass.category,
                        wineClass.title,
                        wineClass.thumbnailUrl,
                        wineClassProgress.progress.coalesce(0.0f)))
                .from(wineClass)
                .leftJoin(wineClassProgress)
                .on(wineClassProgress.wineClass.eq(wineClass).and(wineClassProgress.member.id.eq(memberId)))
                .orderBy(wineClass.title.asc())
                .fetch();
    }

    @Override
    public Optional<WineClassResponseDTO> findWineClassByIdAndMemberId(Long wineClassId, Long memberId) {
        return Optional.ofNullable(
                queryFactory
                .select(new QWineClassResponseDTO(
                        wineClass.id,
                        wineClass.category,
                        wineClass.title,
                        wineClass.thumbnailUrl,
                        wineClassProgress.progress.coalesce(0.0f)))
                .from(wineClass)
                .leftJoin(wineClassProgress)
                .on(wineClassProgress.wineClass.eq(wineClass).and(wineClassProgress.member.id.eq(memberId)))
                .where(wineClassProgress.wineClass.id.eq(wineClassId))
                .fetchOne()
        );
    }
}
