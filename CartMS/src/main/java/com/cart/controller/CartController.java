package com.cart.controller;

import java.util.Optional;

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

import com.cart.entity.Cart;
import com.cart.entity.LineItem;
import com.cart.exception.CartNotFoundException;
import com.cart.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> addCart(@RequestBody Cart cart) {
        Cart createdCart = cartService.addCart(cart);
        System.out.println("DB :"+createdCart);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCart);
    }

    @DeleteMapping("{cartId}")
    public ResponseEntity<String> emptyCart(@PathVariable("cartId") Long cartId) {
        cartService.emptyCart(cartId);
        return ResponseEntity.ok("Cart emptied successfully");
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<Cart> updateCart(@PathVariable("cartId") Long cartId,@RequestBody Cart cart) {
        Cart updatedCart = cartService.updateCart(cartId,cart);
        return ResponseEntity.ok(updatedCart);
    }
    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> searchCart(@PathVariable("cartId") Long cartId) {
        Optional<Cart> cart = cartService.searchCart(cartId);
        if (cart.isPresent()) {
            return ResponseEntity.ok(cart.get());
        } else {
        	throw new CartNotFoundException("Cart with ID " + cartId + " not found ");
        }
    }
    
    @PostMapping("/{cartId}/addItem")
    public ResponseEntity<Cart> addLineItem(@PathVariable("cartId") Long cartId, @RequestBody LineItem lineItem) {
        Cart updatedCart = cartService.addLineItem(cartId, lineItem);
        return ResponseEntity.ok(updatedCart);
    }
    
    @DeleteMapping("/{cartId}/deleteItem/{lineItemId}")
    public ResponseEntity<Cart> deleteLineItem(@PathVariable("cartId") Long cartId, @PathVariable Long lineItemId) {
        Cart updatedCart = cartService.deleteLineItem(cartId, lineItemId);
        return ResponseEntity.ok(updatedCart);
    }
    
    @PutMapping("/{itemId}/updateItem")
    public ResponseEntity<LineItem> updateLineItem(@PathVariable("itemId") Long itemId, @RequestBody LineItem lineItem) {
        LineItem updatedCart = cartService.updateLineItem(itemId, lineItem);
        return ResponseEntity.ok(updatedCart);
    }
    @GetMapping("/searchItem/{lineItemId}")
    public ResponseEntity<LineItem> searchLineItem(@PathVariable Long lineItemId) {
        Optional<LineItem> lineItem = cartService.searchLineItem(lineItemId);
        if (lineItem.isPresent()) {
            return ResponseEntity.ok(lineItem.get());
        } else {
        	throw new CartNotFoundException("LineItem with ID " + lineItemId + " not found in cart");
        }
    }
}