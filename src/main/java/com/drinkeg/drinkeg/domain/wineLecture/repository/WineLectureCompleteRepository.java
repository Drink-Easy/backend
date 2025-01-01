package com.drinkeg.drinkeg.domain.wineLecture.repository;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.wineLecture.domain.WineLecture;
import com.drinkeg.drinkeg.domain.wineLecture.domain.WineLectureComplete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WineLectureCompleteRepository extends JpaRepository<WineLectureComplete, Long> {
    boolean existsByWineLectureAndMember(WineLecture wineLecture, Member member);

    Optional<WineLectureComplete> findByWineLectureIdAndMemberId(Long wineLectureId, Long memberId);
}
