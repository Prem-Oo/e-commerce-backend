package com.shopping.serviceClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shopping.serviceModels.Cart;
import com.shopping.serviceModels.LineItem;



@FeignClient(name = "Cart-MS/cart")
public interface CartClient {
	
	 @PostMapping
	    public ResponseEntity<Cart> addCart(@RequestBody Cart cart);
	       

	    @DeleteMapping("{cartId}")
	    public ResponseEntity<String> emptyCart(@PathVariable("cartId") Long cartId);
	       

	    @PutMapping("/{cartId}")
	    public ResponseEntity<Cart> updateCart(@PathVariable("cartId") Long cartId,@RequestBody Cart cart);
	       
	    @GetMapping("/{cartId}")
	    public ResponseEntity<Cart> searchCart(@PathVariable("cartId") Long cartId);
	       
	    
	    @PostMapping("/{cartId}/addItem")
	    public ResponseEntity<Cart> addLineItem(@PathVariable("cartId") Long cartId, @RequestBody LineItem lineItem);
	      
	    
	    @DeleteMapping("/{cartId}/deleteItem/{lineItemId}")
	    public ResponseEntity<Cart> deleteLineItem(@PathVariable("cartId") Long cartId, @PathVariable Long lineItemId);
	       
	    
	    @PutMapping("/{itemId}/updateItem")
	    public ResponseEntity<LineItem> updateLineItem(@PathVariable("itemId") Long itemId, @RequestBody LineItem lineItem);
	      
	    @GetMapping("/searchItem/{lineItemId}")
	    public ResponseEntity<LineItem> searchLineItem(@PathVariable Long lineItemId);

}
