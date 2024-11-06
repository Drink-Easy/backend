package com.drinkeg.drinkeg.service.wineClassProgressService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;

public interface WineClassProgressService {
    public float getProgress(WineClass wineClass, Member member);
    public float updateProgress(WineClass wineClass, Member member);
}
