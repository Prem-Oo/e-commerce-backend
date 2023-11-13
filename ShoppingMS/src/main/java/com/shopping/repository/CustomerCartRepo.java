package com.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.CustomerCartMapping;

public interface CustomerCartRepo extends JpaRepository<CustomerCartMapping, Long> {

}
