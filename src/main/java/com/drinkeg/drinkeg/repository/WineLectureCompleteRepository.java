package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineLecture;
import com.drinkeg.drinkeg.domain.WineLectureComplete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WineLectureCompleteRepository extends JpaRepository<WineLectureComplete, Long> {
    List<WineLectureComplete> findAllByMember(Member member);

    boolean existsByWineLectureAndMember(WineLecture wineLecture, Member member);
}
