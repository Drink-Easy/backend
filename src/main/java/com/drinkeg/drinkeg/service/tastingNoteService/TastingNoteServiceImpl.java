package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.TastingNoteConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NotePreviewResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.repository.WineRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TastingNoteServiceImpl implements TastingNoteService {

    private final TastingNoteRepository tastingNoteRepository;
    private final WineRepository wineRepository;

    private final MemberService memberService;

    @Override
    public void saveTastingNote(NoteRequestDTO noteRequestDTO, PrincipalDetail principalDetail) {

        // 회원을 조회한다.
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        // 와인을 찾는다.
        Long wineId = noteRequestDTO.getWineId();
        Wine wine = wineRepository.findById(wineId).orElseThrow(()
                -> new GeneralException(ErrorStatus.WINE_NOT_FOUND)
        );

        // TastingNote를 저장한다.
        TastingNote tastingNoteEntity = TastingNoteConverter.toTastingNoteEntity(noteRequestDTO, member, wine);
        TastingNote savedNote = tastingNoteRepository.save(tastingNoteEntity);
    }

    @Override
    public NoteResponseDTO showTastingNoteById(Long noteId, PrincipalDetail principalDetail) {

        // 회원을 조회한다.
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        // noteId로 TastingNote를 찾는다.
        TastingNote foundNote = tastingNoteRepository.findById(noteId).orElseThrow(()
                -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

        // TastingNote의 Member가 요청한 Member와 같은지 확인한다.
        if(!foundNote.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_NOTE);
        }

        // TastingNote를 DTO로 변환한다.
        return TastingNoteConverter.toTastingNoteResponseDTO(foundNote);
    }

    @Override
    public AllNoteResponseDTO findAllTastingNote(PrincipalDetail principalDetail, String sort) {

        // 회원을 조회한다.
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        // Member의 TastingNote를 찾는다.
        List<TastingNote> foundNotes = member.getTastingNotes();

        int total = foundNotes.size();
        int red = (int) foundNotes.stream().filter((note) -> note.getWine().getSort().contains("레드")).count();
        int white = (int) foundNotes.stream().filter((note) -> note.getWine().getSort().contains("화이트")).count();
        int sparkling = (int) foundNotes.stream().filter((note) -> note.getWine().getSort().contains("스파클링")).count();
        int rose = (int) foundNotes.stream().filter((note) -> note.getWine().getSort().contains("로제")).count();
        int etc = total - (red + white + sparkling + rose);

        // 필터링된 노트를 TastingNotePreviewDTO로 변환
        List<NotePreviewResponseDTO> notePreviewResponseDTOList = foundNotes.stream()
                .filter(note -> filterBySort(note, sort))
                .sorted(Comparator.comparing(TastingNote::getCreatedAt).reversed())
                .map(TastingNoteConverter::toTastingNotePreviewDTO)
                .toList();

        return TastingNoteConverter
                .toAllNoteResponseDTO(notePreviewResponseDTOList, total, red, white, sparkling, rose, etc);
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
    public void updateTastingNote(Long noteId, NoteUpdateRequestDTO noteUpdateRequestDTO, PrincipalDetail principalDetail) {

        // 회원을 조회한다.
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        // noteId로 TastingNote를 찾는다.
        TastingNote foundNote = tastingNoteRepository.findById(noteId).orElseThrow(()
                -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

        // TastingNote의 Member가 요청한 Member와 같은지 확인한다.
        if(!foundNote.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_NOTE);
        }

        // TastingNote를 업데이트한다.
        if(noteUpdateRequestDTO.getWineId() != null) {
            Wine wine = wineRepository.findById(noteUpdateRequestDTO.getWineId()).orElseThrow(()
                    -> new GeneralException(ErrorStatus.WINE_NOT_FOUND)
            );
            foundNote.updateWine(wine);
        }
        if(noteUpdateRequestDTO.getColor() != null) {
            foundNote.updateColor(noteUpdateRequestDTO.getColor());
        }
        if(noteUpdateRequestDTO.getTastingDate() != null) {
            foundNote.updatetasteDate(noteUpdateRequestDTO.getTastingDate());
        }

        if(noteUpdateRequestDTO.getSugarContent() != null) {
            foundNote.updateSugarContent(noteUpdateRequestDTO.getSugarContent());
        }
        if(noteUpdateRequestDTO.getAcidity() != null) {
            foundNote.updateAcidity(noteUpdateRequestDTO.getAcidity());
        }
        if(noteUpdateRequestDTO.getTannin() != null) {
            foundNote.updateTannin(noteUpdateRequestDTO.getTannin());
        }
        if(noteUpdateRequestDTO.getBody() != null) {
            foundNote.updateBody(noteUpdateRequestDTO.getBody());
        }
        if(noteUpdateRequestDTO.getAlcohol() != null) {
            foundNote.updateAlcohol(noteUpdateRequestDTO.getAlcohol());
        }

        if(!noteUpdateRequestDTO.getScentAroma().isEmpty()) {
            foundNote.updateScentAroma(noteUpdateRequestDTO.getScentAroma());
        }
        if(!noteUpdateRequestDTO.getScentTaste().isEmpty()) {
            foundNote.updateScentTaste(noteUpdateRequestDTO.getScentTaste());
        }
        if(!noteUpdateRequestDTO.getScentFinish().isEmpty()) {
            foundNote.updateScentFinish(noteUpdateRequestDTO.getScentFinish());
        }

        if(noteUpdateRequestDTO.getSatisfaction() != null) {
            foundNote.updateSatisfaction(noteUpdateRequestDTO.getSatisfaction());
        }
        if(noteUpdateRequestDTO.getMemo() != null) {
            foundNote.updateMemo(noteUpdateRequestDTO.getMemo());
        }

        tastingNoteRepository.save(foundNote);
    }

    @Override
    public void deleteTastingNote(Long noteId, PrincipalDetail principalDetail) {

        // 회원을 조회한다.
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        // noteId로 TastingNote를 찾는다.
        TastingNote foundNote = tastingNoteRepository.findById(noteId).orElseThrow(() -> new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND)
        );

        // TastingNote의 Member가 요청한 Member와 같은지 확인한다.
        if(!foundNote.getMember().equals(member)) {
            throw new GeneralException(ErrorStatus.NOT_YOUR_NOTE);
        }

        // TastingNote를 삭제한다.
        tastingNoteRepository.delete(foundNote);
    }
}
