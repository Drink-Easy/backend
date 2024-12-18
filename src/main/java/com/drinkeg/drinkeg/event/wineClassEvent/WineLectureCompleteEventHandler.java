package com.drinkeg.drinkeg.event.wineClassEvent;

import com.drinkeg.drinkeg.wineClassProgress.service.WineClassProgressService;
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
