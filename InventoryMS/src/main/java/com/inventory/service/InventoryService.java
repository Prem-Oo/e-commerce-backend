package com.inventory.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.entity.Inventory;
import com.inventory.exception.InventoryNotFoundException;
import com.inventory.repository.InventoryRepository;

@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional//
    public Inventory addInventory(Inventory inventory) {
    	Optional<Inventory> optional = searchInventory(inventory.getProductId());
    	if(optional.isPresent()) {
    		throw new InventoryNotFoundException("Inventory with Product-ID "+inventory.getProductId()+" Already Existed!! Please Update..");
    	}
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public void deleteInventory(Long productId) {
    	Optional<Inventory> optional = searchInventory(productId);
    	if(optional.isPresent()) {
    		inventoryRepository.deleteByProductId(productId);
    	}
    	else {
    		throw new InventoryNotFoundException("Inventory with Product-ID "+productId+" Not there to Delete");
    	}
    }
    @Transactional//
    public Inventory updateInventory(Inventory inventory)  {
    	
    		Optional<Inventory> optional = searchInventory(inventory.getProductId());
        	if(optional.isPresent()) {
        		// only update quantity from new to old and save it.
        		Inventory old = optional.get();
        		//testing locking
/*       		if(inventory.getQuantity()==20) {
        			  try {
						Thread.sleep(20000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        		}
 
 */

        		old.setQuantity(inventory.getQuantity());
        		return inventoryRepository.save(old);
        	}
        	else {
        		throw new InventoryNotFoundException("Inventory with Product-ID "+inventory.getProductId()+" Not there to Update");
        	}
        
    }

    public Optional<Inventory> searchInventory(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }
}
