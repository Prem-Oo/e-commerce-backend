package com.catalogue.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    
    private String productName;
    private String productDescription;
    private double productPrice;
    
   
    
}
