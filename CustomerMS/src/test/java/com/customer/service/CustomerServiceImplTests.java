package com.customer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.customer.entity.Customer;
import com.customer.entity.CustomerAddress;
import com.customer.exception.CustomerNotFoundException;
import com.customer.repositry.CustomerRepo;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTests {
	
	@Mock
	CustomerRepo customerRepo;
	
	@InjectMocks
	CustomerServiceImpl customerService;
	
	Customer customer;
	
	static CustomerAddress addr;
	
	@BeforeAll
	public static void init() {
		addr=new CustomerAddress(1l, "2-10", "abc-street", "abc", "HYD", "12345");
	}
	
	@BeforeEach
	public void setup() {
		List<CustomerAddress> listAddress=new ArrayList<CustomerAddress>();
		listAddress.add(addr);
		
		customer=new Customer();
		customer.setCustomerEmail("abc@gmail.com");
		customer.setCustomerName("abc");
		customer.setId(1l);
		customer.setCustomerBillingAddresses(listAddress);
		customer.setCustomerShippingAddresses(listAddress);
		
	}
	
	@Test
	public void testAddCustomer() {
		//given
		given(customerRepo.save(customer)).willReturn(customer);
		
		System.out.println(customerRepo);
		System.out.println(customerService);
		//when
		Customer savedCustomer = customerService.addCustomer(customer);
		System.out.println(savedCustomer);
		//then
		assertThat(savedCustomer).isNotNull();
		
	}
	@Test
	public void testGetCustomerByID() {
		//given
		given(customerRepo.findById(1l)).willReturn(Optional.of(customer));
		//when
		Customer savedCustomer = customerService.getCustomerById(1l);
		System.out.println(savedCustomer);
		//then
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isEqualTo(1l);
		
	}
	@Test
	public void testGetCustomerByID_throwsException() {
		//given
		given(customerRepo.findById(anyLong())).willReturn(Optional.empty());
		//when
		assertThrows(CustomerNotFoundException.class, ()->customerService.getCustomerById(1l) );
		
	}
	
	@Test
	public void testGetAllCustomers() {
		List<CustomerAddress> listAddress=new ArrayList<CustomerAddress>();
		listAddress.add(addr);
		
		Customer customer2=new Customer();
		customer2.setCustomerEmail("xyz@gmail.com");
		customer2.setCustomerName("xyz");
		customer2.setId(2l);
		customer2.setCustomerBillingAddresses(listAddress);
		customer2.setCustomerShippingAddresses(listAddress);
		
		given(customerRepo.findAll()).willReturn(List.of(customer,customer2));
		
		List<Customer> customers = customerService.getAllCustomers();
		
		assertThat(customers).isNotNull();
		assertThat(customers.size()).isEqualTo(2);
	}
	
	@Test
	public void testGetAllCustomers_EmptyList() {
		
		given(customerRepo.findAll()).willReturn(Collections.emptyList());
		
		List<Customer> customers = customerService.getAllCustomers();
		
		assertThat(customers).isEmpty();;
		assertThat(customers.size()).isEqualTo(0);
	}
	
	@Test
	public void testUpdateCustomer() {
		
		 given(customerRepo.findById(1l)).willReturn(Optional.of(customer));
		 given(customerRepo.save(customer)).willReturn(customer); // Mocking the save method
		
		customer.setCustomerName("prem");
		customer.setCustomerEmail("prem@gmail.com");
		
		String response = customerService.updateCustomer(1l, customer);
		
		assertThat(response).isEqualTo("Customer Updated Succesfully");
		verify(customerRepo).save(customer);
		
	}
	@Test
	public void testUpdateCustomer_NotFound() {
		
		 given(customerRepo.findById(2l)).willReturn(Optional.empty());
		
		String response = customerService.updateCustomer(2l, customer);
		
		assertThat(response).isEqualTo("Customer Not Found");
		verify(customerRepo,never()).save(customer);
		
	}
	
	@Test
	public void testDeleteCustomerByID() {
		
		given(customerRepo.findById(1l)).willReturn(Optional.of(customer));
		
		String response = customerService.deleteCustomer(1l);
		
		assertThat(response).isEqualTo("Deleted succesfully");
		verify(customerRepo).deleteById(1l);;
		
	}
	
	@Test
	public void testDeleteCustomerByID_NotFound() {
		
		given(customerRepo.findById(2l)).willReturn(Optional.empty());
		
		assertThrows(CustomerNotFoundException.class, ()->customerService.deleteCustomer(2l) );
		
	}
	
	@Test
	public void testDeleteAllCustomer() {
		
		 String response = customerService.deleteCustomer(null);
		 assertThat(response).isEqualTo("All Customers Deleted succesfully");
		 verify(customerRepo).deleteAll();
		
	}
	
	@Test
	public void testCheckCustomer() {
	    //given
	    String email = "abc@gmail.com";
	    Customer customer = new Customer();
	    customer.setCustomerEmail(email);
	    given(customerRepo.findBycustomerEmail(email)).willReturn(Optional.of(customer));

	    //when
	    Customer foundCustomer = customerService.checkCustomer(email);

	    //then
	    assertThat(foundCustomer).isNotNull();
	    assertThat(foundCustomer.getCustomerEmail()).isEqualTo(email);
	}

	@Test
	public void testCheckCustomer_NotFound() {
	    //given
	    String email = "abc@gmail.com";
	    given(customerRepo.findBycustomerEmail(email)).willReturn(Optional.empty());

	    //when & then
	    assertThrows(CustomerNotFoundException.class, () -> customerService.checkCustomer(email));
	}

	

}
