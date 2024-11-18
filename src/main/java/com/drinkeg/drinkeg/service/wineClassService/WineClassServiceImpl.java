package com.drinkeg.drinkeg.service.wineClassService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineClassConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.dto.WineClassDTO.request.WineClassRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.wineClass.WineClassRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.wineClassProgressService.WineClassProgressService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WineClassServiceImpl implements WineClassService {
    private final WineClassRepository wineClassRepository;
    private final MemberService memberService;

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
    public void saveWineClass(WineClassRequestDTO wineClassRequestDTO, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        wineClassRepository.save(WineClassConverter.toWineClass(wineClassRequestDTO, member));
    }

    @Override
    @Transactional
    public void updateWineClass(Long wineClassId, WineClassRequestDTO wineClassRequestDTO, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        WineClass wineClass = wineClassRepository.findById(wineClassId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND));

        if (!wineClass.getAuthor().equals(member) && !member.getRole().equals("ROLE_ADMIN"))
            throw new GeneralException(ErrorStatus.WINE_CLASS_UNAUTHORIZED);

        wineClass
                .updateTitle(wineClassRequestDTO.getTitle())
                .updateThumbnail(wineClassRequestDTO.getThumbnailUrl())
                .updateCategory(wineClassRequestDTO.getCategory());
    }

    @Override
    public void deleteWineClass(Long wineClassId, PrincipalDetail principalDetail) {
        if (!wineClassRepository.existsById(wineClassId))
            throw new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND);

        wineClassRepository.deleteById(wineClassId);
    }

    @Override
    public List<WineClass> getAllWineClasses() {
        return wineClassRepository.findAll();
    }

    @Override
    public WineClass getWineClassById(Long wineClassId) {
        return wineClassRepository.findById(wineClassId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND));
    }
}