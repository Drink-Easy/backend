package com.drinkeg.drinkeg.service.wineClassBookMarkService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineClassBookMarkConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineClassBookMark;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.request.WineClassBookMarkRequestDTO;
import com.drinkeg.drinkeg.dto.WineClassBookMarkDTO.response.WineClassBookMarkResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.repository.WineClassBookMarkRepository;
import com.drinkeg.drinkeg.repository.WineClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WineClassBookMarkServiceImpl implements WineClassBookMarkService{
    private final WineClassBookMarkRepository wineClassBookMarkRepository;
    private final WineClassRepository wineClassRepository;
    private final MemberRepository memberRepository;

    @Override
    public WineClassBookMarkResponseDTO saveWineClassBookMark(Long userId, WineClassBookMarkRequestDTO wineClassBookMarkRequestDTO) {
        if (wineClassBookMarkRepository.existsByMemberIdAndWineClassId(userId, wineClassBookMarkRequestDTO.getWineClassId()))
            throw new GeneralException(ErrorStatus.WINE_CLASS_BOOKMARK_DUPLICATED);

        WineClass wineClass = wineClassRepository.findById(wineClassBookMarkRequestDTO.getWineClassId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINECLASS_NOT_FOUND));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        WineClassBookMark wineClassBookMark = WineClassBookMarkConverter.toWineClassBookMark(wineClass, member);
        wineClassBookMarkRepository.save(wineClassBookMark);

        return WineClassBookMarkConverter.toWineClassBookMarkResponseDTO(wineClassBookMark);
    }

    @Override
    public List<WineClassBookMarkResponseDTO> getWineClassBookMarksByUserId(Long userId) {
        if (!memberRepository.existsById(userId))
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        return wineClassBookMarkRepository.findAllByMemberId(userId)
                .stream()
                .map(WineClassBookMarkConverter::toWineClassBookMarkResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWineClassBookMarkById(Long wineClassBookMarkId) {
        wineClassBookMarkRepository.findById(wineClassBookMarkId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_CLASS_BOOKMARK_NOT_FOUND));
        wineClassBookMarkRepository.deleteById(wineClassBookMarkId);
    }
}
