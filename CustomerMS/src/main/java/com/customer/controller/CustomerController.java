package com.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.customer.entity.Customer;
import com.customer.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
    private CustomerService customerService;

//	@CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/add")
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
    	System.out.println(customer);
        Customer createdCustomer = customerService.addCustomer(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
    }

//	  @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	  
//	  @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/check")
    public ResponseEntity<Customer> checkCustomer(@RequestParam String email) {
        Customer customer = customerService.checkCustomer(email);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
//	  @CrossOrigin(origins = "http://localhost:3000")
    
    @GetMapping("/getAll")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
//	  @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCustomer(@RequestParam(required = false) Long id) {
    	
    	String result = customerService.deleteCustomer(id);
		return new ResponseEntity<String>(result, HttpStatus.OK);
      
    }
//	  @CrossOrigin(origins = "http://localhost:3000")
  @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCustomer(@RequestBody Customer customer,@PathVariable("id") Long id) {
    	
    	String result = customerService.updateCustomer(id,customer);
		return new ResponseEntity<String>(result, HttpStatus.OK);
      
    }
	
}
