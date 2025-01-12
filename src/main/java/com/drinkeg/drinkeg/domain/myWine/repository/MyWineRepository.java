package com.drinkeg.drinkeg.domain.myWine.repository;

import com.drinkeg.drinkeg.domain.member.domain.Member;
import com.drinkeg.drinkeg.domain.myWine.domain.MyWine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MyWineRepository extends JpaRepository<MyWine, Long> {

    @Query("SELECT w FROM MyWine w JOIN FETCH w.wine WHERE w.member = :member ORDER BY w.createdAt DESC")
    List<MyWine> findByMemberOrderByCreatedAt(Member member);
}
