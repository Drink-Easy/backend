package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineClassBookMark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface WineClassBookMarkRepository extends JpaRepository<WineClassBookMark, Long> {
    Boolean existsByMemberAndWineClass(Member member, WineClass wineClass);

    List<WineClassBookMark> findAllByMember(Member member);
}
