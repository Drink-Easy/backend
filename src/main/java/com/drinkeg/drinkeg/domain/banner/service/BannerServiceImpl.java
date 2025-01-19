package com.drinkeg.drinkeg.domain.banner.service;

import com.drinkeg.drinkeg.domain.banner.domain.Banner;
import com.drinkeg.drinkeg.domain.banner.dto.request.BannerRequest;
import com.drinkeg.drinkeg.domain.banner.dto.response.AllBannerResponse;
import com.drinkeg.drinkeg.domain.banner.dto.response.BannerResponse;
import com.drinkeg.drinkeg.domain.banner.repository.BannerRepository;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.service.MemberService;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.infra.storage.StoragePathName;
import com.drinkeg.drinkeg.infra.storage.StorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final MemberService memberService;
    private final StorageService storageService;

    // 배너 생성 및 저장
    @Override
    public void saveBanner(MultipartFile bannerImage, BannerRequest bannerRequest, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        if (!member.getRole().equals(Role.ROLE_ADMIN)) {
            throw new GeneralException(ErrorStatus.BANNER_UNAUTHORIZED);
        }

        // 배너 이미지 업로드
        String imageUrl = storageService.uploadFile(bannerImage, StoragePathName.BANNER);

        Banner banner = Banner.create(imageUrl, bannerRequest.getPostUrl());

        bannerRepository.save(banner);
    }

    // 단일 배너 조회
    @Override
    public BannerResponse showBanner(Long bannerId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        if (!member.getRole().equals(Role.ROLE_ADMIN)) {
            throw new GeneralException(ErrorStatus.BANNER_UNAUTHORIZED);
        }

        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BANNER_NOT_FOUND));

        return BannerResponse.of(banner);
    }

    // 전체 배너 조회
    @Override
    public AllBannerResponse showAllBanner() {
        List<Banner> bannerList = bannerRepository.findAll();

        List<BannerResponse> bannerResponseList = bannerList.stream()
                .map(BannerResponse::of)
                .toList();

        return AllBannerResponse.create(bannerResponseList);
    }

    // 배너 업데이트
    @Override
    public void updateBanner(Long bannerId, MultipartFile bannerImage, BannerRequest bannerRequest, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        if (!member.getRole().equals(Role.ROLE_ADMIN)) {
            throw new GeneralException(ErrorStatus.BANNER_UNAUTHORIZED);
        }

        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BANNER_NOT_FOUND));

        // 배너 이미지가 요청에 포함된 경우만 이미지 업데이트
        String newImageUrl = null;
        if (bannerImage != null) {
            storageService.deleteFile(banner.getImageUrl());
            newImageUrl = storageService.uploadFile(bannerImage, StoragePathName.BANNER);
        }

        banner.update(newImageUrl,
                !bannerRequest.getPostUrl().isEmpty() ? bannerRequest.getPostUrl() : null);
    }
}
