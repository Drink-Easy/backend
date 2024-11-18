package com.drinkeg.drinkeg.repository.wine;

import com.drinkeg.drinkeg.domain.Wine;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WineRepository extends JpaRepository<Wine, Long>, WineRepositoryCustom {

    // 추천 와인 조회를 위한 쿼리
    List<Wine> findAllBySortContainingIgnoreCase(String sort);
    List<Wine> findAllByAreaContainingIgnoreCase(String area);
    List<Wine> findAllByPriceIsLessThanEqual(int price);

    // Wine 조회 시 WineNote 도 같이 조회
    @Query("SELECT w FROM Wine w JOIN FETCH w.wineNote WHERE w.id = :id")
    Optional<Wine> findByIdWithWineNote(@Param("id") Long id);

    @Query("SELECT w FROM Wine w JOIN FETCH w.tastingNoteList WHERE w.id = :id")
    Optional<Wine> findByIdWithTastingNote(@Param("id") Long id);

}
