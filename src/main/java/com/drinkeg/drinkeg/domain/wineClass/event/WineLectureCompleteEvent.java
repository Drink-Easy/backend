package com.drinkeg.drinkeg.domain.wineClass.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WineLectureCompleteEvent {
    private final Long wineClassId;
    private final Long memberId;
}
