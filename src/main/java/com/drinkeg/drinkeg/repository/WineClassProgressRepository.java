package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.WineClassProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WineClassProgressRepository extends JpaRepository<WineClassProgress, Long> {
    Optional<WineClassProgress> findByMemberIdAndWineClassId(Long memberId, Long wineClassId);
    List<WineClassProgress> findByMemberId(Long memberId);
}