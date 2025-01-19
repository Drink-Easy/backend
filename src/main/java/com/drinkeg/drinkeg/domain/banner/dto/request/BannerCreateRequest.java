package com.drinkeg.drinkeg.domain.banner.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BannerCreateRequest {
    @Schema(description = "배너 관련 게시글 URL", example = "www.drinkeg.com")
    String postUrl;
}
