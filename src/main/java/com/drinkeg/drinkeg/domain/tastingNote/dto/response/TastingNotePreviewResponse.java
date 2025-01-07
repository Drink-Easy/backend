package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
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

        public static TastingNotePreviewResponse of(TastingNote tastingNote){
                return TastingNotePreviewResponse.builder()
                        .noteId(tastingNote.getId())
                        .wineName(tastingNote.getWine().getName())
                        .imageUrl(tastingNote.getWine().getImageUrl())
                        .sort(tastingNote.getWine().getSort())
                        .build();
        }

}
