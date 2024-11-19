package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.wineNote.domain.WineNote;
import com.drinkeg.drinkeg.dto.HomeDTO.HomeResponseDTO;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.SearchWineResponseDTO;

import java.util.List;
import java.util.Optional;

public class WineConverter {

    // 검색한 와인을 노트 와인 응답 DTO로 변환
    public static SearchWineResponseDTO toSearchWineResponseDTO(Wine wine, boolean isLiked) {
        return SearchWineResponseDTO.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .imageUrl(wine.getImageUrl())

                .isLiked(isLiked)
                .sort(wine.getSort())
                .area(wine.getArea())

                .price(((wine.getPrice() * 1300) / 1000) * 1000)

                // wine 기본 평점과, 사용자 평점 중 높은거로
                .satisfaction(Math.max(wine.getSatisfaction(),
                        Optional.ofNullable(wine.getWineNote()).map(WineNote::getAvgSatisfaction).orElse((float) 0)))
                .build();
    }

    // 홈화면 추천 와인 DTO 로 변환
    public static RecommendWineDTO toRecommendWineDTO(Wine wine){

        return RecommendWineDTO.builder()
                .wineId(wine.getId())
                .wineName(wine.getName())
                .imageUrl(wine.getImageUrl())
                .build();
    }

    // 홈화면 추천 와인 DTO 로 변환
    public static HomeResponseDTO toHomeResponseDTO(Member member, List<RecommendWineDTO> recommendWineDTOs){

        return HomeResponseDTO.builder()
                .name(member.getName())
                .recommendWineDTOs(recommendWineDTOs)
                .build();
    }
}
