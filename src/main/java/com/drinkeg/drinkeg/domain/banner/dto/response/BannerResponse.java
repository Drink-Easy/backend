package com.drinkeg.drinkeg.domain.banner.dto.response;

import com.drinkeg.drinkeg.domain.banner.domain.Banner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerResponse {
    @Schema(description = "배너 id", example = "1")
    private Long id;
    @Schema(description = "배너 이미지 url", example = "https://drinkeg.amazonaws.com/123")
    private String imageUrl;
    @Schema(description = "배너 관련 게시글 url", example = "www.drinkeg.com")
    private String postUrl;

    public static BannerResponse of(Banner banner) {
        return BannerResponse.builder()
                .id(banner.getId())
                .imageUrl(banner.getImageUrl())
                .postUrl(banner.getPostUrl())
                .build();
    }
}
