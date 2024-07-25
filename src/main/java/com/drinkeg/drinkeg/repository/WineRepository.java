package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WineRepository extends JpaRepository<Wine, Long> {

    // 검색한 와인 이름이 포함된 모든 와인을 찾는다.
    List<Wine> findAllByNameContainingIgnoreCase(String name);

    // 검색한 와인 이름이 완전히 일치하는 와인을 찾는다.
    List<Wine> findAllByName(String name);
}
