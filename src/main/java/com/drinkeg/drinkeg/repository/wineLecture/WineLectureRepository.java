package com.drinkeg.drinkeg.repository.wineLecture;

import com.drinkeg.drinkeg.domain.WineClass;
import com.drinkeg.drinkeg.domain.WineLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WineLectureRepository extends JpaRepository<WineLecture, Long>, WineLectureRepositoryCustom {
    List<WineLecture> findByWineClass(WineClass wineClass);
}
