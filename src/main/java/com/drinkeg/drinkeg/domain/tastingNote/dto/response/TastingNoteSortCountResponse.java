package com.drinkeg.drinkeg.domain.tastingNote.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TastingNoteSortCountResponse {

    private long totalCount;     // 전체 TastingNote 개수
    private long redCount;       // 레드 와인 개수
    private long whiteCount;     // 화이트 와인 개수
    private long sparklingCount; // 스파클링 와인 개수
    private long roseCount;      // 로제 와인 개수
    private long etcCount;       // 기타 와인 개수

    public TastingNoteSortCountResponse(Long totalCount, Long redCount, Long whiteCount, Long sparklingCount, Long roseCount, Long etcCount) {
        this.totalCount = totalCount != null ? totalCount : 0;
        this.redCount = redCount != null ? redCount : 0;
        this.whiteCount = whiteCount != null ? whiteCount : 0;
        this.sparklingCount = sparklingCount != null ? sparklingCount : 0;
        this.roseCount = roseCount != null ? roseCount : 0;
        this.etcCount = etcCount != null ? etcCount : 0;
    }
}
