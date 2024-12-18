package com.drinkeg.drinkeg.wineClassProgress.service;

public interface WineClassProgressService {
    public float getWineClassProgress(Long wineClassId, Long memberId);
    public void updateWineClassProgress(Long wineClassId, Long memberId);
}
