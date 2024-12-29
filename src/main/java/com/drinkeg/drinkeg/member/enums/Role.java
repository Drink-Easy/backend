package com.drinkeg.drinkeg.member.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ADMIN"), USER("USER");

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }


}
