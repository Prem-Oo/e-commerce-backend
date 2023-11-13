package com.shopping.serviceModels;

import java.util.ArrayList;
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
public class Cart {
    
    private Long cartId;

    private List<LineItem> lineItems = new ArrayList<LineItem>();

   
}
