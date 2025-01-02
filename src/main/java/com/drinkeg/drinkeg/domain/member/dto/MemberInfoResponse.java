package com.drinkeg.drinkeg.domain.member.dto;


import com.drinkeg.drinkeg.domain.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResponse {

    private String imageUrl;
    private String username;
    private String email;
    private String city;
    private String authType;
    private boolean isAdult;

    public static MemberInfoResponse create(Member member,String imageUrl, String email, String city){

        return  MemberInfoResponse.builder()
                .imageUrl(imageUrl)
                .username(member.getName())
                .email(email)
                .city(city)
                .authType(member.getProvider().getValue())
                .isAdult(member.isAdult())
                .build();

    }

}
