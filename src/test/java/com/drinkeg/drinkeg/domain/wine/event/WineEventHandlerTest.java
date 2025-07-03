package com.drinkeg.drinkeg.domain.wine.event;

import com.drinkeg.drinkeg.domain.wine.service.WineService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WineEventHandlerTest {

    @Mock
    WineService wineService;

    @InjectMocks
    WineEventHandler wineEventHandler;

    @DisplayName("WineNoteUpdateEvent 발생 시 WineService의 updateWineNoteStatics가 호출된다.")
    @Test
    void testHandleWineNoteUpdateEvent() {
        // given
        Long wineId = 123L;
        WineNoteUpdateEvent event = new WineNoteUpdateEvent(wineId);

        // when
        wineEventHandler.handleTastingNoteUpdateEvent(event);

        // then
        verify(wineService, times(1)).updateWineNoteStatics(wineId);
    }
}