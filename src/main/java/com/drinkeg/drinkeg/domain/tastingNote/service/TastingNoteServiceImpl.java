package com.drinkeg.drinkeg.domain.tastingNote.service;

import com.drinkeg.drinkeg.domain.tastingNote.event.TastingNoteUpdateEvent;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteUpdateRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNotePreviewResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TastingNoteServiceImpl implements TastingNoteService {

    private final TastingNoteRepository tastingNoteRepository;
    private final WineRepository wineRepository;
    private final MemberRepository memberRepository;

    private final ApplicationEventPublisher eventPublisher;


    @Override
    public Long saveTastingNote(TastingNoteRequest tastingNoteRequest, String username) {

        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
        );

        // 와인을 찾는다.
        Long wineId = tastingNoteRequest.getWineId();
        Wine wine = wineRepository.findById(wineId).orElseThrow(
                () -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        // TastingNote를 저장한다.
        TastingNote save = tastingNoteRepository.save(TastingNote.create(member, wine, tastingNoteRequest));

        eventPublisher.publishEvent(new TastingNoteUpdateEvent(wineId));

        return save.getId();
    }

    @Override
    public TastingNoteResponse showTastingNoteByIdAndUsername(Long noteId, String username) {
        TastingNote tastingNote = tastingNoteRepository.findById(noteId).orElseThrow(()
                -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

        if(!tastingNote.getMember().getUsername().equals(username))
            throw new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN);

        return TastingNoteResponse.of(tastingNote);
    }

    @Override
    public AllTastingNoteResponse findAllTastingNote(TastingNoteWineSort wineSort, String username) {
        List<TastingNote> tastingNoteList = tastingNoteRepository.findTastingNoteBySortAndUsername(wineSort, username);
        TastingNoteSortCountResponse tastingNoteSortCountResponse = tastingNoteRepository.findTastingNoteSortCountsByUsername(username);

        List<TastingNotePreviewResponse> tastingNotePreviewResponseList = tastingNoteList.stream()
                .map(TastingNotePreviewResponse::of)
                .toList();

        return AllTastingNoteResponse.create(tastingNoteSortCountResponse, tastingNotePreviewResponseList);
    }

    // 와인 타입별 필터링 로직
    private boolean filterBySort(TastingNote note, String sort) {
        String wineSort = note.getWine().getSort();

        switch (sort) {
            case "red":
                return wineSort.contains("레드");
            case "white":
                return wineSort.contains("화이트");
            case "sparkling":
                return wineSort.contains("스파클링");
            case "rose":
                return wineSort.contains("로제");
            case "all":
                return true; // 전체 보기
            default:
                return !wineSort.contains("레드") && !wineSort.contains("화이트")
                        && !wineSort.contains("스파클링") && !wineSort.contains("로제");
        }
    }

    @Override
    public void updateTastingNote(Long noteId, TastingNoteUpdateRequest t, String username) {

        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
        );

        // noteId로 TastingNote를 찾는다.
        TastingNote foundNote = tastingNoteRepository.findById(noteId).orElseThrow(()
                -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

        // TastingNote의 Member가 요청한 Member와 같은지 확인한다.
        if(!foundNote.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN);
        }

        // TastingNote를 업데이트한다.
        foundNote.updateTastingNote(t.getColor(), t.getTastingDate(),
                t.getSugarContent(), t.getAcidity(), t.getTannin(), t.getBody(), t.getAlcohol(),
                t.getUpdateNoseList(), t.getRating(), t.getReview());

        tastingNoteRepository.save(foundNote);

    }

    @Override
    public Long deleteTastingNote(Long noteId, String username) {

        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
        );

        // noteId로 TastingNote 를 찾는다.
        TastingNote foundNote = tastingNoteRepository.findById(noteId).orElseThrow(
                () -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

        // TastingNote 의 Member 가 요청한 Member 와 같은지 확인한다.
        if(!foundNote.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN);
        }

        Long wineId = foundNote.getWine().getId();

        // TastingNote를 삭제한다.
        tastingNoteRepository.delete(foundNote);

        eventPublisher.publishEvent(new TastingNoteUpdateEvent(wineId));

        return noteId;
    }

    // 회원 탈퇴 시 탈퇴한 회원의 테이스팅 노트의 member_id null 로 설정
    @Override
    public void setTastingNoteMemberNull(String username) {
        tastingNoteRepository.updateTastingNoteMemberNull(username);
    }
}
