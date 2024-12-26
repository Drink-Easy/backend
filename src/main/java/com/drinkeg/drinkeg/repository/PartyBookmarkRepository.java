package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.domain.Party;
import com.drinkeg.drinkeg.domain.PartyBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyBookmarkRepository extends JpaRepository<PartyBookmark, Long> {

    boolean existsByMemberAndParty(Member member, Party party);

    Optional<PartyBookmark> findByMemberAndParty(Member member, Party party);

    List<PartyBookmark> findByMember(Member member);
}
