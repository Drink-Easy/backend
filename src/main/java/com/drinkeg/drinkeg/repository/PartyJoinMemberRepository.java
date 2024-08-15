package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.domain.PartyJoinMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartyJoinMemberRepository extends JpaRepository<PartyJoinMember, Long> {
    boolean existsByMemberAndParty(Member member, Party party);
}
