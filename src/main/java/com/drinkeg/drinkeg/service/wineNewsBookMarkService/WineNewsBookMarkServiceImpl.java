package com.drinkeg.drinkeg.service.wineNewsBookMarkService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineNewsBookMarkConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineNews;
import com.drinkeg.drinkeg.domain.WineNewsBookMark;
import com.drinkeg.drinkeg.dto.WineNewsBookMarkDTO.request.WineNewsBookMarkRequestDTO;
import com.drinkeg.drinkeg.dto.WineNewsBookMarkDTO.response.WineNewsBookMarkResponseDTO;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.MemberRepository;
import com.drinkeg.drinkeg.repository.WineNewsBookMarkRepository;
import com.drinkeg.drinkeg.repository.WineNewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WineNewsBookMarkServiceImpl implements WineNewsBookMarkService{
    private final WineNewsBookMarkRepository wineNewsBookMarkRepository;
    private final WineNewsRepository wineNewsRepository;
    private final MemberRepository memberRepository;

    @Override
    public WineNewsBookMarkResponseDTO saveWineNewsBookMark(Long userId, WineNewsBookMarkRequestDTO wineNewsBookMarkRequestDTO) {
        if (wineNewsBookMarkRepository.existsByMemberIdAndWineNewsId(userId, wineNewsBookMarkRequestDTO.getWineNewsId()))
            throw new GeneralException(ErrorStatus.WINE_NEWS_BOOKMARK_DUPLICATED);

        WineNews wineNews = wineNewsRepository.findById(wineNewsBookMarkRequestDTO.getWineNewsId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINENEWS_NOT_FOUND));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        WineNewsBookMark wineNewsBookMark = WineNewsBookMarkConverter.toWineNewsBookMark(wineNews, member);
        wineNewsBookMarkRepository.save(wineNewsBookMark);

        return WineNewsBookMarkConverter.toWineNewsBookMarkResponseDTO(wineNewsBookMark);
    }

    @Override
    public List<WineNewsBookMarkResponseDTO> getWineNewsBookMarksByUserId(Long userId) {
        if (!memberRepository.existsById(userId))
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);

        return wineNewsBookMarkRepository.findAllByMemberId(userId)
                .stream()
                .map(WineNewsBookMarkConverter::toWineNewsBookMarkResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteWineNewsBookMarkById(Long wineNewsBookMarkId) {
        wineNewsBookMarkRepository.findById(wineNewsBookMarkId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_NEWS_BOOKMARK_NOT_FOUND));
        wineNewsBookMarkRepository.deleteById(wineNewsBookMarkId);
    }
}
