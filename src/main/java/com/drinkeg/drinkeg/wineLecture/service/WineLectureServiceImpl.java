package com.drinkeg.drinkeg.wineLecture.service;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineLectureConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.wineClass.domain.WineClass;
import com.drinkeg.drinkeg.wineLecture.domain.WineLecture;
import com.drinkeg.drinkeg.wineLecture.dto.WineLectureRequestDTO;
import com.drinkeg.drinkeg.wineLecture.dto.WineLectureResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.wineLecture.repository.WineLectureRepository;
import com.drinkeg.drinkeg.wineClass.repository.WineClassRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WineLectureServiceImpl implements WineLectureService {
    private final WineLectureRepository wineLectureRepository;
    private final WineClassRepository wineClassRepository;
    private final MemberService memberService;

    @Override
    public List<WineLectureResponseDTO> showAllWineLecturesByWineClass(Long wineClassId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        return wineLectureRepository.findWineLectureListByWineClassIdAndMemberId(wineClassId, member.getId());
    }

    @Override
    public WineLectureResponseDTO showWineLectureById(Long wineLectureId,PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        return wineLectureRepository.findWineLectureByIdAndMemberId(wineLectureId, member.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_LECTURE_NOT_FOUND));
    }

    @Override
    public void saveWineLecture(WineLectureRequestDTO wineLectureRequestDTO, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        if (!member.getRole().equals("ROLE_ADMIN"))
            throw new GeneralException(ErrorStatus.WINE_LECTURE_UNAUTHORIZED);

        WineClass wineClass = wineClassRepository.findById(wineLectureRequestDTO.getWineClassId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND));

        WineLecture wineLecture = WineLectureConverter.toWineLecture(wineLectureRequestDTO, wineClass);

        wineLectureRepository.save(wineLecture);
    }

    @Override
    public void updateWineLecture(WineLectureRequestDTO wineLectureRequestDTO, Long wineLectureId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        if (!member.getRole().equals("ROLE_ADMIN"))
            throw new GeneralException(ErrorStatus.WINE_LECTURE_UNAUTHORIZED);

        WineLecture wineLecture = wineLectureRepository.findById(wineLectureId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_LECTURE_NOT_FOUND));

        WineClass wineClass = wineClassRepository.findById(wineLectureRequestDTO.getWineClassId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND));

        wineLecture
                .updateWineClass(wineClass)
                .updateTitle(wineLectureRequestDTO.getTitle())
                .updateContent(wineLectureRequestDTO.getContent());
    }

    @Override
    public void deleteWineLecture(Long wineLectureId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        if (!member.getRole().equals("ROLE_ADMIN"))
            throw new GeneralException(ErrorStatus.WINE_LECTURE_UNAUTHORIZED);

        WineLecture wineLecture = wineLectureRepository.findById(wineLectureId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_LECTURE_NOT_FOUND));

        wineLectureRepository.delete(wineLecture);
    }
}
