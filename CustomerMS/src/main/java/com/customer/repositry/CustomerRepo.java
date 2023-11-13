package com.customer.repositry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customer.entity.Customer;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
	
	Optional<Customer> findBycustomerEmail(String email);

}
