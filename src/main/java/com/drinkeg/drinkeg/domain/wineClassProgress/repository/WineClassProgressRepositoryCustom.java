package com.drinkeg.drinkeg.domain.wineClassProgress.repository;

import java.util.Optional;

public interface WineClassProgressRepositoryCustom {
    Optional<Float> getProgress(Long wineClassId, Long memberId);
}
