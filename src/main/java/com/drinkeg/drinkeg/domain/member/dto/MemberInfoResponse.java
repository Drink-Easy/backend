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
    private String authType;
    private boolean isAdult;

    public static MemberInfoResponse of(Member member){

        return  MemberInfoResponse.builder()
                .imageUrl(member.getImageUrl())
                .username(member.getName())
                .email(member.getEmail())
                .authType(member.getProvider().getValue())
                .isAdult(member.isAdult())
                .build();

    }

}
