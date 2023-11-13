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
public class ProductRequest {


    private Long productId;
    
    private String productName;
    private String productDescription;
    private double productPrice;
    
    private Category category;
    
    private byte[] photo;
    
    private int quantity;
    
    
}
