package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PartyRepository extends JpaRepository<Party, Long> {

    // 단일 엔티티 조회 -> Optional 사용
    Optional<Party> findById(Long id);

    // 여러 엔티티 조회 -> List 사용
    List<Party> findAll();

    // 최신순 정렬
    Page<Party> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 마감 임박순 정렬
    Page<Party> findAllByOrderByPartyDateAsc(Pageable pageable);

    // 인원이 많이 모인 순 정렬
    Page<Party> findAllByOrderByParticipateMemberNumDesc(Pageable pageable);

    // 가격순 정렬 (낮은 가격 순)
    Page<Party> findAllByOrderByAdmissionFeeAsc(Pageable pageable);
}
