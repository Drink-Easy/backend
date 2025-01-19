package com.drinkeg.drinkeg.domain.banner.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BannerRequest {
    @Schema(description = "배너 관련 게시글 URL", example = "www.drinkeg.com")
    @NotNull(message = "필요하지 않은 경우 \"\"를 입력하세요.")
    String postUrl;
}
