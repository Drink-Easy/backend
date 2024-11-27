package com.drinkeg.drinkeg.wineClassProgress.service;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.wineClass.domain.WineClass;
import com.drinkeg.drinkeg.wineClass.domain.WineClassProgress;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.wineClass.repository.WineClassRepository;
import com.drinkeg.drinkeg.wineClassProgress.repository.WineClassProgressRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class WineClassProgressServiceImpl implements WineClassProgressService {
    private final WineClassProgressRepository wineClassProgressRepository;
    private final WineClassRepository wineClassRepository;
    private final MemberRepository memberRepository;

    @Override
    public float getWineClassProgress(Long wineClassId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        WineClass wineClass = wineClassRepository.findById(wineClassId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND));

        if (!wineClassProgressRepository.existsByWineClassAndMember(wineClass, member))
            wineClassProgressRepository.save(WineClassProgress.create(wineClass, member));

        WineClassProgress wineClassProgress = wineClassProgressRepository.findByWineClassAndMember(wineClass, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_PROGRESS_NOT_FOUND));

        return wineClassProgress.getProgress();
    }

    @Override
    public void updateWineClassProgress(Long wineClassId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        WineClass wineClass = wineClassRepository.findById(wineClassId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_NOT_FOUND));

        if (!wineClassProgressRepository.existsByWineClassAndMember(wineClass, member))
            wineClassProgressRepository.save(WineClassProgress.create(wineClass, member));

        WineClassProgress wineClassProgress = wineClassProgressRepository.findByWineClassAndMember(wineClass, member)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_PROGRESS_NOT_FOUND));

        wineClassProgress.updateProgress(wineClassProgressRepository.getProgress(wineClassId, memberId));
    }
}
