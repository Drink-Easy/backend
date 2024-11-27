package com.drinkeg.drinkeg.wineClassProgress.repository;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.wineClass.domain.WineClass;
import com.drinkeg.drinkeg.wineClassProgress.domain.WineClassProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WineClassProgressRepository extends JpaRepository<WineClassProgress, Long>, WineClassProgressRepositoryCustom {
    Optional<WineClassProgress> findByWineClassAndMember(WineClass wineClass, Member member);
    boolean existsByWineClassAndMember(WineClass wineClass, Member member);
}