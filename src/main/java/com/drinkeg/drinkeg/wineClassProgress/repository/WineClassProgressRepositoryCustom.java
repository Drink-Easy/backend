package com.drinkeg.drinkeg.wineClassProgress.repository;

import java.util.Optional;

public interface WineClassProgressRepositoryCustom {
    Optional<Float> getProgress(Long wineClassId, Long memberId);
}
