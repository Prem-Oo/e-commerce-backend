package com.customer.service;

import java.util.List;

import com.customer.entity.Customer;

public interface CustomerService {

	Customer addCustomer(Customer customer);

	Customer getCustomerById(Long customerId);
	
	Customer checkCustomer(String email);

	List<Customer> getAllCustomers();

	String deleteCustomer(Long id);

	String updateCustomer(Long id, Customer customer);

}
