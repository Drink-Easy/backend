package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {

    // 단일 엔티티 조회 -> Optional 사용
    Optional<Party> findById(Long id);

    // 여러 엔티티 조회 -> List 사용
    List<Party> findAll();
}
