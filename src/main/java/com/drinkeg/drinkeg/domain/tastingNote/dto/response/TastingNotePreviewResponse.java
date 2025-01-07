package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TastingNotePreviewResponse {

        private Long noteId;
        private String wineName;
        private String imageUrl;
        private String sort;

        @Builder
        public TastingNotePreviewResponse(Long noteId, String wineName, String imageUrl, String sort){
                this.noteId = noteId;
                this.wineName = wineName;
                this.imageUrl = imageUrl;
                this.sort = sort;
        }

        public static TastingNotePreviewResponse create(Long noteId, String name, String imageUrl, String sort){
                return TastingNotePreviewResponse.builder()
                        .noteId(noteId)
                        .wineName(name)
                        .imageUrl(imageUrl)
                        .sort(sort)
                        .build();
        }

}
