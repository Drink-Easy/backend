package com.drinkeg.drinkeg.dto.TastingNoteDTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotePreviewResponseDTO {

        private Long noteId;

        private String name;

        private String imageUrl;

}
