package com.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.CustomerOrderMapping;

public interface CustomerOrderRepo extends JpaRepository<CustomerOrderMapping, Long> {

}
