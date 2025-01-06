package com.drinkeg.drinkeg.domain.uuid.repository;

import com.drinkeg.drinkeg.domain.uuid.domain.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UuidRepository extends JpaRepository<Uuid,Long> {

    Optional<Uuid> findByUuid(String savedUuid);

    void deleteByUuid(String savedUuid);
}
