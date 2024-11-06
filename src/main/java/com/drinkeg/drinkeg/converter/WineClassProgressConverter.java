package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineClassProgress;

public class WineClassProgressConverter {
    public static WineClassProgress toWineClassProgress(WineClass wineClass, Member member, float progress) {
        return WineClassProgress.builder()
                .wineClass(wineClass)
                .member(member)
                .progress(progress)
                .build();
    }
}
