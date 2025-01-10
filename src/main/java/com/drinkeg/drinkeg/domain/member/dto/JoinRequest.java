package com.drinkeg.drinkeg.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class JoinRequest {

    private String username;
    private String password;
    private String rePassword;


}