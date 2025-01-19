package com.drinkeg.drinkeg.domain.banner.service;

import com.drinkeg.drinkeg.domain.banner.dto.request.BannerCreateRequest;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import org.springframework.web.multipart.MultipartFile;

public interface BannerService {

    void saveBanner(MultipartFile bannerImage, BannerCreateRequest bannerCreateRequest, PrincipalDetail principalDetail);
}
