package com.drinkeg.drinkeg.wineLecture.repository;

import com.drinkeg.drinkeg.dto.WineLectureDTO.response.QWineLectureResponseDTO;
import com.drinkeg.drinkeg.wineLecture.dto.WineLectureResponseDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.drinkeg.drinkeg.wineLecture.domain.QWineLecture.wineLecture;
import static com.drinkeg.drinkeg.wineLecture.domain.QWineLectureComplete.wineLectureComplete;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineLectureRepositoryImpl implements WineLectureRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<WineLectureResponseDTO> findWineLectureListByWineClassIdAndMemberId(Long wineClassId, Long memberId) {
        return queryFactory.select(new QWineLectureResponseDTO(
                    wineLecture.id,
                    wineLecture.title,
                    wineLecture.content,
                    wineLectureComplete.id.isNotNull()
                ))
                .from(wineLecture)
                .leftJoin(wineLectureComplete)
                .on(wineLectureComplete.wineLecture.eq(wineLecture).and(wineLectureComplete.member.id.eq(memberId)))
                .where(wineLecture.wineClass.id.eq(wineClassId))
                .orderBy(wineLecture.id.asc())
                .fetch();
    }

    @Override
    public Optional<WineLectureResponseDTO> findWineLectureByIdAndMemberId(Long wineLectureId, Long memberId) {
        return Optional.ofNullable(
                queryFactory.select(new QWineLectureResponseDTO(
                    wineLecture.id,
                    wineLecture.title,
                    wineLecture.content,
                    wineLectureComplete.id.isNotNull()
                ))
                .from(wineLecture)
                .leftJoin(wineLectureComplete)
                .on(wineLectureComplete.wineLecture.eq(wineLecture).and(wineLectureComplete.member.id.eq(memberId)))
                .where(wineLecture.id.eq(wineLectureId))
                .orderBy(wineLecture.id.asc())
                .fetchOne()
        );
    }
}
