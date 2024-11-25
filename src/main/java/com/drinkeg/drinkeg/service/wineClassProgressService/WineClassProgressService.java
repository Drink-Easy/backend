package com.drinkeg.drinkeg.service.wineClassProgressService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;

public interface WineClassProgressService {
    public float getWineClassProgress(Long wineClassId, Long memberId);
    public void updateWineClassProgress(Long wineClassId, Long memberId);
}
