package com.drinkeg.drinkeg.controller;

import com.drinkeg.drinkeg.apipayLoad.ApiResponse;
import com.drinkeg.drinkeg.dto.PartyRequestDTO;
import com.drinkeg.drinkeg.dto.PartyResponseDTO;
import com.drinkeg.drinkeg.service.partyService.PartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/parties")
public class PartyController {


    private final PartyService partyService;



    // 모임 개설
    @PostMapping
    public ResponseEntity<ApiResponse<PartyResponseDTO>> createParty(@RequestBody PartyRequestDTO partyRequest) {
        PartyResponseDTO createdParty = partyService.createParty(partyRequest);
        ApiResponse<PartyResponseDTO> response = ApiResponse.onSuccess(createdParty);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // 모임 전체조회
    @GetMapping("/")
    public ResponseEntity<ApiResponse<List<PartyResponseDTO>>> getAllParties() {
        List<PartyResponseDTO> parties = partyService.getAllParties();
        ApiResponse<List<PartyResponseDTO>> response = ApiResponse.onSuccess(parties);
        return ResponseEntity.ok(response);
    }

    // 모임 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PartyResponseDTO>> getParty(@PathVariable Long id) {
        PartyResponseDTO party = partyService.getParty(id);
        ApiResponse<PartyResponseDTO> response = ApiResponse.onSuccess(party);
        return ResponseEntity.ok(response);
    }



    //모임 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PartyResponseDTO>> updateParty(
            @PathVariable Long id,
            @RequestBody PartyRequestDTO partyRequest) {
        PartyResponseDTO updatedParty = partyService.updateParty(id, partyRequest);
        ApiResponse<PartyResponseDTO> response = ApiResponse.onSuccess(updatedParty);
        return ResponseEntity.ok(response);
    }


    // 모임 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteParty(@PathVariable Long id) {
        partyService.deleteParty(id);
        ApiResponse<Void> response = ApiResponse.onSuccess(null);
        return ResponseEntity.ok(response);
    }





    //모임 메인페이지 조회

    //모임 검색
}
