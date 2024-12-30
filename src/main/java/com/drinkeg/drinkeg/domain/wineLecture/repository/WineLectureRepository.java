package com.drinkeg.drinkeg.domain.wineLecture.repository;

import com.drinkeg.drinkeg.domain.wineClass.domain.WineClass;
import com.drinkeg.drinkeg.domain.wineLecture.domain.WineLecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WineLectureRepository extends JpaRepository<WineLecture, Long>, WineLectureRepositoryCustom {
    List<WineLecture> findByWineClass(WineClass wineClass);
}
