package com.drinkeg.drinkeg.wineLecture.repository;

import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.wineLecture.domain.WineLecture;
import com.drinkeg.drinkeg.wineLecture.domain.WineLectureComplete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WineLectureCompleteRepository extends JpaRepository<WineLectureComplete, Long> {
    boolean existsByWineLectureAndMember(WineLecture wineLecture, Member member);

    Optional<WineLectureComplete> findByWineLectureIdAndMemberId(Long wineLectureId, Long memberId);
}
