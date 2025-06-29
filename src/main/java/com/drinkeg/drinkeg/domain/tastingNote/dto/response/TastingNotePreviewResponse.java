package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wine.wineVintage.domain.WineVintage;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TastingNotePreviewResponse {

        private Long noteId;
        private LocalDate tasteDate;
        private String wineName;
        private Integer vintageYear;
        private String imageUrl;
        private String sort;
        private LocalDate createdAt;

        @Builder
        public TastingNotePreviewResponse(Long noteId, LocalDate tasteDate, String wineName, Integer vintageYear,
                                          String imageUrl, String sort, LocalDate createdAt) {
                this.noteId = noteId;
                this.tasteDate = tasteDate;
                this.wineName = wineName;
                this.vintageYear = vintageYear;
                this.imageUrl = imageUrl;
                this.sort = sort;
                this.createdAt = createdAt;
        }

        public static TastingNotePreviewResponse from(TastingNote tastingNote){
                WineVintage wineVintage = tastingNote.getWineVintage();
                Integer vintageYear = wineVintage.getVintageYear() == 0 ? null
                        : wineVintage.getVintageYear();

                return TastingNotePreviewResponse.builder()
                        .noteId(tastingNote.getId())
                        .tasteDate(tastingNote.getTasteDate())
                        .wineName(wineVintage.getWine().getName())
                        .vintageYear(vintageYear)
                        .imageUrl(wineVintage.getWine().getImageUrl())
                        .sort(wineVintage.getWine().getSort())
                        .createdAt(LocalDate.from(tastingNote.getCreatedAt()))
                        .build();
        }

}
