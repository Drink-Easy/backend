package com.drinkeg.drinkeg.domain.banner.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BannerRequest {
    @Schema(description = "배너 관련 게시글 URL", example = "www.drinkeg.com")
    @NotNull(message = "postUrl은 필수입니다.")
    String postUrl;

    @Builder
    public BannerRequest(String postUrl) {
        this.postUrl = postUrl;
    }
}
