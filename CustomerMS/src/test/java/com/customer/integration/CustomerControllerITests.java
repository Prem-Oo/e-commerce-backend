package com.customer.integration;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import com.customer.entity.Customer;
import com.customer.entity.CustomerAddress;
import com.customer.repositry.CustomerRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CustomerControllerITests {

	@Autowired
	MockMvc mockmvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	CustomerRepo repo;

	@Transactional
	@Test
	public void testAddcustomer() throws Exception {
		
		//given
		CustomerAddress addr=new CustomerAddress(null, "2-10", "abc-street", "abc", "HYD", "12345");
		List<CustomerAddress> listAddress=new ArrayList<CustomerAddress>();
		listAddress.add(addr);
		
		Customer customer=new Customer();
		customer.setCustomerEmail("abc@gmail.com");
		customer.setCustomerName("abc");
		customer.setCustomerBillingAddresses(listAddress);
		customer.setCustomerShippingAddresses(listAddress);
		
		//when
		ResultActions response = mockmvc.perform(post("/customer/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(customer)));
		
		//verify
		response.andDo(print())
		        .andExpect(status().isCreated())
		        .andExpect(jsonPath("$.customerName", is("abc")))
		        .andExpect(jsonPath("$.customerEmail", is("abc@gmail.com")))
		        .andExpect(jsonPath("$.id", notNullValue()));
		
		
	}
	
	@Transactional
	@Test
	public void testGetAllCustomers() throws Exception {
		//given
		List<Customer> customerList=new ArrayList<Customer>();
		
		CustomerAddress addr=new CustomerAddress(null, "2-10", "abc-street", "abc", "HYD", "12345");
		List<CustomerAddress> listAddress=new ArrayList<CustomerAddress>();
		listAddress.add(addr);
		Customer customer1=new Customer(null,"prem","prem@gmail.com",listAddress,listAddress);
		Customer customer2=new Customer(null,"pavan","pavan@gmail.com",listAddress,listAddress);
		customerList.add(customer2);
		customerList.add(customer1);
		
		List<Customer> total=repo.findAll();
		
	      repo.saveAll(customerList);
		
		//when
		ResultActions response = mockmvc.perform(get("/customer/getAll"));
		
		//then -verify
		response.andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.length()", is(total.size()+2)));
		
		
	}
	
	@Transactional
	@Test
	public void testGetCustomerByID() throws Exception {
		
		//given
				CustomerAddress addr=new CustomerAddress(null, "2-10", "abc-street", "abc", "HYD", "12345");
				List<CustomerAddress> listAddress=new ArrayList<CustomerAddress>();
				listAddress.add(addr);
				
				Customer customer=new Customer();
				customer.setCustomerEmail("abc@gmail.com");
				customer.setCustomerName("abc");
				customer.setCustomerBillingAddresses(listAddress);
				customer.setCustomerShippingAddresses(listAddress);
						
		
				Customer savedCustomer = repo.save(customer);
		
		// when
		ResultActions response = mockmvc.perform(get("/customer/{customerId}",savedCustomer.getId()));
		
		//then
		response.andExpect(status().isOk())
		        .andExpect(jsonPath("$.id", is(Integer.parseInt(savedCustomer.getId().toString()) )))
		        .andExpect(jsonPath("$.customerName", is("abc")))
		        .andExpect(jsonPath("$.customerEmail", is("abc@gmail.com")));
		
	}
	
	@Transactional
	@Test
	public void testGetCustomerByID_NotFound() throws Exception {
		
		ResultActions response = mockmvc.perform(get("/customer/{customerId}",200l));
		
		response.andExpect(status().isNotFound());
		
	}
	@Transactional
	@Test
	public void testUpdateCustomer() throws Exception {
	    //given
	    CustomerAddress addr = new CustomerAddress(null, "2-10", "abc-street", "abc", "HYD", "12345");
	    List<CustomerAddress> listAddress = new ArrayList<>();
	    listAddress.add(addr);

	    Customer customer1=new Customer(null,"prem","prem@gmail.com",listAddress,listAddress);
	    Customer savedCustomer = repo.save(customer1);
	    
	    // Modify the existing savedCustomer object
	    savedCustomer.setCustomerName("pavan");
	    savedCustomer.setCustomerEmail("pavan@gmail.com");

	   
	    ResultActions response = mockmvc.perform(put("/customer/update/{id}", savedCustomer.getId())
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(mapper.writeValueAsString(savedCustomer)));

	    //then
	    response.andExpect(status().isOk())
	            .andExpect(content().string(containsString("Customer Updated Succesfully")));

	}
	
	@Transactional
	@Test
	public void testDeleteCustomer() throws Exception {
		
		 CustomerAddress addr = new CustomerAddress(null, "2-10", "abc-street", "abc", "HYD", "12345");
		    List<CustomerAddress> listAddress = new ArrayList<>();
		    listAddress.add(addr);

		    Customer customer1=new Customer(null,"prem","prem@gmail.com",listAddress,listAddress);
		    Customer savedCustomer = repo.save(customer1);
		
		ResultActions response = mockmvc.perform(delete("/customer/delete").param("id", savedCustomer.getId().toString()));
		response.andExpect(status().isOk())
        .andExpect(content().string("Deleted succesfully"));

        
	}
	
	@Transactional
	@Test
	public void testDeleteCustomer_NotFound() throws Exception {
		
		ResultActions response = mockmvc.perform(delete("/customer/delete").param("id", "1000"));
		response.andExpect(status().is4xxClientError());

        
	}
	
	@Transactional
	@Test
	public void testCheckCustomer() throws Exception {
	    //given
	    CustomerAddress addr = new CustomerAddress(null, "2-10", "abc-street", "abc", "HYD", "12345");
	    List<CustomerAddress> listAddress = new ArrayList<>();
	    listAddress.add(addr);

	    Customer customer = new Customer(null, "abc", "abc@gmail.com", listAddress, listAddress);
	    Customer savedCustomer = repo.save(customer);

	    // when
	    ResultActions response = mockmvc.perform(get("/customer/check").param("email", savedCustomer.getCustomerEmail()));

	    // then
	    response.andExpect(status().isOk())
	            .andExpect(jsonPath("$.customerEmail", is("abc@gmail.com")))
	            .andExpect(jsonPath("$.customerName", is("abc")));
	}

	@Transactional
	@Test
	public void testCheckCustomer_NotFound() throws Exception {
	    //given
	    String email = "xyz@gmail.com";

	    // when
	    ResultActions response = mockmvc.perform(get("/customer/check").param("email", email));

	    // then
	    response.andExpect(status().isNotFound());
	}

}
