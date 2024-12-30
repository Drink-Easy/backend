package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TastingNotePreviewResponseDTO {

        private Long noteId;
        private String wineName;
        private String imageUrl;

        public static TastingNotePreviewResponseDTO create(Long noteId, String name, String imageUrl){
                return TastingNotePreviewResponseDTO.builder()
                        .noteId(noteId)
                        .wineName(name)
                        .imageUrl(imageUrl)
                        .build();
        }

}
