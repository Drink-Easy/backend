package com.drinkeg.drinkeg.wineLectureComplete.repository;

import com.drinkeg.drinkeg.member.domain.Member;
import com.drinkeg.drinkeg.wineLecture.domain.WineLecture;
import com.drinkeg.drinkeg.wineLectureComplete.domain.WineLectureComplete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WineLectureCompleteRepository extends JpaRepository<WineLectureComplete, Long>, WineLectureCompleteRepositoryCustom {
    List<WineLectureComplete> findAllByMember(Member member);

    boolean existsByWineLectureAndMember(WineLecture wineLecture, Member member);

    Optional<WineLectureComplete> findByWineLectureIdAndMemberId(Long wineLectureId, Long memberId);
}
