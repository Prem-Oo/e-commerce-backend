package com.customer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.entity.Customer;
import com.customer.entity.CustomerAddress;
import com.customer.exception.CustomerNotFoundException;
import com.customer.repositry.CustomerRepo;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerRepo customerRepo;

	@Override
	public Customer addCustomer(Customer customer) {
		
		return  customerRepo.save(customer);
	}

	@Override
	public Customer getCustomerById(Long customerId) {
		
		return  customerRepo.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("customer with "+ customerId+" Not Found"));
	}

	@Override
	public List<Customer> getAllCustomers() {
		return customerRepo.findAll();
	}


	@Override
	public String deleteCustomer(Long id) {
		if(id!=null) {
			// delete by ID
			if(customerRepo.findById(id).isPresent()) {
				customerRepo.deleteById(id);
				return "Deleted succesfully";
			}
			else {
				throw new CustomerNotFoundException("customer with "+ id+" Not Found");
			}
			
		}
	
			customerRepo.deleteAll();
			return "All Customers Deleted succesfully";
		
	}

	@Override
	public String updateCustomer(Long id,Customer newCustomer) {
		Optional<Customer> c = customerRepo.findById(id);
		if(c.isPresent()) {
			 Customer existingCustomer = c.get();
		        System.out.println("OLD Customer: " + existingCustomer);

		        // Update customer properties
		        existingCustomer.setCustomerName(newCustomer.getCustomerName());
		        existingCustomer.setCustomerEmail(newCustomer.getCustomerEmail());

		        // Update billing address if new billing address is provided
		        if (newCustomer.getCustomerBillingAddresses() != null) {
		            List<CustomerAddress> existingBillingAddresses = existingCustomer.getCustomerBillingAddresses();
		            List<CustomerAddress> newBillingAddresses = newCustomer.getCustomerBillingAddresses();
		            existingBillingAddresses.clear();
		            existingBillingAddresses.addAll(newBillingAddresses);
		        }

		        // Update shipping address if new shipping address is provided
		        if (newCustomer.getCustomerShippingAddresses() != null) {
		            List<CustomerAddress> existingShippingAddresses = existingCustomer.getCustomerShippingAddresses();
		            List<CustomerAddress> newShippingAddresses = newCustomer.getCustomerShippingAddresses();
		            existingShippingAddresses.clear();
		            existingShippingAddresses.addAll(newShippingAddresses);
		        }

		        // Save the updated customer
		        Customer saved = customerRepo.save(existingCustomer);
		        System.out.println("Saved Customer: " + saved);

			return "Customer Updated Succesfully";
		}
		return "Customer Not Found";
	}

	@Override
	public Customer checkCustomer(String email) {
	
		return  customerRepo.findBycustomerEmail(email).orElseThrow(()-> new CustomerNotFoundException("customer with "+ email+" Not Found"));
	}

}
