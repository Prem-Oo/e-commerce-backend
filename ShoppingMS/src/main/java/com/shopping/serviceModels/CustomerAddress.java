package com.shopping.serviceModels;

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
public class CustomerAddress {

    
    private Long id;
    private String doorNo;
    private String streetName;
    private String layout;
    private String city;
    private String pincode;

   
}