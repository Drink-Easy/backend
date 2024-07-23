package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long> {
}
