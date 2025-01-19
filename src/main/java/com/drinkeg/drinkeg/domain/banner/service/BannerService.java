package com.drinkeg.drinkeg.domain.banner.service;

import com.drinkeg.drinkeg.domain.banner.dto.request.BannerRequest;
import com.drinkeg.drinkeg.domain.banner.dto.response.AllBannerResponse;
import com.drinkeg.drinkeg.domain.banner.dto.response.BannerResponse;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import org.springframework.web.multipart.MultipartFile;

public interface BannerService {

    // 배너 생성 및 저장
    void saveBanner(MultipartFile bannerImage, BannerRequest bannerRequest, PrincipalDetail principalDetail);

    // 단일 배너 조회
    BannerResponse showBanner(Long bannerId, PrincipalDetail principalDetail);

    // 전체 배너 조회
    AllBannerResponse showAllBanner();

    // 배너 업데이트
    void updateBanner(Long bannerId, MultipartFile bannerImage, BannerRequest bannerRequest, PrincipalDetail principalDetail);

    // 배너 삭제
    void deleteBanner(Long bannerId, PrincipalDetail principalDetail);
}
