package com.shopping.serviceClients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shopping.serviceModels.LineItem;
import com.shopping.serviceModels.Order;


@FeignClient(name = "Order-MS/order")
public interface OrderClient {

	    @PostMapping
	    public ResponseEntity<Order> addOrder(@RequestBody Order order); 
	       
	    @GetMapping("/{orderId}")
	    public ResponseEntity<Order> getOrder(@PathVariable Long orderId); 
	       
	    @GetMapping("/all")
	    public ResponseEntity<List<Order>> getAllOrders(); 
	      

	    @PutMapping("/{orderId}")
	    public ResponseEntity<Order> updateOrder(@PathVariable Long orderId, @RequestBody Order updatedOrder);

	    @DeleteMapping("/{orderId}")
	    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId);
	    
	    @PostMapping("/{OrderId}/addItem")
	    public ResponseEntity<Order> addLineItem(@PathVariable("OrderId") Long OrderId, @RequestBody LineItem lineItem); 
	     
	    
	    @DeleteMapping("/{OrderId}/deleteItem/{lineItemId}")
	    public ResponseEntity<Order> deleteLineItem(@PathVariable("OrderId") Long OrderId, @PathVariable Long lineItemId); 
	     
	    
	    @PutMapping("/{itemId}/updateItem")
	    public ResponseEntity<LineItem> updateLineItem(@PathVariable("itemId") Long itemId, @RequestBody LineItem lineItem); 
	     
	    @GetMapping("/searchItem/{lineItemId}")
	    public ResponseEntity<LineItem> searchLineItem(@PathVariable Long lineItemId); 
	      
}
