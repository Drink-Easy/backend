package com.drinkeg.drinkeg.domain.tastingNote.service;

import com.drinkeg.drinkeg.domain.tastingNote.event.WineNoteUpdateEvent;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteWineSort;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteSortCountResponse;
import com.drinkeg.drinkeg.domain.tastingNote.event.WineStatisticsEvent;
import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;
import com.drinkeg.drinkeg.domain.wine.wineVintage.repository.WineVintageRepository;
import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteUpdateRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNotePreviewResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.global.dto.PageResponse;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
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
    private final WineVintageRepository wineVintageRepository;
    private final MemberRepository memberRepository;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Long saveTastingNote(TastingNoteRequest tastingNoteRequest, String username) {

        Member member = findMemberByUsername(username);
        WineVintage wineVintage = findWineVintageByWineIdAndVintageYear(
                tastingNoteRequest.getWineId(),
                tastingNoteRequest.getVintageYear() == null ? 0 : tastingNoteRequest.getVintageYear()
        );

        TastingNote save = tastingNoteRepository.save(TastingNote.create(member, wineVintage, tastingNoteRequest));
        Long wineId = wineVintage.getWine().getId();

        publishWineAndWineVintageNoteUpdateEvent(wineVintage.getId(), wineId);

        return save.getId();
    }

    @Override
    public TastingNoteResponse showTastingNoteByIdAndUsername(Long noteId, String username) {
        Member member = findMemberByUsername(username);
        TastingNote tastingNote = findTastingNoteById(noteId);
        validateTastingNoteOwnership(tastingNote, member);
        return TastingNoteResponse.from(tastingNote);
    }

    @Override
    public AllTastingNoteResponse findAllTastingNote(TastingNoteWineSort wineSort, String username, Pageable pageable) {
        TastingNoteSortCountResponse tastingNoteSortCountResponse = tastingNoteRepository.findTastingNoteSortCountsByUsername(username);

        List<TastingNotePreviewResponse> tastingNotePreviewResponseList = tastingNoteRepository.findTastingNoteBySortAndUsername(wineSort, username, pageable)
                .stream()
                .map(TastingNotePreviewResponse::from)
                .toList();
        long total = tastingNoteRepository.countTastingNoteBySortAndUsername(wineSort, username);

        PageResponse pageResponse = PageResponse.of(new PageImpl<>(tastingNotePreviewResponseList, pageable, total));

        return AllTastingNoteResponse.create(tastingNoteSortCountResponse, pageResponse);
    }

    @Override
    public void updateTastingNote(Long noteId, TastingNoteUpdateRequest t, String username) {

        Member member = findMemberByUsername(username);
        TastingNote foundNote = findTastingNoteById(noteId);
        validateTastingNoteOwnership(foundNote, member);

        foundNote.updateTastingNote(t.getColor(), t.getTastingDate(),
                t.getSweetness(), t.getAcidity(), t.getTannin(), t.getBody(), t.getAlcohol(),
                t.getUpdateNoseList(), t.getRating(), t.getReview());

        WineVintage wineVintage = foundNote.getWineVintage();
        publishWineAndWineVintageNoteUpdateEvent(wineVintage.getId(), wineVintage.getWine().getId());
    }

    @Override
    public Long deleteTastingNote(Long noteId, String username) {

        Member member = findMemberByUsername(username);
        TastingNote foundNote = findTastingNoteById(noteId);
        validateTastingNoteOwnership(foundNote, member);

        WineVintage wineVintage = foundNote.getWineVintage();
        Long wineVintageId = wineVintage.getId();
        Long wineId = wineVintage.getWine().getId();

        tastingNoteRepository.delete(foundNote);
        publishWineAndWineVintageNoteUpdateEvent(wineVintageId, wineId);
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
                .map(TastingNotePreviewResponse::from)
                .toList();
        Long total = tastingNoteRepository.countSearchTastingNoteByWineName(cleanSearchName, username);

        return PageResponse.of(new PageImpl<>(tastingNotePreviewResponseList, pageable, total));
    }

    private Member findMemberByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    private WineVintage findWineVintageByWineIdAndVintageYear(Long wineId, int vintageYear) {
        return wineVintageRepository.findByWineIdAndVintageYear(wineId, vintageYear)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_VINTAGE_NOT_FOUND));
    }

    private TastingNote findTastingNoteById(Long noteId) {
        return tastingNoteRepository.findById(noteId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND));
    }

    private void validateTastingNoteOwnership(TastingNote tastingNote, Member member) {
        if (!tastingNote.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN);
        }
    }

    private void publishWineAndWineVintageNoteUpdateEvent(Long wineVintageId, Long wineId) {
        eventPublisher.publishEvent(new WineStatisticsEvent(wineVintageId));
        eventPublisher.publishEvent(new WineNoteUpdateEvent(wineId));
    }
}
