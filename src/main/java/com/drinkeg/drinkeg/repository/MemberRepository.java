package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
