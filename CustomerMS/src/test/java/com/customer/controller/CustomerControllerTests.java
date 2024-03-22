package com.customer.controller;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import com.customer.entity.Customer;
import com.customer.entity.CustomerAddress;
import com.customer.service.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest
public class CustomerControllerTests {
	
	@Autowired
	private MockMvc mockmvc;
	
	@MockBean
	private CustomerServiceImpl customerService;
	
	@Autowired
	private ObjectMapper mapper;

	@Test
	public void testAddcustomer() throws Exception {
		
		//given
		CustomerAddress addr=new CustomerAddress(1l, "2-10", "abc-street", "abc", "HYD", "12345");
		List<CustomerAddress> listAddress=new ArrayList<CustomerAddress>();
		listAddress.add(addr);
		
		Customer customer=new Customer();
		customer.setCustomerEmail("abc@gmail.com");
		customer.setCustomerName("abc");
		customer.setId(1l);
		customer.setCustomerBillingAddresses(listAddress);
		customer.setCustomerShippingAddresses(listAddress);
		
		given(customerService.addCustomer(any(Customer.class)))
		.willAnswer((invocation)->invocation.getArgument(0));
		
		//when
		ResultActions response = mockmvc.perform(post("/customer/add")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(customer)));
		
		//verify
		response.andDo(print())
		        .andExpect(status().isCreated())
		        .andExpect(jsonPath("$.customerName", is("abc")))
		        .andExpect(jsonPath("$.customerEmail", is("abc@gmail.com")))
		        .andExpect(jsonPath("$.id", is(1)));
		
		
	}
	
	@Test
	public void testGetAllCustomers() throws Exception {
		//given
		List<Customer> customerList=new ArrayList<Customer>();
		
		CustomerAddress addr=new CustomerAddress(1l, "2-10", "abc-street", "abc", "HYD", "12345");
		List<CustomerAddress> listAddress=new ArrayList<CustomerAddress>();
		listAddress.add(addr);
		Customer customer1=new Customer(1l,"prem","prem@gmail.com",listAddress,listAddress);
		Customer customer2=new Customer(2l,"pavan","pavan@gmail.com",listAddress,listAddress);
		customerList.add(customer2);
		customerList.add(customer1);
		
		given(customerService.getAllCustomers()).willReturn(customerList);
		
		//when
		ResultActions response = mockmvc.perform(get("/customer/getAll"));
		
		//then -verify
		response.andDo(print())
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.length()",is(customerList.size())));
		
		
	}
	
	@Test
	public void testGetCustomerByID() throws Exception {
		
		//given
				CustomerAddress addr=new CustomerAddress(1l, "2-10", "abc-street", "abc", "HYD", "12345");
				List<CustomerAddress> listAddress=new ArrayList<CustomerAddress>();
				listAddress.add(addr);
				
				Customer customer=new Customer();
				customer.setCustomerEmail("abc@gmail.com");
				customer.setCustomerName("abc");
				customer.setId(1l);
				customer.setCustomerBillingAddresses(listAddress);
				customer.setCustomerShippingAddresses(listAddress);
						
		given(customerService.getCustomerById(anyLong())).willReturn(customer);
		
		// when
		ResultActions response = mockmvc.perform(get("/customer/{customerId}",customer.getId()));
		
		//then
		response.andExpect(status().isOk())
		        .andExpect(jsonPath("$.id", is(1)))
		        .andExpect(jsonPath("$.customerName", is("abc")))
		        .andExpect(jsonPath("$.customerEmail", is("abc@gmail.com")));
		
	}
	@Test
	public void testGetCustomerByID_NotFound() throws Exception {
		
		//given
						
		given(customerService.getCustomerById(anyLong())).willReturn(null);
		
		// when
		ResultActions response = mockmvc.perform(get("/customer/{customerId}",2l));
		
		//then
		response.andExpect(status().isNotFound());
		
	}
	@Test
	public void testUpdateCustomer() throws Exception {
	    //given
	    CustomerAddress addr = new CustomerAddress(1L, "2-10", "abc-street", "abc", "HYD", "12345");
	    List<CustomerAddress> listAddress = new ArrayList<>();
	    listAddress.add(addr);

	    Customer updatedCustomer = new Customer();
	    updatedCustomer.setCustomerEmail("prem@gmail.com");
	    updatedCustomer.setCustomerName("prem");
	    updatedCustomer.setId(1L);
	    updatedCustomer.setCustomerBillingAddresses(listAddress);
	    updatedCustomer.setCustomerShippingAddresses(listAddress);

	    given(customerService.updateCustomer(eq(1L), any(Customer.class))).willAnswer(invocation -> {
	        Customer oldCustomer = invocation.getArgument(1); // Get the second argument which is the updated customer
	        // Update the existing customer object with the new values
	        oldCustomer.setCustomerName(updatedCustomer.getCustomerName());
	        oldCustomer.setCustomerEmail(updatedCustomer.getCustomerEmail());
	        oldCustomer.setCustomerBillingAddresses(updatedCustomer.getCustomerBillingAddresses());
	        oldCustomer.setCustomerShippingAddresses(updatedCustomer.getCustomerShippingAddresses());
	        return "Customer Updated Successfully";
	    });
	    //when
	    ResultActions response = mockmvc.perform(put("/customer/update/{id}", 1L)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(mapper.writeValueAsString(updatedCustomer)));

	    //then
	    response.andExpect(status().isOk())
	            .andExpect(content().string(containsString("Customer Updated Successfully")));

	    verify(customerService).updateCustomer(1L, updatedCustomer);
	}
	
	@Test
	public void testDeleteCustomer() throws Exception {
		
		given(customerService.deleteCustomer(1l)).willReturn("Deleted succesfully");
		
		ResultActions response = mockmvc.perform(delete("/customer/delete").param("id", "1"));
		response.andExpect(status().isOk())
        .andExpect(content().string("Deleted succesfully"));

          verify(customerService).deleteCustomer(1l);
	}
	
	@Test
	public void testCheckCustomer() throws Exception {
	    //given
	    CustomerAddress addr = new CustomerAddress(1L, "2-10", "abc-street", "abc", "HYD", "12345");
	    List<CustomerAddress> listAddress = new ArrayList<>();
	    listAddress.add(addr);

	    Customer customer = new Customer();
	    customer.setCustomerEmail("abc@gmail.com");
	    customer.setCustomerName("abc");
	    customer.setId(1L);
	    customer.setCustomerBillingAddresses(listAddress);
	    customer.setCustomerShippingAddresses(listAddress);

	    given(customerService.checkCustomer(anyString())).willReturn(customer);

	    // when
	    ResultActions response = mockmvc.perform(get("/customer/check")
	            .param("email", "abc@gmail.com"));

	    //then
	    response.andExpect(status().isOk())
	            .andExpect(jsonPath("$.id", is(1)))
	            .andExpect(jsonPath("$.customerName", is("abc")))
	            .andExpect(jsonPath("$.customerEmail", is("abc@gmail.com")));
	}

	@Test
	public void testCheckCustomer_NotFound() throws Exception {
	    //given
	    given(customerService.checkCustomer(anyString())).willReturn(null);

	    // when
	    ResultActions response = mockmvc.perform(get("/customer/check")
	            .param("email", "abc@gmail.com"));

	    //then
	    response.andExpect(status().isNotFound());
	}

}
