package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Wine;
import com.drinkeg.drinkeg.domain.WineSale;
import com.drinkeg.drinkeg.domain.WineStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface WineSaleRepository extends JpaRepository<WineSale, Long> {
    List<WineSale> findAllByWineStore(WineStore wineStore);

    List<WineSale> findAllByWine(Wine wine);

}
