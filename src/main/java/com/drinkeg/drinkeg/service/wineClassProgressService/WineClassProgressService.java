package com.drinkeg.drinkeg.service.wineClassProgressService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;

public interface WineClassProgressService {
    public float getWineClassProgress(WineClass wineClass, Member member);
    public void updateWineClassProgress(WineClass wineClass, Member member);
}
