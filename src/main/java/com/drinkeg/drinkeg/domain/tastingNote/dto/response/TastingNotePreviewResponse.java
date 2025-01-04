package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
