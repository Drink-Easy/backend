package com.drinkeg.drinkeg.service.wineClassProgressService;

import com.drinkeg.drinkeg.converter.WineClassConverter;
import com.drinkeg.drinkeg.converter.WineClassProgressConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineClassProgress;
import com.drinkeg.drinkeg.repository.WineClassProgressRepository;
import com.drinkeg.drinkeg.repository.WineLectureCompleteRepository;
import com.drinkeg.drinkeg.repository.WineLectureRepository;
import com.drinkeg.drinkeg.service.WineLectureCompleteService.WineLectureCompleteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WineClassProgressServiceImpl implements WineClassProgressService {
    private final WineClassProgressRepository wineClassProgressRepository;
    private final WineLectureCompleteService wineLectureCompleteService;

    @Override
    public float getProgress(WineClass wineClass, Member member) {
        WineClassProgress wineClassProgress = wineClassProgressRepository.findByWineClassAndMember(wineClass, member)
                .orElse(wineClassProgressRepository.save(WineClassProgressConverter.toWineClassProgress(wineClass, member, 0.0f)));

        return wineClassProgress.getProgress();
    }

    @Override
    public float updateProgress(WineClass wineClass, Member member) {
        WineClassProgress wineClassProgress = wineClassProgressRepository.findByWineClassAndMember(wineClass, member)
                .orElse(wineClassProgressRepository.save(WineClassProgressConverter.toWineClassProgress(wineClass, member, 0.0f)));

        float wineLectureCount = (float)wineClass.getWineLectures().stream().count();
        float wineLectureCompleteCount = (float)wineClass.getWineLectures().stream()
                .filter(wineLecture -> wineLectureCompleteService.isCompleted(wineLecture, member)).count();

        if (wineLectureCount == 0.0f)
            wineClassProgress.updateProgress(0.0f);
        else
            wineClassProgress.updateProgress(wineLectureCompleteCount / wineLectureCount);

        return wineClassProgress.getProgress();
    }
}
