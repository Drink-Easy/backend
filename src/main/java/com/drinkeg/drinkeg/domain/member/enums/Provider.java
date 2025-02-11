package com.drinkeg.drinkeg.domain.member.enums;


import com.drinkeg.drinkeg.global.apipayLoad.code.status.ErrorStatus;
import com.drinkeg.drinkeg.global.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.AssertionFailure;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Provider {

    KAKAO("Kakao"),
    APPLE("Apple"),
    DRINKEG("DRINKIG");

    private final String value;

    public static Provider fromValue(String value) {
        return Arrays.stream(Provider.values())
                .filter(provider -> provider.getValue().equals(value)) // 대소문자 정확히 일치
                .findFirst()
                .orElseThrow(() -> new GeneralException(ErrorStatus.PROVIDER_NOT_FOUND));
    }


}