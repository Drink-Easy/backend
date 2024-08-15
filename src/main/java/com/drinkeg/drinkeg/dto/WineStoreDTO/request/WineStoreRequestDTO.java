package com.drinkeg.drinkeg.dto.WineStoreDTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineStoreRequestDTO {
    @NotNull(message = "와인 스토어 이름은 필수입니다")
    private String name;
    @NotNull(message = "와인 스토어 주소는 필수입니다")
    private String address;
}