package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
public interface RefreshRepository extends JpaRepository<RefreshToken,Long> {


    Boolean existsByRefresh(String refresh);

    @Transactional
    void deleteByRefresh(String refresh);
}
