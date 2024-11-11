package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineClassProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WineClassProgressRepository extends JpaRepository<WineClassProgress, Long> {
    Optional<WineClassProgress> findByWineClassAndMember(WineClass wineClass, Member member);
    boolean existsByWineClassAndMember(WineClass wineClass, Member member);
}