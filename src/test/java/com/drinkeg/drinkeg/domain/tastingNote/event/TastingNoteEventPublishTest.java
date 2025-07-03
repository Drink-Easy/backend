package com.drinkeg.drinkeg.domain.tastingNote.event;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteUpdateRequest;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.tastingNote.service.TastingNoteServiceImpl;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.event.WineNoteUpdateEvent;
import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.domain.wine.wineVintage.event.WineVintageNoteEvent;
import com.drinkeg.drinkeg.domain.wine.wineVintage.repository.WineVintageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class TastingNoteEventPublishTest extends IntegrationTestSupport {

    @Mock
    private TastingNoteRepository tastingNoteRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private WineVintageRepository wineVintageRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TastingNoteServiceImpl tastingNoteService;

    @DisplayName("테이스팅 노트 저장 시 이벤트 정상 발행")
    @Test
    void saveTastingNote_이벤트_정상발행() {
        // given
        Member member = createMember("testUser");
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(member));

        Wine wine = createWine("테스트 와인", "레드", "http://example.com/image.jpg");
        ReflectionTestUtils.setField(wine, "id", 1L);

        WineVintage wineVintage = WineVintage.create(2017, wine);
        ReflectionTestUtils.setField(wineVintage, "id", 100L);

        TastingNote tastingNote = mock(TastingNote.class);
        when(tastingNote.getId()).thenReturn(1L);

        TastingNoteRequest request = createTastingNoteRequest(wine, 2017);

        when(wineVintageRepository.findByWineIdAndVintageYear(eq(1L), eq(2017))).thenReturn(wineVintage);
        when(tastingNoteRepository.save(any(TastingNote.class))).thenReturn(tastingNote);

        // when
        tastingNoteService.saveTastingNote(request, "testUser");

        // then
        verify(eventPublisher).publishEvent(isA(WineVintageNoteEvent.class));
        verify(eventPublisher).publishEvent(isA(WineNoteUpdateEvent.class));
    }

    @DisplayName("테이스팅 노트 수정 시 이벤트 정상 발행")
    @Test
    void updateTastingNote_이벤트_정상발행() {
        // given
        Member member = createMember("testUser");
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(member));

        Wine wine = createWine("테스트 와인", "레드", "http://example.com/image.jpg");
        ReflectionTestUtils.setField(wine, "id", 1L);

        WineVintage wineVintage = WineVintage.create(2017, wine);
        ReflectionTestUtils.setField(wineVintage, "id", 100L);

        TastingNote tastingNote = mock(TastingNote.class);
        when(tastingNote.getId()).thenReturn(1L);
        when(tastingNote.getMember()).thenReturn(member);
        when(tastingNote.getWineVintage()).thenReturn(wineVintage);
        when(tastingNoteRepository.findTastingNoteWithWineById(anyLong())).thenReturn(tastingNote);

        TastingNoteUpdateRequest request = createTastingNoteUpdateRequest();

        // when
        tastingNoteService.updateTastingNote(1L, request, "testUser");

        // then
        verify(eventPublisher).publishEvent(isA(WineVintageNoteEvent.class));
        verify(eventPublisher).publishEvent(isA(WineNoteUpdateEvent.class));
    }

    @DisplayName("테이스팅 노트 삭제 시 이벤트 정상 발행")
    @Test
    void deleteTastingNote_이벤트_정상발행() {
        // given
        Member member = createMember("testUser");
        when(memberRepository.findByUsername(anyString())).thenReturn(Optional.of(member));

        Wine wine = createWine("테스트 와인", "레드", "http://example.com/image.jpg");
        ReflectionTestUtils.setField(wine, "id", 1L);

        WineVintage wineVintage = WineVintage.create(2017, wine);
        ReflectionTestUtils.setField(wineVintage, "id", 100L);

        TastingNote tastingNote = mock(TastingNote.class);
        when(tastingNote.getId()).thenReturn(1L);
        when(tastingNote.getMember()).thenReturn(member);
        when(tastingNote.getWineVintage()).thenReturn(wineVintage);
        when(tastingNoteRepository.findTastingNoteWithWineById(anyLong())).thenReturn(tastingNote);


        // when
        tastingNoteService.deleteTastingNote(1L, "testUser");

        // then
        verify(eventPublisher).publishEvent(isA(WineVintageNoteEvent.class));
        verify(eventPublisher).publishEvent(isA(WineNoteUpdateEvent.class));
    }

    private Member createMember(String username) {
        return Member.builder()
                .username(username)
                .password("password")
                .role(Role.ROLE_USER)
                .build();
    }


    private Wine createWine(String name, String sort, String imageUrl) {
        return Wine.builder()
                .name(name)
                .imageUrl(imageUrl)
                .sort(sort)
                .country("프랑스")
                .variety("샤도네이")
                .vivinoRating(4.1f)
                .price(100).build();
    }

    private TastingNoteRequest createTastingNoteRequest(Wine wine, Integer vintageYear) {
        List<String> noseList = List.of("오렌지", "시트러스", "건포도", "흙", "아몬드");

        return TastingNoteRequest.builder()
                .wineId(wine.getId())
                .wineVintage(vintageYear)
                .color("레드")
                .tasteDate(LocalDate.parse("2025-01-01"))
                .sweetness(10)
                .acidity(10)
                .tannin(10)
                .body(10)
                .alcohol(10)
                .nose(noseList)
                .rating(4.5f)
                .review("좋아요").build();
    }

    private TastingNoteUpdateRequest createTastingNoteUpdateRequest() {
        return new TastingNoteUpdateRequest(
                "화이트", LocalDate.parse("2025-01-09"), 20, 20, 20, 20, 20,
                List.of("오렌지", "장미", "차", "아몬드"), 3.5f, "맛있어요");
    }
}
