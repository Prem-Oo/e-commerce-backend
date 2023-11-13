package com.catalogue.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.catalogue.entity.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {
    
}