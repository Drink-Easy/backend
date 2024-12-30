package com.drinkeg.drinkeg.domain.wineClassProgress.service;

import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wineClass.domain.WineClass;
import com.drinkeg.drinkeg.domain.wineClassProgress.domain.WineClassProgress;
import com.drinkeg.drinkeg.domain.wineClassProgress.repository.WineClassProgressRepository;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import com.drinkeg.drinkeg.domain.member.repostitory.MemberRepository;
import com.drinkeg.drinkeg.domain.wineClass.repository.WineClassRepository;
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

        float progress = wineClassProgressRepository.getProgress(wineClass.getId(), member.getId())
                .orElse(0.0f);

        wineClassProgress.updateProgress(progress);
    }
}
