package com.shopping.serviceClients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopping.serviceModels.Customer;



@FeignClient(name = "CUSTOMER-MS/customer")
public interface CustomerClient {
	
	 @PostMapping("/add")
	    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer);
	      

	    @GetMapping("/{customerId}")
	    public ResponseEntity<Customer> getCustomer(@PathVariable Long customerId);
	       

	    @GetMapping("/getAll")
	    public ResponseEntity<List<Customer>> getAllCustomers();
	      

	    @DeleteMapping("/delete")
	    public ResponseEntity<String> deleteCustomer(@RequestParam(required = false) Long id);
	    	
	    	
	  @PutMapping("/update/{id}")
	    public ResponseEntity<String> updateCustomer(@RequestBody Customer customer,@PathVariable("id") Long id);
	    	
	    	

}
