package com.drinkeg.drinkeg.party.domain;


import com.drinkeg.drinkeg.domain.BaseEntity;
import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.PartyJoinMember;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Party extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String name;

    private String introduce;

    private int limitMemberNum;

    @Builder.Default
    private int participateMemberNum = 1;

    private Date partyDate;

    private int admissionFee;

    private String place;

    private Long hostId;
    //private String partyWine;

    @Builder.Default
    private int bookmarkCount = 0;

    // BaseEntity 필드
    //private LocalDateTime createdAt;
    //private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    private List<PartyJoinMember> participations = new ArrayList<>();

    public void updateParticipateMemberNum(int participateMemberNum) {
        this.participateMemberNum = participateMemberNum;
    }

    // bookmarkCount 업데이트 커스텀 메서드
    public void updateBookmarkCount(int bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }
}
