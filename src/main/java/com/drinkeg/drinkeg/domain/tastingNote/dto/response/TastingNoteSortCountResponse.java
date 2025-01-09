package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TastingNoteSortCountResponse {

    private int totalCount;     // 전체 TastingNote 개수
    private int redCount;       // 레드 와인 개수
    private int whiteCount;     // 화이트 와인 개수
    private int sparklingCount; // 스파클링 와인 개수
    private int roseCount;      // 로제 와인 개수
    private int etcCount;       // 기타 와인 개수

    @QueryProjection
    public TastingNoteSortCountResponse(int totalCount, int redCount, int whiteCount, int sparklingCount, int roseCount, int etcCount) {
        this.totalCount = totalCount;
        this.redCount = redCount;
        this.whiteCount = whiteCount;
        this.sparklingCount = sparklingCount;
        this.roseCount = roseCount;
        this.etcCount = etcCount;
    }
}
