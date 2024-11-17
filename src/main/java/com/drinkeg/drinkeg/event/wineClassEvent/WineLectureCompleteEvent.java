package com.drinkeg.drinkeg.event.wineClassEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WineLectureCompleteEvent {
    private final Long wineClasId;
    private final Long memberId;
}
