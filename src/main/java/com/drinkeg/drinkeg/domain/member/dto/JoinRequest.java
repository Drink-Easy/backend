package com.drinkeg.drinkeg.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class JoinRequest {


    @NotBlank(message = "Username은 필수입니다.")
    private String username;

    @NotBlank(message = "Password는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$",
            message = "Password는 최소 하나의 영문자와 숫자를 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "rePassword는 필수입니다.")
    private String rePassword;


}