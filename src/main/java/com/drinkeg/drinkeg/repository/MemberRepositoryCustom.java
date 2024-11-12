package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Member;

import java.util.Optional;

public interface MemberRepositoryCustom {

    Optional<Member> findMemberWithTastingNoteByUsername(String username);
}
