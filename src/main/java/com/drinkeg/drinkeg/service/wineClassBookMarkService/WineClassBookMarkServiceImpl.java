package com.drinkeg.drinkeg.service.wineClassBookMarkService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineClassBookMarkConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineClassBookMark;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.response.WineClassBookMarkResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.WineClassBookMarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WineClassBookMarkServiceImpl implements WineClassBookMarkService{
    private final WineClassBookMarkRepository wineClassBookMarkRepository;

    @Override
    public WineClassBookMarkResponseDTO saveWineClassBookMark(Member member, WineClass wineClass) {
        // 와인클래스 북마크 중복 확인
        if (wineClassBookMarkRepository.existsByMemberAndWineClass(member, wineClass))
            throw new GeneralException(ErrorStatus.WINE_CLASS_BOOKMARK_DUPLICATED);

        WineClassBookMark wineClassBookMark = WineClassBookMarkConverter.toWineClassBookMark(wineClass, member);
        wineClassBookMarkRepository.save(wineClassBookMark);

        return WineClassBookMarkConverter.toWineClassBookMarkResponseDTO(wineClassBookMark);
    }

    @Override
    public List<WineClassBookMarkResponseDTO> getWineClassBookMarksByUserId(Member member) {
        return wineClassBookMarkRepository.findAllByMember(member)
                .stream()
                .map(WineClassBookMarkConverter::toWineClassBookMarkResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWineClassBookMarkById(Member member, Long wineClassBookMarkId) {
        WineClassBookMark wineClassBookMark = wineClassBookMarkRepository.findById(wineClassBookMarkId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_BOOKMARK_NOT_FOUND));
        // 유저 권한 확인
        if (!wineClassBookMark.getMember().equals(member))
            throw new GeneralException(ErrorStatus.WINE_CLASS_BOOKMARK_UNAUTHORIZED);

        wineClassBookMarkRepository.deleteById(wineClassBookMarkId);
    }
}
