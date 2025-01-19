package com.drinkeg.drinkeg.domain.banner.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record BannerCreateRequest(
        @Schema(description = "배너 관련 게시글 URL", example = "www.drinkeg.com")
        String postUrl
) {
}
