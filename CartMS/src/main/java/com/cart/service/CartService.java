package com.cart.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cart.entity.Cart;
import com.cart.entity.LineItem;
import com.cart.exception.CartNotFoundException;
import com.cart.exception.LineItemNotFoundException;
import com.cart.repository.CartRepository;
import com.cart.repository.LineItemRepository;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private LineItemRepository lineItemRepository;

    public Cart addCart(Cart cart) {
        // Add validation and business logic as needed
    	System.out.println("Request DATA : "+ cart);
        return cartRepository.save(cart);
    }

    @Transactional
    public void emptyCart(Long cartId) {
        // Implement logic to empty the cart (delete line items)
        // You can use lineItemRepository to remove line items associated with the cart
    	Optional<Cart> optional = searchCart(cartId);
    	if(optional.isPresent()) {
    		cartRepository.deleteById(cartId);
    	}
    	else {
    		throw new CartNotFoundException("Cart with ID " + cartId + " not found");
    	}
    }
    @Transactional//
    public Cart updateCart(Long cartId,Cart newCart) {
        // Implement logic to update the cart
        // You can use cartRepository.save(cart) to update the cart
    	Optional<Cart> optional = searchCart(cartId);
    	if(optional.isPresent()) {
    		Cart old = optional.get();
    		List<LineItem> oldLineItems = old.getLineItems();
    		List<LineItem> newLlineItems = newCart.getLineItems();
    		
    		oldLineItems.clear();
    		oldLineItems.addAll(newLlineItems);
    		
    		 return cartRepository.save(old);
    	}
    	else {
    		throw new CartNotFoundException("Cart with ID " + cartId + " not found to Update...");
    	}
    }
    public Optional<Cart> searchCart(Long cartId) {
        return cartRepository.findById(cartId);
    }
    @Transactional//
    public Cart addLineItem(Long cartId, LineItem lineItem) {
    	
    	System.out.println("addLineItem Request DATA : "+ lineItem);
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            // Implement validation and business logic as needed
            List<LineItem> lineItems = cart.getLineItems();
            lineItems.add(lineItem);
            return cartRepository.save(cart);
        } else {
            throw new CartNotFoundException("Cart with ID " + cartId + " not found");
        }
    }
    @Transactional//
    public Cart deleteLineItem(Long cartId, Long lineItemId) {
    	 Optional<Cart> optionalCart = cartRepository.findById(cartId);
    	    if (optionalCart.isPresent()) {
    	        Cart cart = optionalCart.get();
    	        
    	        // Use the LineItemRepository to delete the line item by ID
    	        lineItemRepository.deleteById(lineItemId);
    	        
    	        return cartRepository.save(cart);
    	    } else {
    	        throw new CartNotFoundException("Cart with ID " + cartId + " not found");
    	    }
    }
    @Transactional//
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

//    public Cart updateLineItem(Long cartId, LineItem lineItem) {
//        Optional<Cart> optionalCart = cartRepository.findById(cartId);
//        if (optionalCart.isPresent()) {
//            Cart cart = optionalCart.get();
//
//            // Find the line item by its ID in the cart's line items
//            Optional<LineItem> existingLineItem = cart.getLineItems().stream()
//                    .filter(item -> item.getItemId().equals(lineItem.getItemId()))
//                    .findFirst();
//
//            if (existingLineItem.isPresent()) {
//                LineItem updatedLineItem = existingLineItem.get();
//
//                // Update the properties of the existing line item with the new values
//                updatedLineItem.setProductId(lineItem.getProductId());
//                updatedLineItem.setProductName(lineItem.getProductName());
//                updatedLineItem.setQuantity(lineItem.getQuantity());
//                updatedLineItem.setPrice(lineItem.getPrice());
//
//                // Save the updated line item using the LineItemRepository
//                lineItemRepository.save(updatedLineItem);
//
//                // Save the cart to persist the changes
//                return cartRepository.save(cart);
//            } else {
//                throw new CartNotFoundException("LineItem with ID " + lineItem.getItemId() + " not found in cart");
//            }
//        } else {
//            throw new CartNotFoundException("Cart with ID " + cartId + " not found");
//        }
//    }
//
//    // ... other methods ...
//}
//    
    public Optional<LineItem> searchLineItem(Long lineItemId) {
        Optional<LineItem> lineItemOptional = lineItemRepository.findById(lineItemId);
        if (lineItemOptional.isPresent()) {
            return lineItemOptional;
        } else {
            throw new LineItemNotFoundException("LineItem with ID " + lineItemId + " not found");
        }
   
}
    }
