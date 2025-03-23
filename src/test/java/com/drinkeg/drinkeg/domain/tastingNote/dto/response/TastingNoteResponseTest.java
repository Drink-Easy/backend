package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TastingNoteResponseTest extends IntegrationTestSupport {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    WineRepository wineRepository;
    @Autowired
    TastingNoteRepository tastingNoteRepository;

    @DisplayName("TastingNotePreviewResponse of 메서드 매개변수로 TastingNote가 들어가면 TastingNotePreviewResponse로 변환한다.")
    @Test
    void TastingNotePreviewResponseOf() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("와인"));
        TastingNote tastingNote = saveTastingNote(member, wine, 10, 20, 30, 40, 50, 4.5f);

        // when
        TastingNotePreviewResponse tastingNotePreviewResponse = TastingNotePreviewResponse.of(tastingNote);

        // then
        Assertions.assertThat(tastingNotePreviewResponse)
                .extracting("noteId", "tasteDate", "wineName", "imageUrl", "sort", "createdAt")
                .containsExactly(tastingNote.getId(), LocalDate.parse("2025-01-06"), wine.getName(), wine.getImageUrl(), wine.getSort(), tastingNote.getCreatedAt().toLocalDate());

    }

    @DisplayName("TastingNoteResponseTest of 메서드에 매개변수로 TastingNote가 들어가면 TastingNoteResponse로 변환한다.")
    @Test
    void TastingNoteResponseOf() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("와인"));
        TastingNote tastingNote = saveTastingNote(member, wine, 10, 20, 30, 40, 50, 4.5f);


        // when
        TastingNoteResponse tastingNoteResponse = TastingNoteResponse.of(tastingNote);

        // then
        Assertions.assertThat(tastingNoteResponse)
                .extracting("noteId", "wineId", "wineName", "sort", "country", "region", "imageUrl", "color", "tasteDate", "sweetness", "acidity", "tannin", "body", "alcohol", "rating", "review", "createdAt")
                .containsExactly(tastingNote.getId(), wine.getId(), wine.getName(), wine.getSort(), wine.getCountry(), wine.getRegion(), wine.getImageUrl(), "빨간색", LocalDate.of(2025, 1, 6), 10, 20, 30, 40, 50, 4.5f, "맛있어요", tastingNote.getCreatedAt().toLocalDate());
    }

    private Member createMember(String username) {
        return Member.builder()
                .username(username)
                .role(Role.ROLE_USER)
                .isFirst(false)
                .build();
    }
    private Wine createWine(String name) {
        return Wine.builder()
                .name(name)
                .imageUrl("http://default.image")
                .sort("레드")
                .country("프랑스")
                .region("보르도")
                .variety("샤도네이")
                .vivinoRating(4.1f)
                .wineNoteStatistics(WineNoteStatistics.builder().build())
                .price(100).build();
    }

    private TastingNote saveTastingNote(Member member, Wine wine,
                                          int sweetness, int acidity, int tannin, int body, int alcohol, float rating) {
        return tastingNoteRepository.save(
                TastingNote.builder()
                        .member(member)
                        .wine(wine)
                        .color("빨간색")
                        .tasteDate(LocalDate.of(2025, 1, 6))
                        .sweetness(sweetness)
                        .acidity(acidity)
                        .tannin(tannin)
                        .body(body)
                        .alcohol(alcohol)
                        .rating(rating)
                        .review("맛있어요")
                        .build());
    }


}