package com.drinkeg.drinkeg.repository.wine;

import com.drinkeg.drinkeg.domain.Wine;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface WineRepository extends JpaRepository<Wine, Long>, WineRepositoryCustom {

    // 검색한 와인 이름이 포함된 모든 와인을 찾는다.
    List<Wine> findAllByNameContainingIgnoreCaseOrderByName(String name);

}
