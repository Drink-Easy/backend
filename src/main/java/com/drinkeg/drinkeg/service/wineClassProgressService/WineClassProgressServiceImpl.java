package com.drinkeg.drinkeg.service.wineClassProgressService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineClassProgress;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.WineClassProgressRepository;
import com.drinkeg.drinkeg.service.WineLectureCompleteService.WineLectureCompleteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WineClassProgressServiceImpl implements WineClassProgressService {
    private final WineClassProgressRepository wineClassProgressRepository;
    private final WineLectureCompleteService wineLectureCompleteService;

    @Override
    public float getWineClassProgress(WineClass wineClass, Member member) {
        WineClassProgress wineClassProgress = wineClassProgressRepository.findByWineClassAndMember(wineClass, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_PROGRESS_NOT_FOUND));

        return wineClassProgress.getProgress();
    }

    @Override
    public void updateWineClassProgress(WineClass wineClass, Member member) {
        if (!wineClassProgressRepository.existsByWineClassAndMember(wineClass, member))
            wineClassProgressRepository.save(WineClassProgress.create(wineClass, member));

        WineClassProgress wineClassProgress = wineClassProgressRepository.findByWineClassAndMember(wineClass, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_PROGRESS_NOT_FOUND));
        // 진행도 계산
        long completedWineLectureCnt = wineClass.getWineLectures().stream().filter(wineLecture -> wineLectureCompleteService.isCompleted(wineLecture, member)).count();
        long wineLectureCnt = wineClass.getWineLectures().stream().count();
        float progress = (float)completedWineLectureCnt / (float)wineLectureCnt * 100.0f;

        wineClassProgress.updateProgress(progress);
    }
}
