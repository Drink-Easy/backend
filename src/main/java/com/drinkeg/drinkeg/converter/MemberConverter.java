package com.drinkeg.drinkeg.converter;

import com.drinkeg.drinkeg.domain.Member;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class MemberConverter {

    public Member toAppleMember(String username, Map<String, Object> claims) {
        return Member.builder()
                .username(username)
                .email((String) claims.get("email"))  // email 값을 claims에서 추출
                .role("ROLE_USER")
                .isFirst(true)
                .build();

    }
    public Member toMember(String username, String password, boolean isBoolean){
        return Member.builder()
                .username(username)
                .password(password)
                .role("ROLE_USER")
                .isFirst(isBoolean)
                .build();
    }
}
