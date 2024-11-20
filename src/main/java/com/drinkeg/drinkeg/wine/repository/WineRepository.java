<<<<<<<< HEAD:src/main/java/com/drinkeg/drinkeg/repository/wine/WineRepository.java
package com.drinkeg.drinkeg.repository.wine;
========
package com.drinkeg.drinkeg.wine.repository;
>>>>>>>> dev:src/main/java/com/drinkeg/drinkeg/wine/repository/WineRepository.java

import com.drinkeg.drinkeg.wine.domain.Wine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WineRepository extends JpaRepository<Wine, Long>, WineRepositoryCustom {

    // 검색한 와인 이름이 포함된 모든 와인을 찾는다.
    List<Wine> findAllByNameContainingIgnoreCaseOrderByName(String name);

}
