package com.catalogue.repositry;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catalogue.entity.Product;


public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByCategoryId(Long categoryId);

}