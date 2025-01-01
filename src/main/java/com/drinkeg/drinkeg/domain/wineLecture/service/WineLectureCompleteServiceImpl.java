package com.drinkeg.drinkeg.domain.wineLecture.service;

import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wineLecture.domain.WineLecture;
import com.drinkeg.drinkeg.domain.wineLecture.domain.WineLectureComplete;
import com.drinkeg.drinkeg.domain.member.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.domain.wineClass.event.WineLectureCompleteEvent;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.domain.wineLecture.repository.WineLectureRepository;
import com.drinkeg.drinkeg.domain.wineLecture.repository.WineLectureCompleteRepository;
import com.drinkeg.drinkeg.domain.member.service.MemberService;
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
    public void saveWineLectureComplete(Long wineLectureId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipalDetail(principalDetail);

        WineLecture wineLecture = wineLectureRepository.findById(wineLectureId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_LECTURE_NOT_FOUND));

        if (wineLectureCompleteRepository.existsByWineLectureAndMember(wineLecture, member))
            throw new GeneralException(ErrorStatus.WINE_LECTURE_COMPLETE_ALREADY_EXISTS);

        WineLectureComplete wineLectureComplete = WineLectureComplete.builder()
                .wineLecture(wineLecture)
                .member(member)
                .build();

        wineLectureCompleteRepository.save(wineLectureComplete);

        eventPublisher.publishEvent(new WineLectureCompleteEvent(wineLecture.getWineClass().getId(), member.getId()));
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
}
