package com.drinkeg.drinkeg.event.wineClassEvent;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.repository.wineClass.WineClassRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.wineClassProgressService.WineClassProgressService;
import com.drinkeg.drinkeg.service.wineClassService.WineClassService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Transactional
@Component
public class WineLectureCompleteEventHandler {
    private final WineClassProgressService wineClassProgressService;

    @EventListener
    public void handleWineLectureCompleteEvent(WineLectureCompleteEvent event) {
        wineClassProgressService.updateWineClassProgress(event.getWineClassId(), event.getMemberId());
    }
}
