package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.WineNewsBookMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WineNewsBookMarkRepository extends JpaRepository<WineNewsBookMark, Long> {
    Boolean existsByMemberIdAndWineNewsId(Long memberId, Long wineNewsId);
    List<WineNewsBookMark> findAllByMemberId(Long memberId);
}
