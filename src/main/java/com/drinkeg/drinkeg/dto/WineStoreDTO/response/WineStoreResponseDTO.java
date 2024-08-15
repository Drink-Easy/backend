package com.drinkeg.drinkeg.dto.WineStoreDTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineStoreResponseDTO {
    private Long id;
    private String name;
    private String address;
}
