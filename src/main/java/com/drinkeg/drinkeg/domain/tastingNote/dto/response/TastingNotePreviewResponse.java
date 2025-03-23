package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TastingNotePreviewResponse {

        private Long noteId;
        private LocalDate tasteDate;
        private String wineName;
        private String imageUrl;
        private String sort;
        private LocalDate createdAt;

        @Builder
        public TastingNotePreviewResponse(Long noteId, LocalDate tasteDate, String wineName,
                                          String imageUrl, String sort, LocalDate createdAt) {
                this.noteId = noteId;
                this.tasteDate = tasteDate;
                this.wineName = wineName;
                this.imageUrl = imageUrl;
                this.sort = sort;
                this.createdAt = createdAt;
        }

        public static TastingNotePreviewResponse of(TastingNote tastingNote){
                return TastingNotePreviewResponse.builder()
                        .noteId(tastingNote.getId())
                        .tasteDate(tastingNote.getTasteDate())
                        .wineName(tastingNote.getWine().getName())
                        .imageUrl(tastingNote.getWine().getImageUrl())
                        .sort(tastingNote.getWine().getSort())
                        .createdAt(LocalDate.from(tastingNote.getCreatedAt()))
                        .build();
        }

}
