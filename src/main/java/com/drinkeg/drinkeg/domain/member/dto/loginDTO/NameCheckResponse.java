package com.drinkeg.drinkeg.domain.member.dto.loginDTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NameCheckResponse {
    boolean canUse;

    public static NameCheckResponse create(boolean canUse){

        return NameCheckResponse.builder()
                .canUse(canUse)
                .build();
    }
}
