package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HomeResponseDTO {

    private String name;

    @Builder.Default
    private List<RecommendWineDTO> recommendWineDTOs = new ArrayList<>();


    public static HomeResponseDTO create(Member member, List<RecommendWineDTO> recommendWineDTOs){

        return HomeResponseDTO.builder()
                .name(member.getName())
                .recommendWineDTOs(recommendWineDTOs)
                .build();
    }

}
