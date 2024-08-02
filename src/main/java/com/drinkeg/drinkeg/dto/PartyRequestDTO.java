package com.drinkeg.drinkeg.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PartyRequestDTO {

    //개설 회원 id
    private Long hostId;

    //모임이름
    private String name;

    //모임 한줄소개
    private String introduce;

    //최대 인원
    private Integer limitMemberNum;

    //참가한 인원
    private Integer participateMemberNum;

    //모임 날짜
    private Date partyDate;

    //예약금
    private Integer admissionFee;

    //모임 장소
    private String place;

    //모임 와인  사진? url?
    //private Long partyWine;
}

