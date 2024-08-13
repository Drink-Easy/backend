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
public class PartyResponseDTO {

    private Long id;

    private Long hostId;

    private String name;

    private String introduce;

    private Integer limitMemberNum;

    private Integer participateMemberNum;

    private Date partyDate;

    private Integer admissionFee;

    private String place;

    //private  partyWine;
}
