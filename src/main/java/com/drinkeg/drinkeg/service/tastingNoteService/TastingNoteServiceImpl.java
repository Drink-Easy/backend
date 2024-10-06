package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.TastingNoteConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NotePriviewResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
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
    public void saveNote(PrincipalDetail principalDetail, NoteRequestDTO noteRequestDTO) {

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
    public NoteResponseDTO showNoteById(PrincipalDetail principalDetail, Long noteId) {

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
    public List<NotePriviewResponseDTO> findAllNote(PrincipalDetail principalDetail) {

        // 회원을 조회한다.
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        // Member의 TastingNote를 찾는다.
        List<TastingNote> foundNotes = member.getTastingNotes();

        // TastingNote를 최신 생성 순으로 정렬한 후, TastingNotePreviewDTO로 변환한다
        return foundNotes.stream()
                .sorted(Comparator.comparing(TastingNote::getCreatedAt).reversed())
                .map(TastingNoteConverter::toTastingNotePreviewDTO)
                .toList();
    }

    @Override
    public void updateTastingNote(PrincipalDetail principalDetail, Long noteId, NoteUpdateRequestDTO noteUpdateRequestDTO) {

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
    public void deleteTastingNote(PrincipalDetail principalDetail, Long noteId) {

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
