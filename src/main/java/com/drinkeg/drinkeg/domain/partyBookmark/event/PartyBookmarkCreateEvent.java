package com.drinkeg.drinkeg.domain.partyBookmark.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PartyBookmarkCreateEvent {
    private final Long partyId;
    private final Long memberId;
}
