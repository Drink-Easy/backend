package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminWinePreviewResponse {

    private Long wineId;
    private String name;
    private String imageUrl;
    private String sort;
    private String variety;
    private String country;
    private String region;
    private LocalDateTime createdAt;


    @Builder
    public AdminWinePreviewResponse(Long wineId, String name, String imageUrl,
                                    String sort, String variety, String country, String region, LocalDateTime createdAt) {
        this.wineId = wineId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.sort = sort;
        this.variety = variety;
        this.country = country;
        this.region = region;
        this.createdAt = createdAt;
    }

    public static AdminWinePreviewResponse of(Wine wine) {
        return AdminWinePreviewResponse.builder()
                .wineId(wine.getId())
                .name(wine.getName())
                .imageUrl(wine.getImageUrl())
                .sort(wine.getSort())
                .variety(wine.getVariety())
                .country(wine.getCountry())
                .region(wine.getRegion())
                .createdAt(wine.getCreatedAt())
                .build();
    }

}
