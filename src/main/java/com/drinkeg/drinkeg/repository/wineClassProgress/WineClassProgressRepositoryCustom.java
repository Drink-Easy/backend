package com.drinkeg.drinkeg.repository.wineClassProgress;

import java.util.Optional;

public interface WineClassProgressRepositoryCustom {
    Optional<Float> getProgress(Long wineClassId, Long memberId);
}
