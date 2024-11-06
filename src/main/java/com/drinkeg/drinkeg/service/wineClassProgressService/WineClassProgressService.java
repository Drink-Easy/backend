package com.drinkeg.drinkeg.service.wineClassProgressService;

import com.drinkeg.drinkeg.domain.WineClassProgress;

import java.util.List;

public interface WineClassProgressService {
    public List<WineClassProgress> getAllWineClassProgressByMemberId(Long memberId);
    public WineClassProgress getWineClassProgressByMemberIdAndWineClassId(Long memberId, Long wineClassId);
}
