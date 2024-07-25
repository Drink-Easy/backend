package com.drinkeg.drinkeg.service.wineService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.TastingNoteConverter;
import com.drinkeg.drinkeg.converter.WineConverter;
import com.drinkeg.drinkeg.domain.TastingNote;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteWineRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.NoteWineResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.TastingNoteRepository;
import com.drinkeg.drinkeg.repository.WineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional
public class WineServiceImpl implements WineService {

    private final WineRepository wineRepository;
    private final TastingNoteRepository tastingNoteRepository;

    @Override
    public List<NoteWineResponseDTO> searchWinesByName(NoteWineRequestDTO noteWineRequestDTO) {

        // 와인 이름으로 와인을 검색한다.
        String searchName = noteWineRequestDTO.getWineName();
        List<Wine> exactMatchWines = wineRepository.findAllByName(searchName);

        // 와인 이름을 공백으로 나누어 검색한다.
        String[] keywords = searchName.split(" ");
        Set<Wine> searchWines = new LinkedHashSet<>(exactMatchWines);
        for(String keyword : keywords) {
            List<Wine> keywordContainingWines = wineRepository.findAllByNameContainingIgnoreCase(keyword);
            // 와인 이름이 포함된 와인을 추가한다.
            if(!keywordContainingWines.isEmpty()){
                searchWines.addAll(keywordContainingWines);
            }

        }

        // 와인을 NoteWineResponseDTO로 변환한다.
        List<NoteWineResponseDTO> collectWines = searchWines.stream().map(wine ->
                WineConverter.toNoteSearchWineDTO(wine)).collect(Collectors.toList());

        return collectWines;
    }
}
