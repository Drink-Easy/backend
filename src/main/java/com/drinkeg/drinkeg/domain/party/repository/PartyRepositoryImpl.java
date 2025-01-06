package com.drinkeg.drinkeg.party.repository;

import com.drinkeg.drinkeg.member.domain.QMember;
import com.drinkeg.drinkeg.party.domain.QParty;
import com.drinkeg.drinkeg.party.dto.PartyResponseDTO;
import com.drinkeg.drinkeg.partyJoinMember.domain.QPartyJoinMember;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PartyRepositoryImpl implements PartyRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PartyResponseDTO> findPartiesByMemberId(Long memberId) {
        QPartyJoinMember pjm = QPartyJoinMember.partyJoinMember;
        QParty party = QParty.party;
        QMember member = QMember.member;

        return queryFactory.select(Projections.fields(PartyResponseDTO.class,
                        party.id.as("id"),
                        party.name.as("name"),
                        party.participateMemberNum.as("participateMemberNum"),
                        party.limitMemberNum.as("limitMemberNum"),
                        party.admissionFee.as("admissionFee"),
                        party.place.as("place"),
                        party.partyDate.as("partyDate"),
                        party.bookmarkCount.as("bookmarkCount"),
                        party.introduce.as("introduce"),
                        party.createdAt.as("createdAt"),
                        party.hostId.as("hostId"),
                        member.name.as("hostName")
                ))
                .from(pjm)
                .leftJoin(pjm.party, party)
                .leftJoin(pjm.member, member)
                .where(member.id.eq(memberId))
                .fetch();
    }
}
