package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.TastingNoteConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TastingNoteServiceImpl implements TastingNoteService {

    private final TastingNoteRepository tastingNoteRepository;
    private final WineRepository wineRepository;
    private final MemberRepository memberRepository;

    @Override
    public void saveNote(NoteRequestDTO noteRequestDTO) {

        // 와인을 찾는다.
        Long wineId = noteRequestDTO.getWineId();
        Wine wine = wineRepository.findById(wineId).orElseThrow(()-> {
                    throw new GeneralException(ErrorStatus.WINE_NOT_FOUND);
                }
        );

        // TastingNote를 저장한다.
        TastingNote tastingNoteEntity = TastingNoteConverter.toTastingNoteEntity(noteRequestDTO, wine);
        TastingNote savedNote = tastingNoteRepository.save(tastingNoteEntity);
    }

    @Override
    public NoteResponseDTO showNoteById(Long noteId) {

        // noteId로 TastingNote를 찾는다.
        TastingNote foundNote = tastingNoteRepository.findById(noteId).orElseThrow(() -> {
                    throw new GeneralException(ErrorStatus.TASTING_NOTE_NOT_FOUND);
                }
        );

        // TastingNote를 DTO로 변환한다.
        NoteResponseDTO tastingNoteResponseDTO = TastingNoteConverter.toTastingNoteResponseDTO(foundNote);

        return tastingNoteResponseDTO;
    }

    @Override
    public AllNoteResponseDTO findAllNoteByMember(Member member) {

        // Member의 TastingNote를 찾는다.
        List<TastingNote> foundNotes = member.getTastingNotes();

        // TastingNote를 NotePreviewDTO로 변환한 후 AllNoteResponseDTO로 변환한다.
        AllNoteResponseDTO allNoteResponseDTO = AllNoteResponseDTO.builder()
                .allNote(
                        foundNotes.stream().map(foundNote ->
                                TastingNoteConverter.toTastingNotePreviewDTO(foundNote)).toList())

                .build();

        return allNoteResponseDTO;
    }
}
