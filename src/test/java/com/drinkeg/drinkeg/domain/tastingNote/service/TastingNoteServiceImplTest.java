package com.drinkeg.drinkeg.domain.tastingNote.service;

import com.drinkeg.drinkeg.IntegrationTestSupport;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteNose;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteUpdateRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNotePreviewResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.drinkeg.drinkeg.domain.member.domain.Member.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

class TastingNoteServiceImplTest extends IntegrationTestSupport {

    @Autowired
    WineRepository wineRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TastingNoteRepository tastingNoteRepository;
    @Autowired
    TastingNoteService tastingNoteService;

    @DisplayName("테이스팅 노트를 저장한다.")
    @Test
    void saveTastingNote() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequest(wine);

        // when
        Long noteId = tastingNoteService.saveTastingNote(tastingNoteRequest, member.getUsername());

        // then
        Optional<TastingNote> tastingNote = tastingNoteRepository.findById(noteId);
        assertThat(tastingNote).isPresent();
        tastingNote.ifPresent(note -> {
            assertThat(note.getMember().getId()).isEqualTo(member.getId());
            assertThat(note.getWine().getId()).isEqualTo(wine.getId());
        });
    }

    @DisplayName("중복 노즈가 있는 경우 중복을 제거한 후 테이스팅 노트를 저장한다.")
    @Test
    void saveTastingNoteWithDuplicatedNoseElement() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNoteRequest tastingNoteRequest = new TastingNoteRequest(
                wine.getId(), "레드", LocalDate.parse("2025-01-01"), 10, 10, 10, 10, 10,
                List.of("장미", "장미", "장미", "우디", "시나몬"), 4.5f, "좋아요");

        // when
        Long noteId = tastingNoteService.saveTastingNote(tastingNoteRequest, member.getUsername());

        // then
        Optional<TastingNote> tastingNote = tastingNoteRepository.findById(noteId);
        assertThat(tastingNote).isPresent();
        tastingNote.ifPresent(note -> {
            assertThat(note.getMember().getId()).isEqualTo(member.getId());
            assertThat(note.getWine().getId()).isEqualTo(wine.getId());
            assertThat(note.getNoseList()).hasSize(3)
                    .extracting(TastingNoteNose::getNoseElement)
                    .containsExactlyInAnyOrder("장미", "우디", "시나몬");
        });
    }

    @DisplayName("없는 와인을 이용해 테이스팅 노트를 저장하면 WINE_NOT_FOUND 에러가 발생한다.")
    @Test
    void saveTastingNoteWithWrongWine() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequest(wine);
        wineRepository.delete(wine);

        // when & then
        assertThatThrownBy(() -> tastingNoteService.saveTastingNote(tastingNoteRequest, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.WINE_NOT_FOUND.getMessage());
    }

    @DisplayName("없는 회원이 테이스팅 노트를 저장하면 MEMBER_NOT_FOUND 에러가 발생한다.")
    @Test
    void saveTastingNoteWithWrongMember() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNoteRequest tastingNoteRequest = createTastingNoteRequest(wine);
        memberRepository.delete(member);

        // when & then
        assertThatThrownBy(() -> tastingNoteService.saveTastingNote(tastingNoteRequest, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("테이스팅 노트를 id와 username으로 조회한다.")
    @Test
    void showTastingNoteByIdAndUsername() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNote tastingNote = tastingNoteRepository.save(TastingNote.create(member, wine, createTastingNoteRequest(wine)));

        // when
        TastingNoteResponse tastingNoteResponse = tastingNoteService.showTastingNoteByIdAndUsername(tastingNote.getId(), member.getUsername());

        // then
        assertThat(tastingNoteResponse.getNoteId()).isEqualTo(tastingNote.getId());
    }


    @DisplayName("다른 사람의 테이스팅 노트를 조회하는 경우 TASTING_NOTE_FORBIDDEN 에러가 발생한다.")
    @Test
    void showTastingNoteByWrongUsername() {
        // given
        Member member1 = memberRepository.save(createMember("user1", "password", false));
        Member member2 = memberRepository.save(createMember("user2", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));

        TastingNote tastingNote1 = tastingNoteRepository.save(TastingNote.create(member1, wine, createTastingNoteRequest(wine)));

        // when & then
        assertThatThrownBy(() -> tastingNoteService.showTastingNoteByIdAndUsername(tastingNote1.getId(), member2.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.TASTING_NOTE_FORBIDDEN.getMessage());
    }

    @DisplayName("없는 테이스팅 노트를 조회하는 경우 TASTING_NOTE_NOT_FOUND 에러가 발생한다.")
    @Test
    void showTastingNoteByWrongId() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        tastingNoteRepository.save(TastingNote.create(member, wine, createTastingNoteRequest(wine)));

        // when & then
        assertThatThrownBy(() -> tastingNoteService.showTastingNoteByIdAndUsername(-1L, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.TASTING_NOTE_NOT_FOUND.getMessage());
    }

    @DisplayName("테이스팅 노트가 없는 경우 전체 테이스팅 노트를 조회한다.")
    @Test
    void findAllTastingNote_WithNoTastingNote() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));

        // when
        AllTastingNoteResponse allTastingNote = tastingNoteService.findAllTastingNote(TastingNoteWineSort.ALL, member.getUsername());
        List<TastingNotePreviewResponse> notePriviewList = allTastingNote.getNotePriviewList();
        TastingNoteSortCountResponse sortCount = allTastingNote.getSortCount();

        // then
        assertThat(notePriviewList).isEmpty();

        assertThat(sortCount)
                .extracting(
                        TastingNoteSortCountResponse::getTotalCount,
                        TastingNoteSortCountResponse::getRedCount,
                        TastingNoteSortCountResponse::getWhiteCount,
                        TastingNoteSortCountResponse::getSparklingCount,
                        TastingNoteSortCountResponse::getRoseCount,
                        TastingNoteSortCountResponse::getEtcCount
                )
                .containsExactly(0, 0, 0, 0, 0, 0);

    }

    @DisplayName("전체 테이스팅 노트를 조회한다.")
    @Test
    void findAllTastingNote() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));

        Wine wine1 = wineRepository.save(createWine("와인1", "레드", "http://default.image1"));
        Wine wine2 = wineRepository.save(createWine("와인2", "화이트", "http://default.image2"));
        Wine wine3 = wineRepository.save(createWine("와인3", "스파클링", "http://default.image3"));
        Wine wine4 = wineRepository.save(createWine("와인4", "로제", "http://default.image4"));
        Wine wine5 = wineRepository.save(createWine("와인5", "주정강화", "http://default.image5"));
        Wine wine6 = wineRepository.save(createWine("와인6", "기타", "http://default.image6"));

        TastingNote note1 = tastingNoteRepository.save(TastingNote.create(member, wine1, createTastingNoteRequest(wine1)));
        TastingNote note2 = tastingNoteRepository.save(TastingNote.create(member, wine2, createTastingNoteRequest(wine2)));
        TastingNote note3 = tastingNoteRepository.save(TastingNote.create(member, wine3, createTastingNoteRequest(wine3)));
        TastingNote note4 = tastingNoteRepository.save(TastingNote.create(member, wine4, createTastingNoteRequest(wine4)));
        TastingNote note5 = tastingNoteRepository.save(TastingNote.create(member, wine5, createTastingNoteRequest(wine5)));
        TastingNote note6 = tastingNoteRepository.save(TastingNote.create(member, wine6, createTastingNoteRequest(wine6)));

        // when
        AllTastingNoteResponse allTastingNote = tastingNoteService.findAllTastingNote(TastingNoteWineSort.ALL, member.getUsername());

        // then
        List<TastingNotePreviewResponse> notePriviewList = allTastingNote.getNotePriviewList();
        TastingNoteSortCountResponse sortCount = allTastingNote.getSortCount();

        assertThat(notePriviewList).hasSize(6)
                .extracting("noteId", "wineName", "imageUrl", "sort")
                .containsExactly(
                        tuple(note6.getId(), "와인6", "http://default.image6", "기타"),
                        tuple(note5.getId(), "와인5", "http://default.image5", "주정강화"),
                        tuple(note4.getId(), "와인4", "http://default.image4", "로제"),
                        tuple(note3.getId(), "와인3", "http://default.image3", "스파클링"),
                        tuple(note2.getId(), "와인2", "http://default.image2", "화이트"),
                        tuple(note1.getId(), "와인1", "http://default.image1", "레드")
                );

        assertThat(sortCount)
                .extracting(
                        TastingNoteSortCountResponse::getTotalCount,
                        TastingNoteSortCountResponse::getRedCount,
                        TastingNoteSortCountResponse::getWhiteCount,
                        TastingNoteSortCountResponse::getSparklingCount,
                        TastingNoteSortCountResponse::getRoseCount,
                        TastingNoteSortCountResponse::getEtcCount
                )
                .containsExactly(6, 1, 1, 1, 1, 2);

    }

    @DisplayName("전체 레드와인 테이스팅 노트를 조회한다.")
    @Test
    void findAllRedWineTastingNote() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine1 = wineRepository.save(createWine("와인1", "레드", "http://default.image1"));
        Wine wine2 = wineRepository.save(createWine("와인2", "레드", "http://default.image2"));
        Wine wine3 = wineRepository.save(createWine("와인3", "스파클링", "http://default.image3"));
        Wine wine4 = wineRepository.save(createWine("와인4", "로제", "http://default.image4"));
        Wine wine5 = wineRepository.save(createWine("와인5", "주정강화", "http://default.image5"));
        Wine wine6 = wineRepository.save(createWine("와인6", "기타", "http://default.image6"));

        TastingNote note1 = tastingNoteRepository.save(TastingNote.create(member, wine1, createTastingNoteRequest(wine1)));
        TastingNote note2 = tastingNoteRepository.save(TastingNote.create(member, wine2, createTastingNoteRequest(wine2)));
        TastingNote note3 = tastingNoteRepository.save(TastingNote.create(member, wine3, createTastingNoteRequest(wine3)));
        TastingNote note4 = tastingNoteRepository.save(TastingNote.create(member, wine4, createTastingNoteRequest(wine4)));
        TastingNote note5 = tastingNoteRepository.save(TastingNote.create(member, wine5, createTastingNoteRequest(wine5)));
        TastingNote note6 = tastingNoteRepository.save(TastingNote.create(member, wine6, createTastingNoteRequest(wine6)));

        // when
        AllTastingNoteResponse allTastingNote = tastingNoteService.findAllTastingNote(TastingNoteWineSort.RED, member.getUsername());

        // then
        List<TastingNotePreviewResponse> notePriviewList = allTastingNote.getNotePriviewList();
        TastingNoteSortCountResponse sortCount = allTastingNote.getSortCount();

        assertThat(notePriviewList).hasSize(2)
                .extracting("noteId", "wineName", "imageUrl", "sort")
                .containsExactly(
                        tuple(note2.getId(), "와인2", "http://default.image2", "레드"),
                        tuple(note1.getId(), "와인1", "http://default.image1", "레드")
                );

        assertThat(sortCount)
                .extracting(
                        TastingNoteSortCountResponse::getTotalCount,
                        TastingNoteSortCountResponse::getRedCount,
                        TastingNoteSortCountResponse::getWhiteCount,
                        TastingNoteSortCountResponse::getSparklingCount,
                        TastingNoteSortCountResponse::getRoseCount,
                        TastingNoteSortCountResponse::getEtcCount
                )
                .containsExactly(6, 2, 0, 1, 1, 2);

    }

    @DisplayName("전체 기타와인 테이스팅 노트를 조회한다.")
    @Test
    void findAllEtcWineTastingNote() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine1 = wineRepository.save(createWine("와인1", "레드","http://default.image1"));
        Wine wine2 = wineRepository.save(createWine("와인2", "화이트","http://default.image2"));
        Wine wine3 = wineRepository.save(createWine("와인3", "스파클링","http://default.image3"));
        Wine wine4 = wineRepository.save(createWine("와인4", "주정강화","http://default.image4"));
        Wine wine5 = wineRepository.save(createWine("와인5", "주정강화","http://default.image5"));
        Wine wine6 = wineRepository.save(createWine("와인6", "기타","http://default.image6"));

        TastingNote note1 = tastingNoteRepository.save(TastingNote.create(member, wine1, createTastingNoteRequest(wine1)));
        TastingNote note2 = tastingNoteRepository.save(TastingNote.create(member, wine2, createTastingNoteRequest(wine2)));
        TastingNote note3 = tastingNoteRepository.save(TastingNote.create(member, wine3, createTastingNoteRequest(wine3)));
        TastingNote note4 = tastingNoteRepository.save(TastingNote.create(member, wine4, createTastingNoteRequest(wine4)));
        TastingNote note5 = tastingNoteRepository.save(TastingNote.create(member, wine5, createTastingNoteRequest(wine5)));
        TastingNote note6 = tastingNoteRepository.save(TastingNote.create(member, wine6, createTastingNoteRequest(wine6)));

        // when
        AllTastingNoteResponse allTastingNote = tastingNoteService.findAllTastingNote(TastingNoteWineSort.ETCETERA, member.getUsername());

        // then
        List<TastingNotePreviewResponse> notePriviewList = allTastingNote.getNotePriviewList();
        TastingNoteSortCountResponse sortCount = allTastingNote.getSortCount();

        assertThat(notePriviewList).hasSize(3)
                .extracting("noteId", "wineName", "imageUrl", "sort")
                .containsExactly(
                        tuple(note6.getId(), "와인6", "http://default.image6", "기타"),
                        tuple(note5.getId(), "와인5", "http://default.image5", "주정강화"),
                        tuple(note4.getId(), "와인4", "http://default.image4", "주정강화"));

        assertThat(sortCount)
                .extracting(
                        TastingNoteSortCountResponse::getTotalCount,
                        TastingNoteSortCountResponse::getRedCount,
                        TastingNoteSortCountResponse::getWhiteCount,
                        TastingNoteSortCountResponse::getSparklingCount,
                        TastingNoteSortCountResponse::getRoseCount,
                        TastingNoteSortCountResponse::getEtcCount
                )
                .containsExactly(6, 1, 1, 1, 0, 3);
    }

    @DisplayName("없는 와인 종류로 전체 테이스팅 노트를 조회하면 IllegalArgumentException이 발생한다.")
    @Test
    void findAllTastingNoteByWriongSort() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));

        // when & then
        assertThatThrownBy(() -> tastingNoteService.findAllTastingNote(TastingNoteWineSort.of("WrongType"), member.getUsername()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 와인 종류입니다.");
    }

    @DisplayName("테이스팅 노트를 id와 username으로 삭제한다.")
    @Test
    void deleteTastingNote() {
        //given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNote tastingNote = tastingNoteRepository.save(TastingNote.create(member, wine, createTastingNoteRequest(wine)));

        //when
        Long deletedTastingNoteId = tastingNoteService.deleteTastingNote(tastingNote.getId(), member.getUsername());

        //then
        assertThat(deletedTastingNoteId).isEqualTo(tastingNote.getId());
    }

    @DisplayName("다른 사람의 테이스팅 노트를 삭제하는 경우 TASTING_NOTE_FORBIDDEN 에러가 발생한다.")
    @Test
    void deleteOthersTastingNote() {
        // given
        Member member1 = memberRepository.save(createMember("user1", "password", false));
        Member member2 = memberRepository.save(createMember("user2", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드","http://default.image"));

        TastingNote tastingNote1 = tastingNoteRepository.save(TastingNote.create(member1, wine, createTastingNoteRequest(wine)));

        // when & then
        assertThatThrownBy(() -> tastingNoteService.deleteTastingNote(tastingNote1.getId(), member2.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.TASTING_NOTE_FORBIDDEN.getMessage());
    }

    @DisplayName("없는 테이스팅 노트를 삭제하는 경우 TASTING_NOTE_NOT_FOUND 에러가 발생한다.")
    @Test
    void deleteTastingNoteByWrongId() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드","http://default.image"));
        tastingNoteRepository.save(TastingNote.create(member, wine, createTastingNoteRequest(wine)));

        // when & then
        assertThatThrownBy(() -> tastingNoteService.deleteTastingNote(-1L, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.TASTING_NOTE_NOT_FOUND.getMessage());
    }

    @DisplayName("없는 테이스팅 노트를 삭제하는 경우 TASTING_NOTE_NOT_FOUND 에러가 발생한다.")
    @Test
    void deleteTastingNoteByWrongMember() {
        // given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNote tastingNote = tastingNoteRepository.save(TastingNote.create(member, wine, createTastingNoteRequest(wine)));

        Member deleteMember = memberRepository.save(createMember("user2", "password", false));
        memberRepository.delete(deleteMember);

        // when & then
        assertThatThrownBy(() -> tastingNoteService.deleteTastingNote(tastingNote.getId(), deleteMember.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("테이스팅 노트를 수정한다.")
    @Test
    void updateTastingNote() {
        //given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNote tastingNote = tastingNoteRepository.save(TastingNote.create(member, wine, createTastingNoteRequest(wine)));
        TastingNoteUpdateRequest updateRequest = createTastingNoteUpdateRequest();

        //when
        tastingNoteService.updateTastingNote(tastingNote.getId(), updateRequest, member.getUsername());

        //then
        TastingNote updatedNote = tastingNoteRepository.findById(tastingNote.getId()).get();
        assertThat(updatedNote)
                .extracting(
                        TastingNote::getColor, TastingNote::getTasteDate,
                        TastingNote::getSugarContent, TastingNote::getAcidity, TastingNote::getTannin, TastingNote::getBody, TastingNote::getAlcohol,
                        TastingNote::getRating, TastingNote::getReview,
                        (updateNote -> {
                            List<TastingNoteNose> noseList = updateNote.getNoseList();
                            return noseList.stream().map(TastingNoteNose::getNoseElement).toList();
                        })
                )
                .containsExactly(
                        "화이트", LocalDate.parse("2025-01-09"), 20, 20, 20, 20, 20, 3.5f, "맛있어요",
                        List.of("오렌지", "장미", "차", "아몬드")
                );
    }

    @DisplayName("테이스팅 노트 수정 시 중복된 노즈가 있는 경우 중복을 제거하여 수정한다.")
    @Test
    void updateTastingNoteWithDuplicatedNoseElement() {
        //given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNote tastingNote = tastingNoteRepository.save(TastingNote.create(member, wine, createTastingNoteRequest(wine)));
        TastingNoteUpdateRequest updateRequest = new TastingNoteUpdateRequest(
                "화이트", LocalDate.parse("2025-01-09"), 20, 20, 20, 20, 20,
                List.of("장미", "장미", "차", "장미", "차"), 3.5f, "맛있어요"
        );

        //when
        tastingNoteService.updateTastingNote(tastingNote.getId(), updateRequest, member.getUsername());

        //then
        TastingNote updatedNote = tastingNoteRepository.findById(tastingNote.getId()).get();
        assertThat(updatedNote)
                .extracting(
                        TastingNote::getColor, TastingNote::getTasteDate,
                        TastingNote::getSugarContent, TastingNote::getAcidity, TastingNote::getTannin, TastingNote::getBody, TastingNote::getAlcohol,
                        TastingNote::getRating, TastingNote::getReview,
                        (updateNote -> {
                            List<TastingNoteNose> noseList = updateNote.getNoseList();
                            return noseList.stream().map(TastingNoteNose::getNoseElement).toList();
                        })
                )
                .containsExactly(
                        "화이트", LocalDate.parse("2025-01-09"), 20, 20, 20, 20, 20, 3.5f, "맛있어요",
                        List.of("장미", "차")
                );
    }

    @DisplayName("테이스팅 노트 수정 시 노즈에 빈 리스트가 있는 경우 기존 노즈를 삭제한다.")
    @Test
    void updateTastingNoteWithEmptyNoseList() {
        //given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNote tastingNote = tastingNoteRepository.save(TastingNote.create(member, wine, createTastingNoteRequest(wine)));
        TastingNoteUpdateRequest updateRequest = new TastingNoteUpdateRequest(
                "화이트", LocalDate.parse("2025-01-09"), 20, 20, 20, 20, 20,
                new ArrayList<>(), 3.5f, "맛있어요"
        );

        //when
        tastingNoteService.updateTastingNote(tastingNote.getId(), updateRequest, member.getUsername());

        //then
        TastingNote updatedNote = tastingNoteRepository.findById(tastingNote.getId()).get();
        assertThat(updatedNote)
                .extracting(
                        TastingNote::getColor, TastingNote::getTasteDate,
                        TastingNote::getSugarContent, TastingNote::getAcidity, TastingNote::getTannin, TastingNote::getBody, TastingNote::getAlcohol,
                        TastingNote::getRating, TastingNote::getReview,
                        (updateNote -> {
                            List<TastingNoteNose> noseList = updateNote.getNoseList();
                            return noseList.stream().map(TastingNoteNose::getNoseElement).toList();
                        })
                )
                .containsExactly(
                        "화이트", LocalDate.parse("2025-01-09"), 20, 20, 20, 20, 20, 3.5f, "맛있어요",
                        new ArrayList<>()
                );
    }

    @DisplayName("updateRequest가 모두 null인 경우 테이스팅 노트 수정 결과 기존과 동일하다.")
    @Test
    void updateTastingNoteWithNoChange() {
        //given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNote tastingNote = tastingNoteRepository.save(TastingNote.create(member, wine, createTastingNoteRequest(wine)));
        TastingNoteUpdateRequest updateRequest = new TastingNoteUpdateRequest(
                null, null, null, null, null,
                null, null, null, null, null);

        //when
        tastingNoteService.updateTastingNote(tastingNote.getId(), updateRequest, member.getUsername());

        //then
        TastingNote updatedNote = tastingNoteRepository.findById(tastingNote.getId()).get();
        assertThat(updatedNote)
                .extracting(
                        TastingNote::getColor, TastingNote::getTasteDate,
                        TastingNote::getSugarContent, TastingNote::getAcidity, TastingNote::getTannin, TastingNote::getBody, TastingNote::getAlcohol,
                        TastingNote::getRating, TastingNote::getReview,
                        (updateNote -> {
                            List<TastingNoteNose> noseList = updateNote.getNoseList();
                            return noseList.stream().map(TastingNoteNose::getNoseElement).toList();
                        })
                )
                .containsExactly(
                        "레드", LocalDate.parse("2025-01-01"), 10, 10, 10, 10, 10, 4.5f, "좋아요",
                        List.of("오렌지", "시트러스", "건포도", "흙", "아몬드")
                );
    }

    @DisplayName("없는 회원이 테이스팅 노트를 수정하면 MEMBER_NOT_FOUND 에러가 발생한다.")
    @Test
    void updateTastingNoteByWrongMember() {
        //given
        Member WrongMember = memberRepository.save(createMember("user1", "password", false));
        memberRepository.delete(WrongMember);

        Member member = memberRepository.save(createMember("user2", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNote tastingNote = tastingNoteRepository.save(TastingNote.create(member, wine, createTastingNoteRequest(wine)));
        TastingNoteUpdateRequest updateRequest = new TastingNoteUpdateRequest(
                "화이트", LocalDate.parse("2025-01-09"), 20, 20, 20, 20, 20,
                List.of("장미", "장미", "차", "장미", "차"), 3.5f, "맛있어요"
        );

        //when && then
        assertThatThrownBy(() -> tastingNoteService.updateTastingNote(tastingNote.getId(), updateRequest, WrongMember.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
    }

    @DisplayName("없는 테이스팅 노트를 수정하면 TASTING_NOTE_NOT_FOUND 에러가 발생한다.")
    @Test
    void updateTastingNoteByWrongTastingNote() {
        //given
        Member member = memberRepository.save(createMember("user1", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNote tastingNote = tastingNoteRepository.save(TastingNote.create(member, wine, createTastingNoteRequest(wine)));
        tastingNoteRepository.delete(tastingNote);
        TastingNoteUpdateRequest updateRequest = new TastingNoteUpdateRequest(
                "화이트", LocalDate.parse("2025-01-09"), 20, 20, 20, 20, 20,
                List.of("장미", "장미", "차", "장미", "차"), 3.5f, "맛있어요"
        );

        //when && then
        assertThatThrownBy(() -> tastingNoteService.updateTastingNote(tastingNote.getId(), updateRequest, member.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.TASTING_NOTE_NOT_FOUND.getMessage());
    }

    @DisplayName("다른 회원의 테이스팅 노트를 수정하면 TASTING_NOTE_NOT_FOUND 에러가 발생한다.")
    @Test
    void updateForbiddenTastingNote() {
        //given
        Member member1 = memberRepository.save(createMember("user1", "password", false));
        Member member2 = memberRepository.save(createMember("user2", "password", false));
        Wine wine = wineRepository.save(createWine("와인1", "레드", "http://default.image"));
        TastingNote tastingNote = tastingNoteRepository.save(TastingNote.create(member2, wine, createTastingNoteRequest(wine)));
        TastingNoteUpdateRequest updateRequest = new TastingNoteUpdateRequest(
                "화이트", LocalDate.parse("2025-01-09"), 20, 20, 20, 20, 20,
                List.of("장미", "장미", "차", "장미", "차"), 3.5f, "맛있어요"
        );

        //when && then
        assertThatThrownBy(() -> tastingNoteService.updateTastingNote(tastingNote.getId(), updateRequest, member1.getUsername()))
                .isInstanceOf(GeneralException.class)
                .hasMessage(ErrorStatus.TASTING_NOTE_FORBIDDEN.getMessage());
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

    private TastingNoteRequest createTastingNoteRequest(Wine wine) {
        List<String> noseList = List.of("오렌지", "시트러스", "건포도", "흙", "아몬드");

        return TastingNoteRequest.builder()
                .wineId(wine.getId())
                .color("레드")
                .tasteDate(LocalDate.parse("2025-01-01"))
                .sugarContent(10)
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