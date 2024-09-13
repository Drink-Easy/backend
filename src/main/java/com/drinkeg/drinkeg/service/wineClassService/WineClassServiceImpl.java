package com.drinkeg.drinkeg.service.wineClassService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineClassConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.dto.WineClassDTO.request.WineClassRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassDTO.response.WineClassResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.repository.WineClassBookMarkRepository;
import com.drinkeg.drinkeg.repository.WineClassRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WineClassServiceImpl implements WineClassService {
    private final WineClassRepository wineClassRepository;
    private final WineClassBookMarkRepository wineClassBookMarkRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<WineClassResponseDTO> getAllWineClasses(PrincipalDetail principalDetail) {
        Member member = memberRepository.findByUsername(principalDetail.getUsername())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<WineClass> wineClasses = wineClassRepository.findAll();

        return wineClasses.stream()
                .map(wineClass -> {
                    boolean isLiked = wineClassBookMarkRepository.existsByMemberAndWineClass(member, wineClass);
                    return WineClassConverter.toWineClassResponseDTO(wineClass, isLiked);
                })
                .collect(Collectors.toList());
    }

    @Override
    public WineClassResponseDTO getWineClassById(Long wineClassId, PrincipalDetail principalDetail) {
        Member member = memberRepository.findByUsername(principalDetail.getUsername())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        WineClass wineClass = wineClassRepository.findById(wineClassId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND));

        boolean isLiked = wineClassBookMarkRepository.existsByMemberAndWineClass(member, wineClass);

        return WineClassConverter.toWineClassResponseDTO(wineClass, isLiked);
    }

    @Override
    public void saveWineClass(WineClassRequestDTO wineClassRequestDTO, PrincipalDetail principalDetail) {
        Member member = memberRepository.findByUsername(principalDetail.getUsername())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        wineClassRepository.save(WineClassConverter.toWineClass(wineClassRequestDTO, member));
    }

    @Override
    @Transactional
    public WineClassResponseDTO updateWineClass(Long wineClassId, WineClassRequestDTO wineClassRequestDTO, PrincipalDetail principalDetail) {
        Member member = memberRepository.findByUsername(principalDetail.getUsername())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        WineClass wineClass = wineClassRepository.findById(wineClassId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND));

        if (!wineClass.getAuthor().equals(member))
            throw new GeneralException(ErrorStatus.WINE_CLASS_UNAUTHORIZED);

        wineClass
                .updateTitle(wineClassRequestDTO.getTitle())
                .updateThumbnail(wineClassRequestDTO.getThumbnailUrl())
                .updateContent(wineClassRequestDTO.getContent());

        boolean isLiked = wineClassBookMarkRepository.existsByMemberAndWineClass(member, wineClass);

        return WineClassConverter.toWineClassResponseDTO(wineClass, isLiked);
    }

    @Override
    public void deleteWineClass(Long wineClassId, PrincipalDetail principalDetail) {
        if (!wineClassRepository.existsById(wineClassId))
            throw new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND);
        wineClassRepository.deleteById(wineClassId);
    }
}