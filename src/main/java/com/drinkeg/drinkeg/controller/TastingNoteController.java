package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineNote;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteUpdateRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.AllNoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NotePriviewResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteResponseDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.request.NoteWineRequestDTO;
import com.drinkeg.drinkeg.dto.TastingNoteDTO.response.NoteWineResponseDTO;
import com.drinkeg.drinkeg.service.memberService.MemberService;
import com.drinkeg.drinkeg.service.tastingNoteService.TastingNoteService;
import com.drinkeg.drinkeg.service.wineNoteService.WineNoteService;
import com.drinkeg.drinkeg.service.wineService.WineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasting-note")
public class TastingNoteController {

    private final TastingNoteService tastingNoteService;
    private final WineService wineService;
    private final MemberService memberService;

    // 새 노트 작성
    @PostMapping("/new-note")
    public ApiResponse<String> saveNote(@RequestBody @Valid NoteRequestDTO noteRequestDTO) {

        tastingNoteService.saveNote(noteRequestDTO);
        return ApiResponse.onSuccess("노트 작성 완료");
    }

    // 새 노트 작성 시 와인 검색
    @GetMapping("/wine")
    public ApiResponse<List<NoteWineResponseDTO>> saveNote(@RequestBody NoteWineRequestDTO noteWineRequestDTO) {

        List<NoteWineResponseDTO> noteWineResponseDTOs = wineService.searchWinesByName(noteWineRequestDTO);
        return ApiResponse.onSuccess(noteWineResponseDTOs);
    }

    // 선택한 노트 보기
    @GetMapping("/{noteId}")
    public ApiResponse<NoteResponseDTO> showNote(@PathVariable("noteId") Long noteId) {

        NoteResponseDTO noteResponseDTO = tastingNoteService.showNoteById(noteId);
        return ApiResponse.onSuccess(noteResponseDTO);
    }

    // 전체 노트 보기
    @GetMapping("/all-note/{memberId}")
    public ApiResponse<List<NotePriviewResponseDTO>> showAllNote(@PathVariable("memberId") Long memberId) {

        Member foundMember = memberService.findMemberById(memberId);

        List<NotePriviewResponseDTO> allNoteByMember = tastingNoteService.findAllNoteByMember(foundMember);
        return ApiResponse.onSuccess(allNoteByMember);
    }

    @PatchMapping("/{noteId}")
    public ApiResponse<String> updateTastingNote(@PathVariable("noteId") Long noteId, @RequestBody @Valid NoteUpdateRequestDTO noteUpdateRequestDTO) {

        tastingNoteService.updateTastingNote(noteId, noteUpdateRequestDTO);
        return ApiResponse.onSuccess("노트 수정 완료");
    }

    @DeleteMapping("/{noteId}")
    public ApiResponse<String> deleteTastingNote(@PathVariable("noteId") Long noteId) {

        tastingNoteService.deleteTastingNote(noteId);
        return ApiResponse.onSuccess("노트 삭제 완료");
    }

}
