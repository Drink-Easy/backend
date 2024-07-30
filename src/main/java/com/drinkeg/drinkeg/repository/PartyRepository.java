package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyRepository extends JpaRepository<Party, Long> {

    List<Party> findAll();

}
