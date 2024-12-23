package com.drinkeg.drinkeg.event.partyBookmarkEvent;

import com.drinkeg.drinkeg.party.service.PartyService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Transactional
@Component
public class PartyBookmarkEventHandler {

    private final PartyService partyService;

    @EventListener
    public void handlePartyBookmarkCreateEvent(PartyBookmarkCreateEvent event) {
        // 북마크가 추가된 경우
        partyService.increaseBookmarkCount(event.getPartyId());
        //Long memberId = event.getMemberId();
    }

    @EventListener
    public void handlePartyBookmarkDeleteEvent(PartyBookmarkDeleteEvent event) {
        // 북마크가 삭제된 경우
        partyService.decreaseBookmarkCount(event.getPartyId());
        //Long memberId = event.getMemberId();
    }
}