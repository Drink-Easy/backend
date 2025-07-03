package com.drinkeg.drinkeg.domain.tastingNote.repository;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.domain.WineNoteStatistics;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.domain.wine.wineVintage.repository.WineVintageRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class TastingNoteRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private TastingNoteRepository tastingNoteRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WineRepository wineRepository;
    @Autowired
    private WineVintageRepository wineVintageRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("와인 아이디와 빈티지 년도를 받아서 최근 3개의 테이스팅 노트를 조회한다.")
    @Test
    void findRecentThreeTastingNoteByWineIdAndVintageYearWineId() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("맛있는 와인"));
        WineVintage wineVintage = createWineVintage(wine, 2017);
        TastingNote note1 = createTastingNote(wineVintage, member, "가성비 좋아요");
        TastingNote note2 = createTastingNote(wineVintage, member, "나쁘지 않아요");
        TastingNote note3 = createTastingNote(wineVintage, member, "맛있어요!");
        TastingNote note4 = createTastingNote(wineVintage, member, "좋아요!");
        tastingNoteRepository.save(note1);
        tastingNoteRepository.save(note2);
        tastingNoteRepository.save(note3);
        tastingNoteRepository.save(note4);

        // when
        List<TastingNote> recentNotes = tastingNoteRepository.findRecentThreeTastingNoteByWineId(wine.getId(), wineVintage.getVintageYear());

        // then
        assertThat(recentNotes)
                .hasSize(3)
                .extracting("review")
                .containsExactly("좋아요!", "맛있어요!", "나쁘지 않아요");
    }

    @DisplayName("테이스팅 노트가 없는 경우 빈 리스트를 반환한다.")
    @Test
    void findRecentThreeTastingNoteByWineIdAndVintageYearWineIdWithNoNotes() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("맛있는 와인"));
        WineVintage wineVintage = createWineVintage(wine, 2017);

        // when
        List<TastingNote> recentNotes = tastingNoteRepository.findRecentThreeTastingNoteByWineId(wine.getId(), wineVintage.getVintageYear());

        // then
        assertThat(recentNotes).isEmpty();
    }

    @DisplayName("와인 아이디를 받아서 최근 3개의 테이스팅 노트를 리뷰가 null인 리뷰를 포함하여 조회한다.")
    @Test
    void findRecentThreeTastingNoteByWineIdAndVintageYearWineIdWithNullReview() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("맛있는 와인"));
        WineVintage wineVintage = createWineVintage(wine, 2017);
        TastingNote note1 = createTastingNote(wineVintage, member, "가성비 좋아요");
        TastingNote note2 = createTastingNote(wineVintage, member, null);
        TastingNote note3 = createTastingNote(wineVintage, member, "맛있어요!");
        TastingNote note4 = createTastingNote(wineVintage, member, "좋아요!");
        tastingNoteRepository.save(note1);
        tastingNoteRepository.save(note2);
        tastingNoteRepository.save(note3);
        tastingNoteRepository.save(note4);

        // when
        List<TastingNote> recentNotes = tastingNoteRepository.findRecentThreeTastingNoteByWineId(wine.getId());

        // then
        assertThat(recentNotes)
                .hasSize(3)
                .extracting("review")
                .containsExactly("좋아요!", "맛있어요!", null);
    }

    @DisplayName("테이스팅 노트가 1개인 경우 와인 아이디를 받아서 최근 1개의 테이스팅 노트를 조회한다.")
    @Test
    void findRecentOneTastingNoteByWineId() {
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("맛있는 와인"));
        WineVintage wineVintage = createWineVintage(wine, 2017);
        TastingNote note1 = createTastingNote(wineVintage, member, "가성비 좋아요");
        tastingNoteRepository.save(note1);

        // when
        List<TastingNote> recentNotes = tastingNoteRepository.findRecentThreeTastingNoteByWineId(wine.getId());

        // then
        assertThat(recentNotes)
                .hasSize(1)
                .extracting("review")
                .containsExactly("가성비 좋아요");
    }

    @DisplayName("탈퇴할 사용자의 username으로 테이스팅 노트의 member를 null로 업데이트한다.")
    @Test
    void updateTastingNoteMemberNull(){
        // given
        Member member = memberRepository.save(createMember("user"));
        Wine wine = wineRepository.save(createWine("맛있는 와인"));
        WineVintage wineVintage = createWineVintage(wine, 2017);
        TastingNote note1 = createTastingNote(wineVintage, member, "가성비 좋아요");
        TastingNote note2 = createTastingNote(wineVintage, member, "나쁘지 않아요");
        TastingNote note3 = createTastingNote(wineVintage, member, "맛있어요!");
        TastingNote note4 = createTastingNote(wineVintage, member, "좋아요!");
        tastingNoteRepository.save(note1);
        tastingNoteRepository.save(note2);
        tastingNoteRepository.save(note3);
        tastingNoteRepository.save(note4);

        // when
        tastingNoteRepository.updateTastingNoteMemberNull(member.getUsername());
        tastingNoteRepository.flush();
        entityManager.clear();

        // then
        List<TastingNote> tastingNotes = tastingNoteRepository.findAll();
        for(TastingNote tastingNote : tastingNotes){
            assertThat(tastingNote.getMember()).isNull();
        }
    }



    private TastingNote createTastingNote(WineVintage wineVintage, Member member, String review) {
        return TastingNote.builder()
                .wineVintage(wineVintage)
                .member(member)
                .review(review)
                .color("레드")
                .sweetness(50)
                .acidity(30)
                .tannin(20)
                .body(40)
                .alcohol(13)
                .rating(4.5f)
                .build();
    }

    private Wine createWine(String name) {
        return Wine.builder()
                .name(name)
                .imageUrl("http://default.image")
                .sort("레드")
                .country("프랑스")
                .variety("샤도네이")
                .vivinoRating(4.1f)
                .wineNoteStatistics(WineNoteStatistics.builder().build())
                .price(100).build();
    }

    private Member createMember(String username) {
        return Member.builder()
                .username(username)
                .role(Role.ROLE_USER)
                .isFirst(false)
                .build();
    }

    private WineVintage createWineVintage(Wine wine, int year) {
        WineVintage wineVintage=
                WineVintage.builder()
                        .wine(wine)
                        .vintageYear(year)
                        .build();

        wineVintageRepository.save(wineVintage);
        return wineVintage;
    }
}
