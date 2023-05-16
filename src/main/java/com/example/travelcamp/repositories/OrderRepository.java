package com.example.travelcamp.repositories;

import com.example.travelcamp.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
