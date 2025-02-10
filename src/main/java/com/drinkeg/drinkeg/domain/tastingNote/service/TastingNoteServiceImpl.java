package com.drinkeg.drinkeg.domain.tastingNote.service;

import com.drinkeg.drinkeg.domain.tastingNote.event.TastingNoteUpdateEvent;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.controller.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.controller.request.TastingNoteUpdateRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNotePreviewResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
        );

        Long wineId = tastingNoteRequest.getWineId();
        Wine wine = wineRepository.findById(wineId).orElseThrow(
                () -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        TastingNote save = tastingNoteRepository.save(TastingNote.create(member, wine, tastingNoteRequest));

        eventPublisher.publishEvent(new TastingNoteUpdateEvent(wineId));

        return save.getId();
    }

    @Override
    public TastingNoteResponse showTastingNoteByIdAndUsername(Long noteId, String username) {
        TastingNote tastingNote = tastingNoteRepository.findById(noteId).orElseThrow(
                () -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND));

        if(!tastingNote.getMember().getUsername().equals(username))
            throw new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN);

        return TastingNoteResponse.of(tastingNote);
    }

    @Override
    public AllTastingNoteResponse findAllTastingNote(TastingNoteWineSort wineSort, String username, Pageable pageable) {
        TastingNoteSortCountResponse tastingNoteSortCountResponse = tastingNoteRepository.findTastingNoteSortCountsByUsername(username);

        List<TastingNotePreviewResponse> tastingNotePreviewResponseList = tastingNoteRepository.findTastingNoteBySortAndUsername(wineSort, username, pageable)
                .stream()
                .map(TastingNotePreviewResponse::of)
                .toList();
        long total = tastingNoteRepository.countTastingNoteBySortAndUsername(wineSort, username);

        PageResponse pageResponse = PageResponse.of(new PageImpl<>(tastingNotePreviewResponseList, pageable, total));

        return AllTastingNoteResponse.create(tastingNoteSortCountResponse, pageResponse);
    }

    @Override
    public void updateTastingNote(Long noteId, TastingNoteUpdateRequest t, String username) {

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
        );

        TastingNote foundNote = tastingNoteRepository.findById(noteId).orElseThrow(()
                -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

        if(!foundNote.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN);
        }

        foundNote.updateTastingNote(t.getColor(), t.getTastingDate(),
                t.getSweetness(), t.getAcidity(), t.getTannin(), t.getBody(), t.getAlcohol(),
                t.getUpdateNoseList(), t.getRating(), t.getReview());
    }

    @Override
    public Long deleteTastingNote(Long noteId, String username) {

        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
        );

        TastingNote foundNote = tastingNoteRepository.findById(noteId).orElseThrow(
                () -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

        Member foundNoteMember = foundNote.getMember();

        if(foundNoteMember == null || !foundNoteMember.equals(member)) {
            throw new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN);
        }

        Long wineId = foundNote.getWine().getId();
        tastingNoteRepository.delete(foundNote);

        eventPublisher.publishEvent(new TastingNoteUpdateEvent(wineId));

        return noteId;
    }

    @Override
    public void setTastingNoteMemberNull(String username) {
        tastingNoteRepository.updateTastingNoteMemberNull(username);
    }

    @Override
    public PageResponse<TastingNotePreviewResponse> searchTastingNoteByWineName(String searchName, String username, Pageable pageable) {
        String cleanSearchName = searchName.replaceAll("[ ,.'\\\\]", "").toLowerCase();
        List<TastingNotePreviewResponse> tastingNotePreviewResponseList = tastingNoteRepository.searchTastingNoteByWineName(cleanSearchName, username, pageable).
                stream()
                .map(TastingNotePreviewResponse::of)
                .toList();
        Long total = tastingNoteRepository.countSearchTastingNoteByWineName(cleanSearchName.toLowerCase(), username);

        return PageResponse.of(new PageImpl<>(tastingNotePreviewResponseList, pageable, total));
    }
}
