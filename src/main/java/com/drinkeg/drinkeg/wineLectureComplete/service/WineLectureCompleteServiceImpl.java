package com.drinkeg.drinkeg.wineLectureComplete.service;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineLectureCompleteConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.wineLecture.domain.WineLecture;
import com.drinkeg.drinkeg.wineLectureComplete.domain.WineLectureComplete;
import com.drinkeg.drinkeg.wineLectureComplete.dto.WineLectureCompleteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.event.wineClassEvent.WineLectureCompleteEvent;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.wineLecture.repository.WineLectureRepository;
import com.drinkeg.drinkeg.wineLectureComplete.repository.WineLectureCompleteRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WineLectureCompleteServiceImpl implements WineLectureCompleteService {
    private final WineLectureCompleteRepository wineLectureCompleteRepository;
    private final WineLectureRepository wineLectureRepository;
    private final MemberService memberService;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public WineLectureCompleteResponseDTO saveWineLectureComplete(Long wineLectureId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        WineLecture wineLecture = wineLectureRepository.findById(wineLectureId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_LECTURE_NOT_FOUND));

        if (wineLectureCompleteRepository.existsByWineLectureAndMember(wineLecture, member))
            throw new GeneralException(ErrorStatus.WINE_LECTURE_COMPLETE_ALREADY_EXISTS);

        WineLectureComplete wineLectureComplete = WineLectureCompleteConverter.toWineLectureComplete(wineLecture, member);
        wineLectureCompleteRepository.save(wineLectureComplete);

        eventPublisher.publishEvent(new WineLectureCompleteEvent(wineLecture.getWineClass().getId(), member.getId()));

        return WineLectureCompleteConverter.toWineLectureCompleteResponseDTO(wineLectureComplete);
    }

    @Override
    public void deleteWineLectureComplete(Long wineLectureId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        WineLectureComplete wineLectureComplete = wineLectureCompleteRepository.findByWineLectureIdAndMemberId(wineLectureId, member.getId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_LECTURE_COMPLETE_NOT_FOUND));

        if (!wineLectureComplete.getMember().equals(member) && !member.getRole().equals("ROLE_ADMIN"))
            throw new GeneralException(ErrorStatus.WINE_LECTURE_COMPLETE_UNAUTHORIZED);

        eventPublisher.publishEvent(new WineLectureCompleteEvent(wineLectureComplete.getWineLecture().getWineClass().getId(), member.getId()));

        wineLectureCompleteRepository.delete(wineLectureComplete);
    }

    @Override
    public boolean isCompleted(WineLecture wineLecture, Member member) {
        return wineLectureCompleteRepository.existsByWineLectureAndMember(wineLecture, member);
    }
}
