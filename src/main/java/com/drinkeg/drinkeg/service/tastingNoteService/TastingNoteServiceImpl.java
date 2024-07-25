package com.drinkeg.drinkeg.service.tastingNoteService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.TastingNoteConverter;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TastingNoteServiceImpl implements TastingNoteService {

    private final TastingNoteRepository tastingNoteRepository;
    private final WineRepository wineRepository;

    @Override
    public void saveNote(NoteRequestDTO noteRequestDTO) {

        // 와인을 찾는다.
        Long wineId = noteRequestDTO.getWineId();
        Wine wine = wineRepository.findById(wineId).orElseThrow(()-> {
                    throw new GeneralException(ErrorStatus.Wine_NOT_FOUND);
                }
        );

        // TastingNote를 저장한다.
        TastingNote tastingNoteEntity = TastingNoteConverter.toTastingNoteEntity(noteRequestDTO, wine);
        TastingNote savedNote = tastingNoteRepository.save(tastingNoteEntity);
    }
}
