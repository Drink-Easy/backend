package com.drinkeg.drinkeg.wineClass.service;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.storageService.StoragePathName;
import com.drinkeg.drinkeg.storageService.StorageService;
import com.drinkeg.drinkeg.wineClass.domain.WineClass;
import com.drinkeg.drinkeg.wineClass.dto.WineClassRequestDTO;
import com.drinkeg.drinkeg.wineClass.dto.WineClassResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.wineClass.repository.WineClassRepository;
import com.drinkeg.drinkeg.member.service.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WineClassServiceImpl implements WineClassService {
    private final WineClassRepository wineClassRepository;
    private final MemberService memberService;
    private final StorageService storageService;

    @Override
    public List<WineClassResponseDTO> showAllWineClasses(PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        return wineClassRepository.findWineClassListByMemberId(member.getId());
    }

    @Override
    public WineClassResponseDTO showWineClassById(Long wineClassId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        return wineClassRepository.findWineClassByIdAndMemberId(wineClassId, member.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND));
    }

    @Override
    @Transactional
    public void saveWineClass(WineClassRequestDTO wineClassRequestDTO, PrincipalDetail principalDetail) {
        String thumbnailUrl = storageService.uploadFile(wineClassRequestDTO.getThumbnail(), StoragePathName.WINE_CLASS);

        WineClass wineClass = WineClass.create(wineClassRequestDTO.getTitle(),
                wineClassRequestDTO.getCategory(),
                thumbnailUrl);

        wineClassRepository.save(wineClass);
    }

    @Override
    @Transactional
    public void updateWineClass(Long wineClassId, WineClassRequestDTO wineClassRequestDTO, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        WineClass wineClass = wineClassRepository.findById(wineClassId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND));

        if (!member.getRole().equals("ROLE_ADMIN"))
            throw new GeneralException(ErrorStatus.WINE_CLASS_UNAUTHORIZED);

        String thumbnailUrl = storageService.uploadFile(wineClassRequestDTO.getThumbnail(), StoragePathName.WINE_CLASS);

        wineClass
                .updateTitle(wineClassRequestDTO.getTitle())
                .updateThumbnail(thumbnailUrl)
                .updateCategory(wineClassRequestDTO.getCategory());
    }

    @Override
    public void deleteWineClass(Long wineClassId, PrincipalDetail principalDetail) {
        WineClass wineClass = wineClassRepository.findById(wineClassId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND));

        storageService.deleteFile(wineClass.getThumbnailUrl());

        wineClassRepository.deleteById(wineClassId);
    }
}