package com.drinkeg.drinkeg.domain.wine.dto.response;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class WineResponseTest extends IntegrationTestSupport {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    WineRepository wineRepository;
    @Autowired
    TastingNoteRepository tastingNoteRepository;

    @DisplayName("HomeWineResponse of 메서드 매개변로 Wine이 들어가면 HomeWineResponse로 변환한다.")
    @Test
    void HomeWineResponseOf() {
        // given
        Wine wine = wineRepository.save(createWine());

        // when
        HomeWineResponse homeWineResponse = HomeWineResponse.of(wine);

        // then
        Assertions.assertThat(homeWineResponse)
                .extracting("wineId", "imageUrl", "wineName", "wineNameEng", "sort", "price", "vivinoRating")
                .containsExactly(wine.getId(), wine.getImageUrl(), wine.getName(), wine.getNameEng(), wine.getSort(), wine.getPrice(), wine.getVivinoRating());

    }

    @DisplayName("PageResponse of 메서드 매개변수로 Page가 들어가면 MyWineResponse로 변환한다.")
    @Test
    void PageResponseOf() {
        // given
        Wine wine = wineRepository.save(createWine());
        Pageable pageable = PageRequest.of(0, 10);
        Page<Wine> wines = wineRepository.findAll(pageable);

        // when
        PageResponse<Wine> pageResponse = PageResponse.of(wines);

        // then
        Assertions.assertThat(pageResponse)
                .extracting("content", "pageNumber", "totalPages")
                .containsExactly(wines.getContent(), wines.getNumber(), wines.getTotalPages());

    }

    @DisplayName("WineInfoResponse of 메서드 매개변수로 Wine과 isLiked가 들어가면 WineInfoResponse로 변환한다.")
    @Test
    void WineInfoResponseOf() {
        // given
        Wine wine = wineRepository.save(createWine());
        boolean isLiked = true;

        // when
        WineInfoResponse wineInfoResponse = WineInfoResponse.of(wine, isLiked);

        // then
        Assertions.assertThat(wineInfoResponse)
                .extracting("wineId", "name", "nameEng", "imageUrl", "price", "sort", "country", "region", "variety", "vivinoRating",
                        "avgSweetness", "avgAcidity", "avgTannin", "avgBody", "avgAlcohol", "nose1", "nose2", "nose3", "avgMemberRating", "liked")
                .containsExactly(wine.getId(), wine.getName(), wine.getNameEng(), wine.getImageUrl(), wine.getPrice(), wine.getSort(), wine.getCountry(), wine.getRegion(), wine.getVariety(), wine.getVivinoRating(),
                        wine.getWineNoteStatistics().getAvgSweetness(), wine.getWineNoteStatistics().getAvgAcidity(), wine.getWineNoteStatistics().getAvgTannin(), wine.getWineNoteStatistics().getAvgBody(),
                        wine.getWineNoteStatistics().getAvgAlcohol(), wine.getWineNoteStatistics().getNose1(), wine.getWineNoteStatistics().getNose2(), wine.getWineNoteStatistics().getNose3(), wine.getWineNoteStatistics().getAvgMemberRating(), isLiked);
    }

    @DisplayName("WinePreviewResponse of 메서드 매개변수로 Wine이 들어가면 WinePreviewResponse로 변환한다.")
    @Test
    void WinePreviewResponseOf() {
        // given
        Wine wine = wineRepository.save(createWine());

        // when
        WinePreviewResponse winePreviewResponse = WinePreviewResponse.of(wine);

        // then
        Assertions.assertThat(winePreviewResponse)
                .extracting("wineId", "name", "nameEng", "imageUrl", "sort", "country", "region", "variety", "vivinoRating", "price")
                .containsExactly(wine.getId(), wine.getName(), wine.getNameEng(), wine.getImageUrl(), wine.getSort(), wine.getCountry(), wine.getRegion(), wine.getVariety(), wine.getVivinoRating(), wine.getPrice());
    }

    @DisplayName("WineReviewResponse of 메서드 매개변수로 TastingNote가 들어가면 WineReviewResponse로 변환한다.")
    @Test
    void WineReviewResponseOf() {
        // given
        Member member = memberRepository.save(createMember());
        Wine wine = wineRepository.save(createWine());
        WineVintage wineVintage = createWineVintage(wine, 2017);
        TastingNote tastingNote = tastingNoteRepository.save(createTastingNote(member, wineVintage, "와인이 맛있어요."));

        // when
        WineReviewResponse wineReviewResponse = WineReviewResponse.of(tastingNote);

        // then
        Assertions.assertThat(wineReviewResponse)
                .extracting("name", "review", "rating", "createdAt")
                .containsExactly(member.getName(), tastingNote.getReview(), tastingNote.getRating(), tastingNote.getCreatedAt());

    }

    @DisplayName("WineReviewResponse of 메서드 매개변수로 TastingNote의 Member가 null이면 WineReviewResponse name은 '(알 수 없음)'으로 반환한다.")
    @Test
    void WineReviewResponseOfException() {
        // given
        Wine wine = wineRepository.save(createWine());
        WineVintage wineVintage = createWineVintage(wine, 2017);
        TastingNote tastingNote = tastingNoteRepository.save(createTastingNote(null, wineVintage, "와인이 맛있어요."));

        // when
        WineReviewResponse wineReviewResponse = WineReviewResponse.of(tastingNote);

        // then
        Assertions.assertThat(wineReviewResponse)
                .extracting("name", "review", "rating", "createdAt")
                .containsExactly("(알 수 없음)", tastingNote.getReview(), tastingNote.getRating(), tastingNote.getCreatedAt());

    }


    @DisplayName("WineWithThreeReviewsResponse of 메서드 매개변수로 Wine, TastingNote List, isLiked가 들어가면 WineWithThreeReviewsResponse로 변환한다.")
    @Test
    void WineWithThreeReviewsResponseOf() {
        // given
        Member member = memberRepository.save(createMember());
        Wine wine = wineRepository.save(createWine());
        WineVintage wineVintage = createWineVintage(wine, 2017);
        TastingNote tastingNote1 = tastingNoteRepository.save(createTastingNote(member, wineVintage, "와인이 맛있어요1."));
        TastingNote tastingNote2 = tastingNoteRepository.save(createTastingNote(member, wineVintage, "와인이 맛있어요2."));
        TastingNote tastingNote3 = tastingNoteRepository.save(createTastingNote(member, wineVintage, "와인이 맛있어요3."));
        List<TastingNote> tastingNotes = List.of(tastingNote3, tastingNote2, tastingNote1);

        // when
        WineWithThreeReviewsResponse wineWithThreeReviewsResponse = WineWithThreeReviewsResponse.of(wine, tastingNotes, true);

        // then
        Assertions.assertThat(wineWithThreeReviewsResponse.getWineInfoResponse())
                .extracting("wineId", "name", "nameEng", "imageUrl", "price", "sort", "country", "region", "variety", "vivinoRating",
                        "avgSweetness", "avgAcidity", "avgTannin", "avgBody", "avgAlcohol", "nose1", "nose2", "nose3", "avgMemberRating", "liked")
                .containsExactly(wine.getId(), wine.getName(), wine.getNameEng(), wine.getImageUrl(), wine.getPrice(), wine.getSort(), wine.getCountry(), wine.getRegion(), wine.getVariety(), wine.getVivinoRating(),
                        wine.getWineNoteStatistics().getAvgSweetness(), wine.getWineNoteStatistics().getAvgAcidity(), wine.getWineNoteStatistics().getAvgTannin(), wine.getWineNoteStatistics().getAvgBody(),
                        wine.getWineNoteStatistics().getAvgAlcohol(), wine.getWineNoteStatistics().getNose1(), wine.getWineNoteStatistics().getNose2(), wine.getWineNoteStatistics().getNose3(), wine.getWineNoteStatistics().getAvgMemberRating(), true);
        Assertions.assertThat(wineWithThreeReviewsResponse.getRecentReviews())
                .hasSize(3)
                .extracting("name", "review", "rating", "createdAt")
                .containsExactly(
                        Assertions.tuple(member.getName(), tastingNote3.getReview(), tastingNote3.getRating(), tastingNote3.getCreatedAt()),
                        Assertions.tuple(member.getName(), tastingNote2.getReview(), tastingNote2.getRating(), tastingNote2.getCreatedAt()),
                        Assertions.tuple(member.getName(), tastingNote1.getReview(), tastingNote1.getRating(), tastingNote1.getCreatedAt())

                );
    }

    @DisplayName("WineWithThreeReviewsResponse of 메서드에서 매개변수 TastingNote List가 빈 리스트면 정상적으로 반환한다.")
    @Test
    void WineWithThreeReviewsResponseOfException3() {
        // given
        Wine wine = wineRepository.save(createWine());
        List<TastingNote> tastingNotes = new ArrayList<>();

        // when
        WineWithThreeReviewsResponse wineWithThreeReviewsResponse = WineWithThreeReviewsResponse.of(wine, tastingNotes, true);

        // then
        Assertions.assertThat(wineWithThreeReviewsResponse.getWineInfoResponse())
                .extracting("wineId", "name", "nameEng", "imageUrl", "price", "sort", "country", "region", "variety", "vivinoRating",
                        "avgSweetness", "avgAcidity", "avgTannin", "avgBody", "avgAlcohol", "nose1", "nose2", "nose3", "avgMemberRating", "liked")
                .containsExactly(wine.getId(), wine.getName(), wine.getNameEng(), wine.getImageUrl(), wine.getPrice(), wine.getSort(), wine.getCountry(), wine.getRegion(), wine.getVariety(), wine.getVivinoRating(),
                        wine.getWineNoteStatistics().getAvgSweetness(), wine.getWineNoteStatistics().getAvgAcidity(), wine.getWineNoteStatistics().getAvgTannin(), wine.getWineNoteStatistics().getAvgBody(),
                        wine.getWineNoteStatistics().getAvgAlcohol(), wine.getWineNoteStatistics().getNose1(), wine.getWineNoteStatistics().getNose2(), wine.getWineNoteStatistics().getNose3(), wine.getWineNoteStatistics().getAvgMemberRating(), true);

        Assertions.assertThat(wineWithThreeReviewsResponse.getRecentReviews()).isEmpty();
    }


    private Member createMember() {
        return Member.builder()
                .username("user")
                .role(Role.ROLE_USER)
                .isFirst(false)
                .build();
    }

    private Wine createWine() {
        return Wine.builder()
                .name("와인1")
                .nameEng("wine1")
                .imageUrl("http://default.image")
                .sort("레드")
                .country("프랑스")
                .region("보르도")
                .variety("샤도네이")
                .vivinoRating(4.1f)
                .wineNoteStatistics(WineNoteStatistics.builder().build())
                .price(100).build();
    }

    private WineVintage createWineVintage(Wine wine, int vintageYear) {
        return WineVintage.builder()
                .wine(wine)
                .vintageYear(vintageYear)
                .build();
    }

    private TastingNote createTastingNote(Member member, WineVintage wineVintage, String review) {
        return TastingNote.builder()
                .member(member)
                .wineVintage(wineVintage)
                .color("0x000000")
                .tasteDate(LocalDate.parse("2025-01-01"))
                .sweetness(30)
                .acidity(30)
                .tannin(30)
                .body(30)
                .alcohol(30)
                .noseList(new ArrayList<>())
                .rating(4.5f)
                .review(review)
                .build();
    }

}