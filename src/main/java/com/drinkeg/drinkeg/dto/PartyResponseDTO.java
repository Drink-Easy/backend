package com.drinkeg.drinkeg.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public class PartyResponseDTO {

    private Long id;

    private Long hostId;

    private String name;

    private String introduce;

    private int limitMemberNum;

    private int participateMemberNum;

    private Date partyDate;

    private int admissionFee;

    private String place;

    //private  partyWine;
}
