package com.drinkeg.drinkeg.event.partyBookmarkEvent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PartyBookmarkDeleteEvent {
    private final Long partyId;
    private final Long memberId;
}
