package com.drinkeg.drinkeg.domain.member.repostitory;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    void deleteByUsername(String username);

    Member findMemberByUsername(String username);
}
