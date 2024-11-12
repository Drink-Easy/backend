package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.TastingNoteConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.TastingNoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.TastingNoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllTastingNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.TastingNotePreviewResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.TastingNoteResponseDTO;
import com.drinkeg.drinkeg.event.wineNoteEvent.WineNoteUpdateEvent;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.repository.TastingNoteNoseRepository;
import com.drinkeg.drinkeg.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.wineService.WineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TastingNoteServiceImpl implements TastingNoteService {

    private final TastingNoteRepository tastingNoteRepository;
    private final TastingNoteNoseRepository tastingNoteNoseRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final MemberService memberService;
    private final WineService wineService;
    private final MemberRepository memberRepository;

    @Override
    public void saveTastingNote(TastingNoteRequestDTO tastingNoteRequestDTO, PrincipalDetail principalDetail) {

        // 회원을 조회한다.
        Member member = memberService.loadMemberWithTastingNoteByPrincipalDetail(principalDetail);

        // 와인을 찾는다.
        Long wineId = tastingNoteRequestDTO.getWineId();
        Wine wine = wineService.findWineById(wineId);

        // TastingNote를 저장한다.
        com.drinkeg.drinkeg.domain.TastingNote tastingNote = tastingNoteRepository
                .save(TastingNoteConverter.toTastingNoteEntity(tastingNoteRequestDTO, member, wine));

        eventPublisher.publishEvent(new WineNoteUpdateEvent(wineId));
    }

    @Override
    public  TastingNoteResponseDTO showTastingNoteById(Long noteId, PrincipalDetail principalDetail) {
        // noteId로 TastingNote를 찾는다.
        TastingNote foundNote = tastingNoteRepository.findByIdWithWineAndNose(noteId).orElseThrow(()
                -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

        // TastingNote의 Member가 요청한 Member와 같은지 확인한다.
        if(!foundNote.getMember().getUsername().equals(principalDetail.getUsername())) {
            throw new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN);
        }

        // TastingNote를 DTO로 변환한다.
        return TastingNoteConverter.toTastingNoteResponseDTO(foundNote);
    }

    @Override
    public AllTastingNoteResponseDTO findAllTastingNote(String sort, PrincipalDetail principalDetail) {

        // 회원을 조회한다.
        Member member = memberService.loadMemberWithTastingNoteByPrincipalDetail(principalDetail);

        // Member의 TastingNote를 찾는다.
        List<com.drinkeg.drinkeg.domain.TastingNote> foundNotes = member.getTastingNotes();

        int total = foundNotes.size();
        int red = (int) foundNotes.stream().filter((note) -> note.getWine().getSort().contains("레드")).count();
        int white = (int) foundNotes.stream().filter((note) -> note.getWine().getSort().contains("화이트")).count();
        int sparkling = (int) foundNotes.stream().filter((note) -> note.getWine().getSort().contains("스파클링")).count();
        int rose = (int) foundNotes.stream().filter((note) -> note.getWine().getSort().contains("로제")).count();
        int etc = total - (red + white + sparkling + rose);

        // 필터링된 노트를 TastingNotePreviewDTO로 변환
        List<TastingNotePreviewResponseDTO> tastingNotePreviewResponseDTOList = foundNotes.stream()
                .filter(note -> filterBySort(note, sort))
                .sorted(Comparator.comparing(com.drinkeg.drinkeg.domain.TastingNote::getCreatedAt).reversed())
                .map(TastingNoteConverter::toTastingNotePreviewDTO)
                .toList();

        return TastingNoteConverter
                .toAllNoteResponseDTO(tastingNotePreviewResponseDTOList, total, red, white, sparkling, rose, etc);
    }

    // 와인 타입별 필터링 로직
    private boolean filterBySort(com.drinkeg.drinkeg.domain.TastingNote note, String sort) {
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
    public void updateTastingNote(Long noteId, TastingNoteUpdateRequestDTO tastingNoteUpdateRequestDTO, PrincipalDetail principalDetail) {

        // 회원을 조회한다.
        Member member = memberService.loadMemberWithTastingNoteByPrincipalDetail(principalDetail);

        // noteId로 TastingNote를 찾는다.
        com.drinkeg.drinkeg.domain.TastingNote foundNote = tastingNoteRepository.findById(noteId).orElseThrow(()
                -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

        // TastingNote의 Member가 요청한 Member와 같은지 확인한다.
        if(!foundNote.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN);
        }

        // TastingNote를 업데이트한다.
        if(tastingNoteUpdateRequestDTO.getColor() != null) {
            foundNote.updateColor(tastingNoteUpdateRequestDTO.getColor());
        }
        if(tastingNoteUpdateRequestDTO.getTastingDate() != null) {
            foundNote.updatetasteDate(tastingNoteUpdateRequestDTO.getTastingDate());
        }

        if(tastingNoteUpdateRequestDTO.getSugarContent() != null) {
            foundNote.updateSugarContent(tastingNoteUpdateRequestDTO.getSugarContent());
        }
        if(tastingNoteUpdateRequestDTO.getAcidity() != null) {
            foundNote.updateAcidity(tastingNoteUpdateRequestDTO.getAcidity());
        }
        if(tastingNoteUpdateRequestDTO.getTannin() != null) {
            foundNote.updateTannin(tastingNoteUpdateRequestDTO.getTannin());
        }
        if(tastingNoteUpdateRequestDTO.getBody() != null) {
            foundNote.updateBody(tastingNoteUpdateRequestDTO.getBody());
        }
        if(tastingNoteUpdateRequestDTO.getAlcohol() != null) {
            foundNote.updateAlcohol(tastingNoteUpdateRequestDTO.getAlcohol());
        }

        List<String> addNoseList = tastingNoteUpdateRequestDTO.getAddNoseList();
        if(!addNoseList.isEmpty()){
            for(String addNose: addNoseList){
                foundNote.addNoseElement(addNose);
            }
        }

        List<Long> removeNoseList = tastingNoteUpdateRequestDTO.getRemoveNoseList();
        if(!removeNoseList.isEmpty()){
            for(Long removeNoseId: removeNoseList){
                removeNoseElement(removeNoseId);
            }
        }

        if(tastingNoteUpdateRequestDTO.getSatisfaction() != null) {
            foundNote.updateSatisfaction(tastingNoteUpdateRequestDTO.getSatisfaction());
        }
        if(tastingNoteUpdateRequestDTO.getReview() != null) {
            foundNote.updateMemo(tastingNoteUpdateRequestDTO.getReview());
        }

        tastingNoteRepository.save(foundNote);

    }

    @Override
    public void deleteTastingNote(Long noteId, PrincipalDetail principalDetail) {

        // 회원을 조회한다.
        Member member = memberService.loadMemberWithTastingNoteByPrincipalDetail(principalDetail);

        // noteId로 TastingNote를 찾는다.
        com.drinkeg.drinkeg.domain.TastingNote foundNote = tastingNoteRepository.findById(noteId).orElseThrow(()
                -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

        // TastingNote의 Member가 요청한 Member와 같은지 확인한다.
        if(!foundNote.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.TASTING_NOTE_FORBIDDEN);
        }

        Long wineId = foundNote.getWine().getId();

        // TastingNote를 삭제한다.
        tastingNoteRepository.delete(foundNote);

        eventPublisher.publishEvent(new WineNoteUpdateEvent(wineId));
    }

    private void removeNoseElement(Long noseElementId){
        tastingNoteNoseRepository.deleteById(noseElementId);
    }
}
