package com.drinkeg.drinkeg.service.wineClassProgressService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineClassProgress;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;

import com.drinkeg.drinkeg.repository.wineClass.WineClassRepository;
import com.drinkeg.drinkeg.repository.wineClassProgress.WineClassProgressRepository;
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
