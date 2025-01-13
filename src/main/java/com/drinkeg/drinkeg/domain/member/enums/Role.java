package com.drinkeg.drinkeg.domain.member.enums;



import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"), ROLE_USER("ROLE_USER");

    private final String value;
    public static Role fromValue(String value) {

        return Arrays.stream(Role.values())
                .filter(role -> role.getValue().equals(value)) // 대소문자 정확히 일치
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.ROLE_NOT_FOUND));
    }



}