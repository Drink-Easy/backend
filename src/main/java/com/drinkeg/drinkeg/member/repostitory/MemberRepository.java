package com.drinkeg.drinkeg.member.repostitory;

import com.drinkeg.drinkeg.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>{

    Optional<Member> findByUsername(String username);

    Boolean existsByUsername(String username);
}
