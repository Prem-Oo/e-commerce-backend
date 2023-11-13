package com.inventory.controller;

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

import com.inventory.entity.Inventory;
import com.inventory.exception.InventoryNotFoundException;
import com.inventory.service.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory) {
        Inventory addedInventory = inventoryService.addInventory(inventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedInventory);
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteInventory(@PathVariable Long productId) {
        inventoryService.deleteInventory(productId);
        return ResponseEntity.ok("Inventory deleted successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<Inventory> updateInventory(@RequestBody Inventory inventory) {
        Inventory updatedInventory = inventoryService.updateInventory(inventory);
        return ResponseEntity.ok(updatedInventory);
    }

    @GetMapping("/search/{productId}")
    public ResponseEntity<Inventory> searchInventory(@PathVariable Long productId) {
        Optional<Inventory> inventory = inventoryService.searchInventory(productId);
        if (inventory.isPresent()) {
            return ResponseEntity.ok(inventory.get());
        } else {
            throw new InventoryNotFoundException("Inventory with Product-ID "+productId+" Not there");
        }
    }
}
