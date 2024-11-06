package com.drinkeg.drinkeg.service.wineClassProgressService;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineClassProgress;
import com.drinkeg.drinkeg.repository.WineClassProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WineClassProgressServiceImpl implements WineClassProgressService {
    private final WineClassProgressRepository wineClassProgressRepository;

    @Override
    public float getWineClassProgress(Long memberId, Long wineClassId) {
        WineClassProgress wineClassProgress = wineClassProgressRepository.findByMemberIdAndWineClassId(memberId, wineClassId)
                .orElse(wineClassProgressRepository.saveByMemberIdAndWineClassIdAndProgress(memberId, wineClassId, 0.0f));
        return wineClassProgress.getProgress();
    }
}
