package com.order.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.order.entity.LineItem;
import com.order.entity.Order;
import com.order.exception.OrderNotFoundException;
import com.order.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

	 @Autowired
	    private OrderService orderService;

	    @PostMapping
	    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
	    	System.out.println("addOrder : "+order);
	        Order createdOrder = orderService.addOrder(order);
	        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
	    }

	    @GetMapping("/test")
	    public ResponseEntity<Order> test() {
//	    	System.out.println("addOrder : "+order);
	    	
	    	 Order o = new Order();
             List<LineItem> items = o.getLineItems();

            LineItem lineItem = new LineItem();
            lineItem.setPrice(100.0);
            lineItem.setProductId(1L);
            lineItem.setProductName("Bike");
            lineItem.setQuantity(10);
            
            items.add(lineItem);
             System.out.println("Order Items : "+items);
	    	ResponseEntity<Order> addOrder = addOrder(o);
	    
	        return ResponseEntity.status(HttpStatus.CREATED).body(addOrder.getBody());
	    }
	    @GetMapping("/{orderId}")
	    public ResponseEntity<Order> getOrder(@PathVariable Long orderId) {
	        Optional<Order> order = orderService.searchOrder(orderId);
	        if (order.isPresent()) {
	            return ResponseEntity.ok(order.get());
	        } else {
	            throw new OrderNotFoundException("Order with ID " + orderId + " not found");
	        }
	    }

	    @GetMapping("/all")
	    public ResponseEntity<List<Order>> getAllOrders() {
	        List<Order> orders = orderService.getAllOrders();
	        return ResponseEntity.ok(orders);
	    }

	    @PutMapping("/{orderId}")
	    public ResponseEntity<Order> updateOrder(@PathVariable Long orderId, @RequestBody Order updatedOrder) {
	        updatedOrder.setOrderId(orderId);
	        Order updated = orderService.updateOrder(updatedOrder);
	        return ResponseEntity.ok(updated);
	    }

	    @DeleteMapping("/{orderId}")
	    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
	        orderService.deleteOrder(orderId);
	        return ResponseEntity.noContent().build();
	    }
	    /////////
	    
	    @PostMapping("/{OrderId}/addItem")
	    public ResponseEntity<Order> addLineItem(@PathVariable("OrderId") Long OrderId, @RequestBody LineItem lineItem) {
	        Order updatedOrder = orderService.addLineItem(OrderId, lineItem);
	        return ResponseEntity.ok(updatedOrder);
	    }
	    
	    @DeleteMapping("/{OrderId}/deleteItem/{lineItemId}")
	    public ResponseEntity<Order> deleteLineItem(@PathVariable("OrderId") Long OrderId, @PathVariable Long lineItemId) {
	        Order updatedOrder = orderService.deleteLineItem(OrderId, lineItemId);
	        return ResponseEntity.ok(updatedOrder);
	    }
	    
	    @PutMapping("/{itemId}/updateItem")
	    public ResponseEntity<LineItem> updateLineItem(@PathVariable("itemId") Long itemId, @RequestBody LineItem lineItem) {
	        LineItem updatedOrder = orderService.updateLineItem(itemId, lineItem);
	        return ResponseEntity.ok(updatedOrder);
	    }
	    @GetMapping("/searchItem/{lineItemId}")
	    public ResponseEntity<LineItem> searchLineItem(@PathVariable Long lineItemId) {
	        Optional<LineItem> lineItem = orderService.searchLineItem(lineItemId);
	        if (lineItem.isPresent()) {
	            return ResponseEntity.ok(lineItem.get());
	        } else {
	        	throw new OrderNotFoundException("LineItem with ID " + lineItemId + " not found in Order");
	        }
	    }
}
