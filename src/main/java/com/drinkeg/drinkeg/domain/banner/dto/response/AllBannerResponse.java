package com.drinkeg.drinkeg.domain.banner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AllBannerResponse {
    private List<BannerResponse> bannerResponseList;

    public static AllBannerResponse create(List<BannerResponse> bannerResponseList) {
        return new AllBannerResponse(bannerResponseList);
    }
}
