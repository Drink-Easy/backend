package com.drinkeg.drinkeg.repository.wineClass;

import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional
public class WineClassRepositoryImpl implements WineClassRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Override
    public List<WineClassResponseDTO> findWineClassListByMemberId(Long memberId) {
        return List.of();
    }

    @Override
    public WineClassResponseDTO findWineClassByIdAndMemberId(Long id, Long memberId) {
        return null;
    }
}
