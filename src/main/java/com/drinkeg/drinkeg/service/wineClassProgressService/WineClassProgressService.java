package com.drinkeg.drinkeg.service.wineClassProgressService;

public interface WineClassProgressService {
    public float getProgress(Long wineClassId, Long memberId);
    public float updateProgress(Long wineClassId, Long memberId);
}
