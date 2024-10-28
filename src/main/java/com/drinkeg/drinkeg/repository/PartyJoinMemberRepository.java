package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.domain.PartyJoinMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartyJoinMemberRepository extends JpaRepository<PartyJoinMember, Long> {
    boolean existsByMemberAndParty(Member member, Party party);

    // 특정 파티에 속한 참가자의 수를 계산하는 메서드
    long countByParty(Party party);

    Optional<PartyJoinMember> findByMemberAndParty(Member member, Party party);
}
