package com.order.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.order.entity.LineItem;
import com.order.entity.Order;
import com.order.exception.LineItemNotFoundException;
import com.order.exception.OrderNotFoundException;
import com.order.repository.LineItemRepository;
import com.order.repository.OrderRepository;

@Service
public class OrderService {

	 @Autowired
	    private OrderRepository orderRepository;
	 @Autowired
	private  LineItemRepository lineItemRepository;

	 @Transactional
	    public Order addOrder(Order order) {
	        // Add validation and business logic as needed
		 System.out.println("OrderService-addOrder "+order);
	        return orderRepository.save(order);
	    }
	 @Transactional
	    public void deleteOrder(Long orderId) {
	        // Implement logic to delete an order by ID
	        orderRepository.deleteById(orderId);
	    }
	    @Transactional
	    public Order updateOrder(Order order) {
	        // Implement logic to update an order
	        // You can use orderRepository.save(order) to update the order
	        return orderRepository.save(order);
	    }
	    @Transactional
	    public Optional<Order> searchOrder(Long orderId) {
	        return orderRepository.findById(orderId);
	    }
	    @Transactional
	    public List<Order> getAllOrders() {
	        // Implement logic to retrieve all orders
	        return orderRepository.findAll();
	    }
	    ////////////
	    public Order addLineItem(Long OrderId, LineItem lineItem) {
	    	
	    	System.out.println("addLineItem Request DATA : "+ lineItem);
	        Optional<Order> optionalOrder = orderRepository.findById(OrderId);
	        if (optionalOrder.isPresent()) {
	            Order Order = optionalOrder.get();
	            // Implement validation and business logic as needed
	            List<LineItem> lineItems = Order.getLineItems();
	            lineItems.add(lineItem);
	            return orderRepository.save(Order);
	        } else {
	            throw new OrderNotFoundException("Order with ID " + OrderId + " not found");
	        }
	    }
	    
	    public Order deleteLineItem(Long OrderId, Long lineItemId) {
	    	 Optional<Order> optionalOrder = orderRepository.findById(OrderId);
	    	    if (optionalOrder.isPresent()) {
	    	        Order Order = optionalOrder.get();
	    	        
	    	        // Use the LineItemRepository to delete the line item by ID
	    	        lineItemRepository.deleteById(lineItemId);
	    	        
	    	        return orderRepository.save(Order);
	    	    } else {
	    	        throw new OrderNotFoundException("Order with ID " + OrderId + " not found");
	    	    }
	    }
	    
	    public LineItem updateLineItem(Long itemId, LineItem lineItem) {
	        // First, retrieve the line item by its ID
	        Optional<LineItem> optionalLineItem = lineItemRepository.findById(itemId);

	        if (optionalLineItem.isPresent()) {
	            LineItem existingLineItem = optionalLineItem.get();

	            // Update the properties of the existing line item with the new values
	            existingLineItem.setProductId(lineItem.getProductId());
	            existingLineItem.setProductName(lineItem.getProductName());
	            existingLineItem.setQuantity(lineItem.getQuantity());
	            existingLineItem.setPrice(lineItem.getPrice());

	            // Save the updated line item using the LineItemRepository
	           
	            return   lineItemRepository.save(existingLineItem);
	        } else {
	            throw new LineItemNotFoundException("LineItem with ID " + itemId + " not found");
	        }
	    }


	    public Optional<LineItem> searchLineItem(Long lineItemId) {
	        Optional<LineItem> lineItemOptional = lineItemRepository.findById(lineItemId);
	        if (lineItemOptional.isPresent()) {
	            return lineItemOptional;
	        } else {
	            throw new LineItemNotFoundException("LineItem with ID " + lineItemId + " not found");
	        }
	   
	}
}
