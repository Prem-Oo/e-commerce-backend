package com.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cart.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
}