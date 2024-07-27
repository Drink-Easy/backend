package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteWineRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteWineResponseDTO;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.tastingNoteService.TastingNoteService;
import com.drinkeg.drinkeg.service.wineService.WineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tastingNote")
public class TastingNoteController {

    private final TastingNoteService tastingNoteService;
    private final WineService wineService;
    private final MemberService memberService;

    // 새 노트 작성
    @PostMapping("/new-note")
    public ApiResponse<String> saveNote(NoteRequestDTO noteRequestDTO) {

        tastingNoteService.saveNote(noteRequestDTO);
        return ApiResponse.onSuccess("노트 작성 완료");
    }

    // 새 노트 작성 시 와인 검색
    @PostMapping("/wine")
    public ApiResponse<List<NoteWineResponseDTO>> saveNote(NoteWineRequestDTO noteWineRequestDTO) {

        List<NoteWineResponseDTO> noteWineResponseDTOS = wineService.searchWinesByName(noteWineRequestDTO);
        return ApiResponse.onSuccess(noteWineResponseDTOS);
    }

    // 선택한 노트 보기
    @GetMapping("/{noteId}")
    public ApiResponse<NoteResponseDTO> showNote(@PathVariable Long noteId) {

        NoteResponseDTO noteResponseDTO = tastingNoteService.showNoteById(noteId);
        return ApiResponse.onSuccess(noteResponseDTO);
    }

    // 전체 노트 보기
    @GetMapping("/all-note/{memberId}")
    public ApiResponse<AllNoteResponseDTO> showAllNote(@PathVariable Long memberId) {

        Member foundMember = memberService.findMemberById(memberId);

        AllNoteResponseDTO allNoteByMember = tastingNoteService.findAllNoteByMember(foundMember);
        return ApiResponse.onSuccess(allNoteByMember);
    }


}
