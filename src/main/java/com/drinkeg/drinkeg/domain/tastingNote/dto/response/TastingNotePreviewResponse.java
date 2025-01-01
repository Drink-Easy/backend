package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TastingNotePreviewResponse {

        private Long noteId;
        private String wineName;
        private String imageUrl;

        public static TastingNotePreviewResponse create(Long noteId, String name, String imageUrl){
                return TastingNotePreviewResponse.builder()
                        .noteId(noteId)
                        .wineName(name)
                        .imageUrl(imageUrl)
                        .build();
        }

}
