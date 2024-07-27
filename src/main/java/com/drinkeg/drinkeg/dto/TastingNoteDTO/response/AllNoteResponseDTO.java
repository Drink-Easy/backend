package com.drinkeg.drinkeg.dto.TastingNoteDTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllNoteResponseDTO {

    private List<NotePriviewResponseDTO> allNote;

}
