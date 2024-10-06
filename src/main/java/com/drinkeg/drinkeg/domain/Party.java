package com.drinkeg.drinkeg.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Auditable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Party extends BaseEntity{

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

    // BaseEntity 필드
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    private List<PartyJoinMember> participations = new ArrayList<>();

    public void updateParticipateMemberNum(int participateMemberNum) {
        this.participateMemberNum = participateMemberNum;
    }
}
