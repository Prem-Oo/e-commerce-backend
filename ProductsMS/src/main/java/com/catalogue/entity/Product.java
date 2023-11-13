package com.catalogue.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
    
    @ManyToOne(targetEntity = Category.class)
    @JsonBackReference
    private Category category;
    
    @Lob
    private byte[] photo;
    
}
