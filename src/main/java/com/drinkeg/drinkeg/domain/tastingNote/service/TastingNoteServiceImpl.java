package com.drinkeg.drinkeg.domain.tastingNote.service;

import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNoteNose;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.request.TastingNoteUpdateRequest;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.AllTastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNotePreviewResponse;
import com.drinkeg.drinkeg.domain.tastingNote.dto.response.TastingNoteResponse;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteNoseRepository;
import com.drinkeg.drinkeg.domain.tastingNote.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.domain.wine.domain.Wine;
import com.drinkeg.drinkeg.domain.wine.repository.WineRepository;
import com.drinkeg.drinkeg.domain.wineNote.event.WineNoteUpdateEvent;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TastingNoteServiceImpl implements TastingNoteService {

    private final TastingNoteRepository tastingNoteRepository;
    private final TastingNoteNoseRepository tastingNoteNoseRepository;
    private final WineRepository wineRepository;
    private final MemberRepository memberRepository;

    private final ApplicationEventPublisher eventPublisher;


    @Override
    public void saveTastingNote(TastingNoteRequest tastingNoteRequest, String username) {

        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
        );

        // 와인을 찾는다.
        Long wineId = tastingNoteRequest.getWineId();
        Wine wine = wineRepository.findById(wineId).orElseThrow(
                () -> new GeneralException(ErrorStatus.WINE_NOT_FOUND));

        // TastingNote를 저장한다.
        tastingNoteRepository.save(TastingNote.create(member, wine, tastingNoteRequest));

        eventPublisher.publishEvent(new WineNoteUpdateEvent(wineId));
    }

    @Override
    public TastingNoteResponse showTastingNoteById(Long noteId, String username) {
        // noteId로 TastingNote를 찾는다.
        return tastingNoteRepository
                .findTastingNoteWithWineAndNoseByTastingNoteIdAndUsername(noteId, username)
                .orElseThrow(()
                -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

    }

    @Override
    public AllTastingNoteResponse findAllTastingNote(String sort, String username) {

        // username 을 이용해서 TastingNotes 조회
        // TastingNotes 조회 시 Wine, WineNose fetch join 하여 최적화
        List<TastingNote> foundNotes= tastingNoteRepository.findTastingNotesWithWineAndNoseByUsername(username);

        int total = foundNotes.size();
        int red = (int) foundNotes.stream().filter((note) -> note.getWine().getSort().contains("레드")).count();
        int white = (int) foundNotes.stream().filter((note) -> note.getWine().getSort().contains("화이트")).count();
        int sparkling = (int) foundNotes.stream().filter((note) -> note.getWine().getSort().contains("스파클링")).count();
        int rose = (int) foundNotes.stream().filter((note) -> note.getWine().getSort().contains("로제")).count();
        int etc = total - (red + white + sparkling + rose);

        // 필터링된 노트를 TastingNotePreviewDTO로 변환
        List<TastingNotePreviewResponse> tastingNotePreviewResponseList = foundNotes.stream()
                .filter(note -> filterBySort(note, sort))
                .sorted(Comparator.comparing(TastingNote::getCreatedAt).reversed())
                .map(note -> TastingNotePreviewResponse.create(note.getId(), note.getWine().getName(), note.getWine().getImageUrl()))
                .toList();

        return AllTastingNoteResponse.create(tastingNotePreviewResponseList, total, red, white, sparkling, rose, etc);
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
    public void updateTastingNote(Long noteId, TastingNoteUpdateRequest tastingNoteUpdateRequest, String username) {

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
        if(tastingNoteUpdateRequest.getColor() != null) {
            foundNote.updateColor(tastingNoteUpdateRequest.getColor());
        }
        if(tastingNoteUpdateRequest.getTastingDate() != null) {
            foundNote.updateTasteDate(tastingNoteUpdateRequest.getTastingDate());
        }

        if(tastingNoteUpdateRequest.getSugarContent() != null) {
            foundNote.updateSugarContent(tastingNoteUpdateRequest.getSugarContent());
        }
        if(tastingNoteUpdateRequest.getAcidity() != null) {
            foundNote.updateAcidity(tastingNoteUpdateRequest.getAcidity());
        }
        if(tastingNoteUpdateRequest.getTannin() != null) {
            foundNote.updateTannin(tastingNoteUpdateRequest.getTannin());
        }
        if(tastingNoteUpdateRequest.getBody() != null) {
            foundNote.updateBody(tastingNoteUpdateRequest.getBody());
        }
        if(tastingNoteUpdateRequest.getAlcohol() != null) {
            foundNote.updateAlcohol(tastingNoteUpdateRequest.getAlcohol());
        }

        List<String> addNoseList = tastingNoteUpdateRequest.getAddNoseList();
        if(!addNoseList.isEmpty()){
            for(String addNose: addNoseList){
                foundNote.addNoseElement(addNose);
            }
        }

        List<Long> removeNoseList = tastingNoteUpdateRequest.getRemoveNoseList();
        if(!removeNoseList.isEmpty()){
            for(Long removeNoseId: removeNoseList){
                removeNoseElement(removeNoseId);
            }
        }

        if(tastingNoteUpdateRequest.getRating() != null) {
            foundNote.updateRating(tastingNoteUpdateRequest.getRating());
        }
        if(tastingNoteUpdateRequest.getReview() != null) {
            foundNote.updateMemo(tastingNoteUpdateRequest.getReview());
        }

        tastingNoteRepository.save(foundNote);

    }

    @Override
    public void deleteTastingNote(Long noteId, String username) {

        // 회원을 조회한다.
        Member member = memberRepository.findByUsername(username).orElseThrow(
                () -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
        );

        // noteId로 TastingNote 를 찾는다.
        TastingNote foundNote = tastingNoteRepository.findTastingNoteWithNoseById(noteId).orElseThrow(
                () -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

        // TastingNote 의 Member 가 요청한 Member 와 같은지 확인한다.
        if(!foundNote.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN);
        }

        Long wineId = foundNote.getWine().getId();

        // TastingNote를 삭제한다.
        tastingNoteRepository.delete(foundNote);

        eventPublisher.publishEvent(new WineNoteUpdateEvent(wineId));
    }

    @Override
    public List<Map<Long, String>> showMemberNoseMapList(String username) {

        // 사용자가 작성한 TastingNoteNose 리스트 가져오기
        List<TastingNoteNose> tastingNoteNoseList = tastingNoteNoseRepository.getTastingNoteNoseListByUsername(username);

        // TastingNoteNose 리스트를 Map<Long, String>으로 변환
        return tastingNoteNoseList.stream()
                .map(nose -> Map.of(nose.getId(), nose.getNoseElement()))
                .collect(Collectors.toList());

    }

    private void removeNoseElement(Long noseElementId){
        tastingNoteNoseRepository.deleteById(noseElementId);
    }
}
