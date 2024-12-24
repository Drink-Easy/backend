package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UuidRepository extends JpaRepository<Uuid,Long> {

    Optional<Uuid> findByUuid(String savedUuid);

    void deleteByUuid(String savedUuid);
}
