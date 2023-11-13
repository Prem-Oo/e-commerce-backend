package com.catalogue.repositry;

import org.springframework.data.jpa.repository.JpaRepository;

import com.catalogue.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
