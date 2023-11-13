package com.shopping.serviceClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shopping.serviceModels.Inventory;



@FeignClient(name = "Inventory-MS/inventory")
public interface InventoryClient {

	 @PostMapping("/add")
	    public ResponseEntity<Inventory> addInventory(@RequestBody Inventory inventory);
	       

	    @DeleteMapping("/delete/{productId}")
	    public ResponseEntity<String> deleteInventory(@PathVariable Long productId); 
	       

	    @PutMapping("/update")
	    public ResponseEntity<Inventory> updateInventory(@RequestBody Inventory inventory); 
	       

	    @GetMapping("/search/{productId}")
	    public ResponseEntity<Inventory> searchInventory(@PathVariable Long productId); 
	       
}
