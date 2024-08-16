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
    List<Wine> findAllBySortContainingIgnoreCase(String sort);
    List<Wine> findAllByAreaContainingIgnoreCase(String area);
    List<Wine> findAllByPriceIsLessThanEqual(int price);

}
