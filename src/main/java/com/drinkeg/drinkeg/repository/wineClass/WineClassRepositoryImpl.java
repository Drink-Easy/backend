package com.drinkeg.drinkeg.repository.wineClass;

import com.drinkeg.drinkeg.dto.WineClassDTO.response.QWineClassResponseDTO;
import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.drinkeg.drinkeg.domain.QWineClass.wineClass;
import static com.drinkeg.drinkeg.domain.QWineClassProgress.wineClassProgress;

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
                        wineClassProgress.progress))
                .from(wineClass)
                .join(wineClassProgress)
                .on(wineClassProgress.wineClass.eq(wineClass).and(wineClassProgress.member.id.eq(memberId)))
                .orderBy(wineClass.title.asc())
                .fetch();
    }

    @Override
    public WineClassResponseDTO findWineClassByIdAndMemberId(Long wineClassId, Long memberId) {
        return queryFactory
                .select(new QWineClassResponseDTO(
                        wineClass.id,
                        wineClass.category,
                        wineClass.title,
                        wineClass.thumbnailUrl,
                        wineClassProgress.progress))
                .from(wineClass)
                .join(wineClassProgress)
                .on(wineClassProgress.wineClass.eq(wineClass).and(wineClassProgress.member.id.eq(memberId)))
                .where(wineClassProgress.wineClass.id.eq(wineClassId))
                .fetchOne();
    }
}
