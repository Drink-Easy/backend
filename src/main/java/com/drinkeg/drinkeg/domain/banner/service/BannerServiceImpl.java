package com.drinkeg.drinkeg.domain.banner.service;

import com.drinkeg.drinkeg.domain.banner.domain.Banner;
import com.drinkeg.drinkeg.domain.banner.dto.request.BannerCreateRequest;
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

@Service
@RequiredArgsConstructor
@Transactional
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;
    private final MemberService memberService;
    private final StorageService storageService;

    // 배너 생성 및 저장
    @Override
    public void saveBanner(MultipartFile bannerImage, BannerCreateRequest bannerCreateRequest, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        if (!member.getRole().equals(Role.ROLE_ADMIN)) {
            throw new GeneralException(ErrorStatus.BANNER_UNAUTHORIZED);
        }

        // 배너 이미지 업로드
        String imageUrl = storageService.uploadFile(bannerImage, StoragePathName.BANNER);

        Banner banner = Banner.create(imageUrl, bannerCreateRequest.postUrl());

        bannerRepository.save(banner);
    }
}
