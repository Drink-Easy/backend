package com.drinkeg.drinkeg.event.wineClassEvent;

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
    private final MemberService memberService;
    private final WineClassService wineClassService;

    @EventListener
    public void handleWineLectureCompleteEvent(WineLectureCompleteEvent event) {
        wineClassProgressService.updateWineClassProgress(
                wineClassService.getWineClassById(event.getWineClasId()),
                memberService.getMemberById(event.getMemberId()));
    }
}
