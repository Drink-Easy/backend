package com.drinkeg.drinkeg.domain.wineClassProgress.repository;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wineClass.domain.WineClass;
import com.drinkeg.drinkeg.domain.wineClassProgress.domain.WineClassProgress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WineClassProgressRepository extends JpaRepository<WineClassProgress, Long>, WineClassProgressRepositoryCustom {
    Optional<WineClassProgress> findByWineClassAndMember(WineClass wineClass, Member member);
    boolean existsByWineClassAndMember(WineClass wineClass, Member member);
}