package com.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
