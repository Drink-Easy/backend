package com.drinkeg.drinkeg.member.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.AssertionFailure;

@Getter
@RequiredArgsConstructor
public enum Provider {

    KAKAO("Kakao"),
    APPLE("Apple"),
    DRINKEG("Drinkeg");

    private final String value;

    @Override
    public String toString() {
        return this.value;
    }

    public static Provider fromValue(String value) {
        for (Provider provider : Provider.values()) {
            if (provider.value.equalsIgnoreCase(value)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("No enum constant " + Provider.class.getCanonicalName() + "." + value);
    }


}
