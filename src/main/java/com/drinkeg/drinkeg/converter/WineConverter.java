package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineNote;
import com.drinkeg.drinkeg.dto.HomeDTO.HomeResponseDTO;
import com.drinkeg.drinkeg.dto.HomeDTO.RecommendWineDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.SearchWineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineResponseDTO;
import com.drinkeg.drinkeg.dto.WineDTO.response.WineReviewResponseDTO;

import java.util.List;
import java.util.Optional;

public class WineConverter {

    // 검색한 와인을 노트 와인 응답 DTO로 변환
    public static SearchWineResponseDTO toSearchWineResponseDTO(Wine wine) {
        return SearchWineResponseDTO.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .imageUrl(wine.getImageUrl())

                .sort(wine.getSort())
                .area(wine.getArea())

                .price(((wine.getPrice() * 1300) / 1000) * 1000)

                // wine 기본 평점과, 사용자 평점 중 높은거로
                .rating(Math.max(wine.getRating(),
                        Optional.ofNullable(wine.getWineNote()).map(WineNote::getRating).orElse((float) 0)))
                .build();
    }

    // 검색한 와인을 노트 와인 응답 DTO로 변환
    public static WineResponseDTO toWineResponseDTO(Wine wine) {
        WineNote wineNote = wine.getWineNote();

        return WineResponseDTO.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .imageUrl(wine.getImageUrl())

                .price(((wine.getPrice() * 1300) / 100) * 100)
                .sort(wine.getSort())
                .area(wine.getArea())

                .sugarContent(wineNote.getSugarContent())
                .acidity(wineNote.getAcidity())
                .tannin(wineNote.getTannin())
                .body(wineNote.getBody())
                .alcohol(wineNote.getAlcohol())

                .scentAroma(wineNote.getScentAroma())
                .scentTaste(wineNote.getScentTaste())
                .scentFinish(wineNote.getScentFinish())

                .rating(Math.max(wine.getRating(), wineNote.getRating()))
                .build();
    }

    // 와인에 대한 사용자 리뷰 DTO 로 변환
    public static WineReviewResponseDTO toWineReviewResPonseDTO(TastingNote tastingNote){

        return WineReviewResponseDTO.builder()
                .name(tastingNote.getMember().getName())
                .satisfaction(tastingNote.getSatisfaction())
                .review(tastingNote.getReview())
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
