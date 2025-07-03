package com.drinkeg.drinkeg.domain.wineVintage.event;

import com.drinkeg.drinkeg.domain.wineVintage.service.WineVintageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WineEventHandlerTest {

    @Mock
    WineVintageService wineVintageService;

    @InjectMocks
    WineVintageEventHandler wineVintageEventHandler;

    @DisplayName("WineVintageNoteUpdateEvent 발생 시 WineVintageService의 updateWineVintageNoteStatics가 호출된다.")
    @Test
    void testHandleWineNoteUpdateEvent() {
        // given
        Long wineVintageId = 123L;
        WineVintageNoteEvent event = new WineVintageNoteEvent(wineVintageId);

        // when
        wineVintageEventHandler.handleTastingNoteUpdateEvent(event);

        // then
        verify(wineVintageService, times(1)).updateWineVintageNoteStatics(wineVintageId);
    }
}