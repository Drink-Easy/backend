package com.drinkeg.drinkeg.dto.HomeDTO;

import com.drinkeg.drinkeg.member.domain.Member;
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
    private List<HomeWineDTO> recommendWineDTOS = new ArrayList<>();

    @Builder.Default
    private List<HomeWineDTO> MostLikedWineDTOS = new ArrayList<>();


    public static HomeResponseDTO create(Member member
            , List<HomeWineDTO> recommendWineDTOS, List<HomeWineDTO> mostLikedWineDTOS){

        return HomeResponseDTO.builder()
                .name(member.getName())
                .recommendWineDTOS(recommendWineDTOS)
                .MostLikedWineDTOS(mostLikedWineDTOS)
                .build();
    }

}
