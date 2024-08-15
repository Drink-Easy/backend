package com.drinkeg.drinkeg.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Auditable;

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

    private int participateMemberNum;

    private Date partyDate;

    private int admissionFee;

    private String place;

    private Long hostId;
    //private String partyWine;

    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    private List<PartyJoinMember> participations = new ArrayList<>();

    public void updateParticipateMemberNum(int participateMemberNum) {
        this.participateMemberNum = participateMemberNum;
    }
}
