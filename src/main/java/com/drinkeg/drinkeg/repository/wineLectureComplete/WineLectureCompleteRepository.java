package com.drinkeg.drinkeg.repository.wineLectureComplete;

import com.drinkeg.drinkeg.domain.Member;
import com.drinkeg.drinkeg.domain.WineLecture;
import com.drinkeg.drinkeg.domain.WineLectureComplete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WineLectureCompleteRepository extends JpaRepository<WineLectureComplete, Long>, WineLectureCompleteRepositoryCustom {
    List<WineLectureComplete> findAllByMember(Member member);

    boolean existsByWineLectureAndMember(WineLecture wineLecture, Member member);

    Optional<WineLectureComplete> findByWineLectureIdAndMemberId(Long wineLectureId, Long memberId);
}
