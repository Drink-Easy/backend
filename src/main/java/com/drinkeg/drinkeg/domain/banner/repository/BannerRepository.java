package com.drinkeg.drinkeg.domain.banner.repository;

import com.drinkeg.drinkeg.domain.banner.domain.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findAllByOrderByIdDesc();
}
