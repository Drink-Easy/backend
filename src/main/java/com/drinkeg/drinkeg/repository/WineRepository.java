package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Wine;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WineRepository extends JpaRepository<Wine, Long> {

    // 검색한 와인 이름이 포함된 모든 와인을 찾는다.
    List<Wine> findAllByNameContainingIgnoreCase(String name);

    // 검색한 와인 이름이 완전히 일치하는 와인을 찾는다.
    List<Wine> findAllByName(String name);

    // 추천 와인 조회를 위한 쿼리
    @Query("SELECT w FROM Wine w WHERE " +
            "(:wineSort IS NULL OR LOWER(w.sort) LIKE %:wineSort%) AND " +
            "(:wineArea IS NULL OR EXISTS (SELECT 1 FROM w.area a WHERE LOWER(a) LIKE %:wineArea%)) AND " +
            "(:monthPriceMax IS NULL OR w.price <= :monthPriceMax/1300)")
    List<Wine> findRecommendedWines(@Param("wineSort") List<String> wineSort,
                                    @Param("wineArea") List<String> wineArea,
                                    @Param("monthPriceMax") Long monthPriceMax);

}
