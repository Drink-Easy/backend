package com.drinkeg.drinkeg.event.wineClassEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WineLectureCompleteEvent {
    private final Long wineClassId;
    private final Long memberId;
}
