package com.drinkeg.drinkeg.repository;

import com.drinkeg.drinkeg.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
