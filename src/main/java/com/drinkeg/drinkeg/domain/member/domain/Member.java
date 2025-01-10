package com.drinkeg.drinkeg.domain.member.domain;


import com.drinkeg.drinkeg.domain.member.converter.StringListConverter;
import com.drinkeg.drinkeg.domain.member.dto.MemberRequest;
import com.drinkeg.drinkeg.domain.member.dto.MemberUpdateRequest;
import com.drinkeg.drinkeg.domain.myWine.domain.MyWine;
import com.drinkeg.drinkeg.domain.member.enums.Provider;
import com.drinkeg.drinkeg.domain.member.enums.Role;
import com.drinkeg.drinkeg.domain.tastingNote.domain.TastingNote;
import com.drinkeg.drinkeg.domain.wineWishlist.domain.WineWishlist;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private String username;

    private String password;

    private String region;

    private Boolean isNewbie;

    private boolean isAdult;

    // 처음 회원가입 한 사용자면 true이다가 회원 가입하면 false로 변함
    private Boolean isFirst;

    // 월 평균 와인 소비가의 범위중 최댓값
    private Long monthPriceMax;

    // 프로필 이미지
    private String imageUrl;

    // 선호 종류, 품종, 국가
    @Convert(converter = StringListConverter.class)
    private List<String> wineSort = new ArrayList<>();

    @Convert(converter = StringListConverter.class)
    private List<String> wineArea = new ArrayList<>();

    @Convert(converter = StringListConverter.class)
    private List<String> wineVariety = new ArrayList<>();

    private boolean agreement;

    @OneToMany(mappedBy = "member")
    private List<TastingNote> tastingNotes = new ArrayList<>();

    // CascadeType.REMOVE: Member 엔티티가 삭제되면 연관된 WineWishlist 엔티티도 삭제
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<WineWishlist> wineWishlists = new ArrayList<>();

    // CascadeType.REMOVE: Member 엔티티가 삭제되면 연관된 MyWine 엔티티도 삭제
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MyWine> myWines = new ArrayList<>();

    @Builder
    public Member(String name, String email, Role role, Provider provider, String username, String password,
                   String region, Boolean isNewbie, Boolean isFirst, Long monthPriceMax,
                   List<String> wineSort, List<String> wineArea, boolean agreement, boolean isAdult, String imageUrl) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.provider = provider;

        this.username = username;
        this.password = password;

        this.region = region;
        this.isNewbie = isNewbie;
        this.isFirst = isFirst;
        this.monthPriceMax = monthPriceMax;
        this.wineSort = wineSort != null ? wineSort : new ArrayList<>();
        this.wineArea = wineArea != null ? wineArea : new ArrayList<>();
        this.agreement = agreement;
        this.isAdult = isAdult;
        this.imageUrl = imageUrl;
    }


    public void updateMemberInfo(MemberUpdateRequest memberUpdateRequest) {
        if (memberUpdateRequest.getName() != null) name = memberUpdateRequest.getName();
        if (memberUpdateRequest.getRegion() != null) region = memberUpdateRequest.getRegion();
    }

    public void updateEmail(String email) { this.email = email; };
    public void updateImageUrl(String imageUrl) {
        if (imageUrl != null) this.imageUrl = imageUrl;
    };

    public static Member createMember(String username, String password, boolean isFirst) {
        return Member.builder()
                .username(username)
                .password(password)
                .email(username)
                .provider(Provider.DRINKEG)
                .role(Role.ROLE_USER)
                .isFirst(isFirst)
                .build();
    }

    public static Member createOAuthMember(String username, String email, String provider) {
        return Member.builder()
                .username(username)
                .email(email) // email 값을 claims에서 추출
                .role(Role.ROLE_USER)
                .provider(Provider.fromValue(provider))
                .isFirst(true)
                .build();
    }


    public void updateFirstUser(MemberRequest memberRequest){
        if(memberRequest.getName() != null) this.name = memberRequest.getName();
        if(memberRequest.getIsNewbie() != null) this.isNewbie = memberRequest.getIsNewbie();
        if(memberRequest.getMonthPrice() != null) this.monthPriceMax = memberRequest.getMonthPrice();
        if(memberRequest.getWineSort() != null) this.wineSort = memberRequest.getWineSort();
        if(memberRequest.getWineArea() != null) this.wineArea = memberRequest.getWineArea();
        if(memberRequest.getWineVariety() != null) this.wineVariety = memberRequest.getWineVariety();
        if(memberRequest.getRegion() != null) this.region =memberRequest.getRegion();
        this.isFirst = false;
    }
}
