package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
}
