package com.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.order.entity.LineItem;

public interface LineItemRepository extends JpaRepository<LineItem, Long> {
    
}
