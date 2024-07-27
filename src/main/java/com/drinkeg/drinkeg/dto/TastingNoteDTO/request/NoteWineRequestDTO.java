package com.drinkeg.drinkeg.dto.TastingNoteDTO.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteWineRequestDTO {

    private String wineName;

}
