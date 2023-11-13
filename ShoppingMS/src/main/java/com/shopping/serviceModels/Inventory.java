package com.shopping.serviceModels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

   
    private Long inventoryId;

    private Long productId;
    
    private int quantity;
}
