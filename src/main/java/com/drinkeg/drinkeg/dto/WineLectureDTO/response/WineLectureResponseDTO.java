package com.drinkeg.drinkeg.dto.WineLectureDTO.response;

import com.drinkeg.drinkeg.dto.MemberDTO.response.MemberBasicInfoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WineLectureResponseDTO {
    private Long id;
    private String title;
    private String content;
    private MemberBasicInfoResponseDTO author;
}
