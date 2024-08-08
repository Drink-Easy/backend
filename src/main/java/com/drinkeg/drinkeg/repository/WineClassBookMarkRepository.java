package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.WineClassBookMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WineClassBookMarkRepository extends JpaRepository<WineClassBookMark, Long> {
    Boolean existsByMemberIdAndWineClassId(Long memberId, Long wineClassId);
    List<WineClassBookMark> findAllByMemberId(Long memberId);
}
