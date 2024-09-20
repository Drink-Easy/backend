package com.drinkeg.drinkeg.service.WineLectureCompleteService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineLectureCompleteConverter;
import com.drinkeg.drinkeg.converter.WineLectureConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineLecture;
import com.drinkeg.drinkeg.domain.WineLectureComplete;
import com.drinkeg.drinkeg.dto.WineLectureCompleteDTO.response.WineLectureCompleteResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.WineLectureCompleteRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.wineLectureService.WineLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WineLectureCompleteServiceImpl implements WineLectureCompleteService {
    private final WineLectureCompleteRepository wineLectureCompleteRepository;
    private final WineLectureService wineLectureService;
    private final MemberService memberService;

    @Override
    public List<WineLectureCompleteResponseDTO> showWineLectureCompleteByMember(PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        List<WineLectureComplete> wineLectureCompletes = wineLectureCompleteRepository.findAllByMember(member);

        return wineLectureCompletes.stream()
                .map(wineLectureComplete -> WineLectureCompleteConverter.toWineLectureCompleteResponseDTO(wineLectureComplete))
                .collect(Collectors.toList());
    }

    @Override
    public WineLectureCompleteResponseDTO saveWineLectureComplete(Long wineLectureId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);
        WineLecture wineLecture = wineLectureService.getWineLectureById(wineLectureId);

        if (wineLectureCompleteRepository.existsByWineLectureAndMember(wineLecture, member))
            throw new GeneralException(ErrorStatus.WINE_LECTURE_COMPLETE_ALREADY_EXISTS);

        WineLectureComplete wineLectureComplete = WineLectureCompleteConverter.toWineLectureComplete(wineLecture, member);
        wineLectureCompleteRepository.save(wineLectureComplete);

        return WineLectureCompleteConverter.toWineLectureCompleteResponseDTO(wineLectureComplete);
    }

    @Override
    public void deleteWineLectureCompleteById(Long wineLectureCompleteId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        WineLectureComplete wineLectureComplete = wineLectureCompleteRepository.findById(wineLectureCompleteId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_LECTURE_COMPLETE_NOT_FOUND));

        if (!wineLectureComplete.getMember().equals(member) && !member.getRole().equals("ROLE_ADMIN"))
            throw new GeneralException(ErrorStatus.WINE_LECTURE_COMPLETE_UNAUTHORIZED);

        wineLectureCompleteRepository.delete(wineLectureComplete);
    }
}
