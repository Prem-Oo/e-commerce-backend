package com.shopping.serviceModels;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer {

	
	private Long id;
    private String customerName;
    private String customerEmail;
   
    private List<CustomerAddress> customerBillingAddresses;

   
    private List<CustomerAddress> customerShippingAddresses;

}
