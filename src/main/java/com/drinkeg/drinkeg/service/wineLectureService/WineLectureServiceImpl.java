package com.drinkeg.drinkeg.service.wineLectureService;

import com.drinkeg.drinkeg.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.converter.WineLectureConverter;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineLecture;
import com.drinkeg.drinkeg.dto.WineLectureDTO.request.WineLectureRequestDTO;
import com.drinkeg.drinkeg.dto.WineLectureDTO.response.WineLectureResponseDTO;
import com.drinkeg.drinkeg.dto.loginDTO.commonDTO.PrincipalDetail;
import com.drinkeg.drinkeg.exception.GeneralException;
import com.drinkeg.drinkeg.repository.WineLectureRepository;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.wineClassService.WineClassService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WineLectureServiceImpl implements WineLectureService {
    private final WineLectureRepository wineLectureRepository;
    private final WineClassService wineClassService;
    private final MemberService memberService;

    @Override
    public List<WineLectureResponseDTO> showAllWineLectures(PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        List<WineLecture> wineLectures = wineLectureRepository.findAll();
        return wineLectures.stream()
                .map(wineLecture -> WineLectureConverter.toWineLectureResponseDTO(wineLecture, member))
                .collect(Collectors.toList());
    }

    @Override
    public List<WineLectureResponseDTO> showAllWineLecturesByWineClass(Long wineClassId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        WineClass wineClass = wineClassService.getWineClassById(wineClassId);

        List<WineLecture> wineLectures = wineLectureRepository.findByWineClass(wineClass);
        return wineLectures.stream()
                .map(wineLecture -> WineLectureConverter.toWineLectureResponseDTO(wineLecture, member))
                .collect(Collectors.toList());
    }

    @Override
    public WineLectureResponseDTO showWineLectureById(Long wineLectureId,PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        WineLecture wineLecture = wineLectureRepository.findById(wineLectureId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_LECTURE_NOT_FOUND));
        return WineLectureConverter.toWineLectureResponseDTO(wineLecture, member);
    }

    @Override
    public WineLectureResponseDTO saveWineLecture(WineLectureRequestDTO wineLectureRequestDTO, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);
        WineClass wineClass = wineClassService.getWineClassById(wineLectureRequestDTO.getWineClassId());

        WineLecture wineLecture = WineLectureConverter.toWineLecture(wineLectureRequestDTO, wineClass, member);

        wineLectureRepository.save(wineLecture);
        return WineLectureConverter.toWineLectureResponseDTO(wineLecture, member);
    }

    @Override
    public WineLectureResponseDTO updateWineLecture(WineLectureRequestDTO wineLectureRequestDTO, Long wineLectureId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        WineLecture wineLecture = wineLectureRepository.findById(wineLectureId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_LECTURE_NOT_FOUND));

        if (!member.getRole().equals("ROLE_ADMIN") && !wineLecture.getAuthor().equals(member))
            throw new GeneralException(ErrorStatus.WINE_LECTURE_UNAUTHORIZED);

        WineClass wineClass = wineClassService.getWineClassById(wineLectureRequestDTO.getWineClassId());

        wineLecture
                .updateWineClass(wineClass)
                .updateTitle(wineLectureRequestDTO.getTitle())
                .updateContent(wineLectureRequestDTO.getContent());

        return WineLectureConverter.toWineLectureResponseDTO(wineLecture, member);
    }

    @Override
    public void deleteWineLecture(Long wineLectureId, PrincipalDetail principalDetail) {
        Member member = memberService.loadMemberByPrincipleDetail(principalDetail);

        WineLecture wineLecture = wineLectureRepository.findById(wineLectureId)
                        .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_LECTURE_NOT_FOUND));

        if (!member.getRole().equals("ROLE_ADMIN") && !wineLecture.getAuthor().equals(member))
            throw new GeneralException(ErrorStatus.WINE_LECTURE_UNAUTHORIZED);

        wineLectureRepository.delete(wineLecture);
    }

    @Override
    public List<WineLecture> getAllWineLectures() {
        return wineLectureRepository.findAll();
    }

    @Override
    public List<WineLecture> getAllWineLecturesByWineClass(WineClass wineClass) {
        return wineLectureRepository.findByWineClass(wineClass);
    }

    @Override
    public WineLecture getWineLectureById(Long id) {
        return wineLectureRepository.findById(id)
                .orElseThrow(() -> new GeneralException(ErrorStatus.WINE_LECTURE_NOT_FOUND));
    }


}
