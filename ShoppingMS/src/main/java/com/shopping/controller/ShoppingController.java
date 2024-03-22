package com.shopping.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shopping.entity.CustomerCartMapping;
import com.shopping.entity.CustomerOrderMapping;
import com.shopping.repository.CustomerCartRepo;
import com.shopping.repository.CustomerOrderRepo;
import com.shopping.service.ShoppingService;
import com.shopping.serviceClients.CartClient;
import com.shopping.serviceClients.CategoryClient;
import com.shopping.serviceClients.CustomerClient;
import com.shopping.serviceClients.InventoryClient;
import com.shopping.serviceClients.OrderClient;
import com.shopping.serviceClients.ProductClient;
import com.shopping.serviceModels.Cart;
import com.shopping.serviceModels.CartRequest;
import com.shopping.serviceModels.Category;
import com.shopping.serviceModels.Customer;
import com.shopping.serviceModels.Inventory;
import com.shopping.serviceModels.LineItem;
import com.shopping.serviceModels.LineItemRequest;
import com.shopping.serviceModels.Order;
import com.shopping.serviceModels.Product;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/shoppingservice")
public class ShoppingController {
	
	@Autowired
	ShoppingService shoppingService;

	@Autowired
	ProductClient productClient;
	@Autowired
	CustomerClient customerClient;
	@Autowired
	InventoryClient inventoryClient;
	@Autowired
	CartClient cartClient;
	@Autowired
	OrderClient orderClient;
	
	@Autowired
	CategoryClient categoryClient;
	
	@Autowired
	CustomerCartRepo customerCartRepo;
	
	@Autowired
	CustomerOrderRepo customerOrderRepo;
	
	 @PersistenceContext
	 private EntityManager entityManager;
	
	@GetMapping("/check")
	public String check() {
		return "ShoppingController MS";
	}
	
	@PostMapping("/products")
	 @CircuitBreaker(name = "shoppingService",fallbackMethod = "fallBackProduct")
	public ResponseEntity<?> addingProductAndInventory(@RequestParam("productName") String productName,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("productPrice") double productPrice,
            @RequestParam("categoryID") Long categoryID,
            @RequestPart("photo") MultipartFile photo,@RequestParam("quantity") int quantity) {
	    // Step 1: Invoke the Product Microservice to create a product
		System.out.println("addingProductAndInventory()....");
		
        ResponseEntity<Product> productResponse = productClient.createProduct(productName,productDescription,productPrice,categoryID,photo);

        // Check if the product creation was successful
        if (productResponse.getStatusCode() == HttpStatus.CREATED) {
            Product createdProduct = productResponse.getBody();

            // Step 2: Invoke the Inventory Microservice to update inventory information
            Inventory inventory = new Inventory();
            inventory.setProductId(createdProduct.getProductId()); // Use the actual ID returned by the Product Microservice
            inventory.setQuantity(quantity); // Set the initial quantity as needed

            ResponseEntity<?> inventoryResponse = inventoryClient.addInventory(inventory);

            if (inventoryResponse.getStatusCode() == HttpStatus.CREATED) {
                // Both product creation and inventory  were successful
                return ResponseEntity.status(HttpStatus.CREATED).body("Product and Inventory created successfully");
            } else {
                // Handle the case where inventory update failed
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to add inventory");
            }
        } else {
            // Handle the case where product creation failed
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create product");
        }
		
	
		
	}
	
	@PostMapping("/customer")
	 @CircuitBreaker(name = "shoppingService",fallbackMethod = "fallBackCustomer")
	public ResponseEntity<?> addCustomerAndCart(@RequestBody Customer cus) {
		
		System.out.println("addCustomerAndCart :: " +cus);
		ResponseEntity<Customer> response = customerClient.addCustomer(cus);
		
		if(response.getStatusCode()== HttpStatus.CREATED) {
			
			Customer customer = response.getBody();
			
			Cart c = new Cart();
			ArrayList<LineItem> list = new ArrayList<LineItem>();
			LineItem lineItem = new LineItem();
			lineItem.setPrice(0);
			lineItem.setProductId(null);
			lineItem.setProductName(null);
			lineItem.setQuantity(0);
			c.setLineItems(list);
			list.add(lineItem);
			
			ResponseEntity<Cart> cartResponse = cartClient.addCart(c);
			if(cartResponse.getStatusCode()==HttpStatus.CREATED) {
				Cart emptyCart = cartResponse.getBody();
				CustomerCartMapping map=new CustomerCartMapping();
				map.setCartId(emptyCart.getCartId());
				map.setCustomerId(customer.getId());
				
				
			CustomerCartMapping mapping = customerCartRepo.save(map);
			return  ResponseEntity.status(HttpStatus.CREATED).body(mapping);
				
			}
			else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Cart");
			}
			
		}
		else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Customer");
		}
		
			
	
	}
	
	@PutMapping("/customer/{customerId}/cart")
	 @CircuitBreaker(name = "shoppingService",fallbackMethod = "fallBackCart")
	public ResponseEntity<?> addItemstoCart(@RequestBody CartRequest cartReq,@PathVariable("customerId") Long customerId) {
		
		System.out.println("addItemstoCart.. CartRequest :: "+cartReq);
		Optional<CustomerCartMapping> optional = customerCartRepo.findById(customerId);
		// check for cart exist or not
		if(optional.isPresent()) {
			CustomerCartMapping customerCartMapping = optional.get();
			Long cartId = customerCartMapping.getCartId();
			
			Cart existing_cart = cartClient.searchCart(cartId).getBody();// fetch existing cart
			
			List<LineItemRequest> lineItemsReq = cartReq.getLineItems();
			List<LineItem> oldItems = existing_cart.getLineItems();
				Iterator<LineItemRequest> iterator = lineItemsReq.iterator();
				
				
				List<LineItem> lineItems = new ArrayList<>();
				Cart c=new Cart();
				while(iterator.hasNext()) {
					LineItemRequest item = iterator.next();
					Long productId = item.getProductId();
					ResponseEntity<Product> productResp = productClient.getProductById(productId);
					if(productResp.getStatusCode()==HttpStatus.OK) {
						Product product = productResp.getBody();
						LineItem lineItem = new LineItem();
						lineItem.setProductId(productId);
						lineItem.setProductName(item.getProductName());
						lineItem.setQuantity(item.getQuantity());
						lineItem.setPrice(product.getProductPrice());
						lineItems.add(lineItem);
					}
					else {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product Not Found");
					}
				}
				c.setLineItems(lineItems);
			ResponseEntity<Cart> updatedCart = cartClient.updateCart(cartId,c);
			if(updatedCart.getStatusCode()==HttpStatus.OK) {
				Cart addedcart = updatedCart.getBody();
				return ResponseEntity.status(HttpStatus.OK).body("Cart updated :: "+addedcart);
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add Items to Cart");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Find Cart of Customer with id : "+customerId);
		
	}
	// below has a bug 
	/*
	@PutMapping("/customer/{customerId}/cart")
	public ResponseEntity<?> addItemstoCart(@RequestBody CartRequest cartReq,@PathVariable("customerId") Long customerId) {
		
		System.out.println("addItemstoCart.. CartRequest :: "+cartReq);
		Optional<CustomerCartMapping> optional = customerCartRepo.findById(customerId);
		// check for cart exist or not
		if(optional.isPresent()) {
			CustomerCartMapping customerCartMapping = optional.get();
			Long cartId = customerCartMapping.getCartId();
			System.out.println(cartId+"-----"+customerId);
			
			Cart existing_cart = cartClient.searchCart(cartId).getBody();// fetch existing cart
			System.out.println(existing_cart);
			if(cartReq.getLineItems().isEmpty()) {
				existing_cart.getLineItems().clear();       // emptying cart.
			}
			else {
				 List<LineItemRequest> lineItemsReq = cartReq.getLineItems();
		            List<LineItem> oldLineItems = existing_cart.getLineItems();
                             System.out.println(lineItemsReq);
                             System.out.println(oldLineItems);
		            for (LineItemRequest newItem : lineItemsReq) {
		                // Check if the product is already in the cart
		                boolean productExists = false;
		                for (LineItem oldItem : oldLineItems) {
		                    if (oldItem.getProductId() != null && oldItem.getProductId().equals(newItem.getProductId())) {
		                        // Increase the quantity if the product exists
		                    	oldItem.setQuantity(oldItem.getQuantity() + newItem.getQuantity());
		                        productExists = true;
		                        break;
		                    }
		                }

		                if (!productExists) {
		                    // Add the product as a new line item if it doesn't exist
		                    ResponseEntity<Product> productResp = productClient.getProductById(newItem.getProductId());
		                    if (productResp.getStatusCode() == HttpStatus.OK) {
		                        Product product = productResp.getBody();
		                        LineItem lineItem = new LineItem();
		                        lineItem.setProductId(newItem.getProductId());
		                        lineItem.setProductName(product.getProductName());
		                        lineItem.setQuantity(newItem.getQuantity());
		                        lineItem.setPrice(product.getProductPrice());
		                        oldLineItems.add(lineItem);// adding new item to old item
		                    } else {
		                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product Not Found");
		                    }
		                }
		            }
			}
			
			ResponseEntity<Cart> updatedCart = cartClient.updateCart(cartId,existing_cart);// updating old cart
			if(updatedCart.getStatusCode()==HttpStatus.OK) {
				Cart addedcart = updatedCart.getBody();
				return ResponseEntity.status(HttpStatus.OK).body("Cart updated :: "+addedcart);
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add Items to Cart");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to Find Cart of Customer with id : "+customerId);
		
	}
	*/
	 
	
	@PostMapping("/customer/{customerId}/order")
	 @CircuitBreaker(name = "shoppingService",fallbackMethod = "fallBack")
	 @Transactional 
	    public ResponseEntity<?> placeOrder(@PathVariable Long customerId) {
	        // Step 1: Retrieve the Customer's Cart ID from "Customer-Cart" table
	        Optional<CustomerCartMapping> customerCartMappingOptional = customerCartRepo.findById(customerId);

	        if (customerCartMappingOptional.isPresent()) {
	            CustomerCartMapping customerCartMapping = customerCartMappingOptional.get();
	            Long cartId = customerCartMapping.getCartId();

	            // Step 2: Fetch all the line items from the Cart
	            ResponseEntity<Cart> cartResponse = cartClient.searchCart(cartId);

	            if (cartResponse.getStatusCode() == HttpStatus.OK) {
	                Cart cart = cartResponse.getBody();
	                // Step 3: Ensure LineItems are managed
	                List<LineItem> cartItems = cart.getLineItems();
	                System.out.println("From Cart :"+cartItems);
	                // Step 3: Invoke the Order Microservice to create an order

	                // Step 4: Create and persist the Order
	                Order o = new Order();
	                List<LineItem> items = o.getLineItems();

	                for (LineItem cartItem : cartItems) {
	                    LineItem item = new LineItem();
	                    item.setPrice(cartItem.getPrice() * cartItem.getQuantity());
	                    item.setProductId(cartItem.getProductId());
	                    item.setProductName(cartItem.getProductName());
	                    item.setQuantity(cartItem.getQuantity());
	                    items.add(item);
	                }
	                System.out.println("Order Items : "+items);
	               // o.setLineItems(cartItems);
	              //  entityManager.persist(o);
	            
	                ResponseEntity<Order> orderResponse = orderClient.addOrder(o);

	                if (orderResponse.getStatusCode() == HttpStatus.CREATED) {
	                    Order order = orderResponse.getBody();
	                    System.out.println("Order Success :"+order);
	                    // Step 4: Update the Inventory Microservice
	                    // Loop through line items and update inventory
	                    for (LineItem lineItem : cart.getLineItems()) {
	                    	Inventory i=new Inventory();
	                    	i.setProductId(lineItem.getProductId());
	                    	ResponseEntity<Inventory> searchInventoryResp = inventoryClient.searchInventory(lineItem.getProductId());
	                    	if(searchInventoryResp.getStatusCode()==HttpStatus.OK) {
	                           Inventory inventory = searchInventoryResp.getBody();
	                           i.setQuantity(inventory.getQuantity()-lineItem.getQuantity());
	 
		                        inventoryClient.updateInventory(i);
	                    	}
	                    	
	                    }

	                    // Step 5: Maintain the mapping between Customer ID and Order ID
	                    CustomerOrderMapping existingMapping = entityManager.find(CustomerOrderMapping.class, customerId);
	                    if (existingMapping != null) {
	                        System.out.println("CustomerOrderMapping Before: " + customerId + " -- " + order.getOrderId());
	                        existingMapping.setCustomerId(customerId);
	                        List<Long> orderIds = existingMapping.getOrderIds();
	                        System.out.println(orderIds);
	                        orderIds.add(order.getOrderId());
	                        
	                        // Merge the changes back into the managed entity
	                        entityManager.merge(existingMapping);

	                        System.out.println(orderIds);
	                        System.out.println("CustomerOrderMapping: " + customerId + " -- " + order.getOrderId());
	                    } else {
	                        // Handle the case where the entity with customerId doesn't exist
	                        System.out.println("CustomerOrderMapping not found for customerId: " + customerId);
	                     // Step 7: Create a new CustomerOrderMapping if it doesn't exist
	                        CustomerOrderMapping newMapping = new CustomerOrderMapping();
	                        newMapping.setCustomerId(customerId);
	                       
	                        List<Long> orderIds = newMapping.getOrderIds();
	                     
	                        orderIds.add(order.getOrderId());
	                        System.out.println(orderIds);
	                        // Save the new mapping to the database
	                        CustomerOrderMapping save = customerOrderRepo.save(newMapping);

	                        
	                    }

	                    // Step 6: Invoke the Cart Microservice to empty the Cart
	                    
	                    Cart c = new Cart();
	        			ArrayList<LineItem> list = new ArrayList<LineItem>();
	        			LineItem lineItem = new LineItem();
	        			lineItem.setPrice(0);
	        			lineItem.setProductId(null);
	        			lineItem.setProductName(null);
	        			lineItem.setQuantity(0);
	        			c.setLineItems(list);
	        			list.add(lineItem);
	                    ResponseEntity<Cart> emptyCartResponse = cartClient.updateCart(cartId, c);

	                    if (emptyCartResponse.getStatusCode() == HttpStatus.OK) {
	                    	// send order-mail to the customer
	                    	System.out.println("ShoppingController  -  sendMail() ");
	                    	sendMail(customerId,order.getOrderId());
	                    	
	                        return ResponseEntity.status(HttpStatus.CREATED).body("Order placed successfully. :"+order.getOrderId());
	                    } else {
	                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to empty the Cart.");
	                    }
	                } else {
	                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create the Order.");
	                }
	            } else {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch Cart details.");
	            }
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with ID " + customerId + " not found.");
	        }
	        
	        
	    }
	
	
	
	//// sending-mail to customer
	
	private void sendMail(Long customerID,Long orderID) {
		ResponseEntity<Customer> customerResponse = customerClient.getCustomer(customerID);
		if(customerResponse.getStatusCode()== HttpStatus.OK) {
			Customer customer = customerResponse.getBody();
			String toEmail = customer.getCustomerEmail();
			  ResponseEntity<Order> orderResponse = orderClient.getOrder(orderID);
			Order orderDetails = orderResponse.getBody();
			shoppingService.sendMail(toEmail, orderDetails);
		}
		
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
	    	ResponseEntity<Order> addOrder =orderClient.addOrder(o);
	    
	        return ResponseEntity.status(HttpStatus.CREATED).body(addOrder.getBody());
	    }
	 
	 @GetMapping("/customer/{customerId}/orders")
	 @CircuitBreaker(name = "shoppingService",fallbackMethod = "fallBack")
	 public ResponseEntity<?> getCustomerOrders(@PathVariable Long customerId) {
	     // Step 1: Check if the customer exists
	     ResponseEntity<Customer> customerResponse = customerClient.getCustomer(customerId);

	     if (customerResponse.getStatusCode() == HttpStatus.OK) {
	         Customer customer = customerResponse.getBody();

	         // Step 2: Fetch the orderIds for the customer from Customer-Order table
	         Optional<CustomerOrderMapping> customerOrderMappingOptional = customerOrderRepo.findById(customerId);

	         if (customerOrderMappingOptional.isPresent()) {
	             CustomerOrderMapping customerOrderMapping = customerOrderMappingOptional.get();
	             List<Long> orderIds = customerOrderMapping.getOrderIds();

	             // Step 3: Fetch orders for the customer from Order Service
	             List<Order> customerOrders = new ArrayList<>();
	             for (Long orderId : orderIds) {
	                 ResponseEntity<Order> orderResponse = orderClient.getOrder(orderId);

	                 if (orderResponse.getStatusCode() == HttpStatus.OK) {
	                     customerOrders.add(orderResponse.getBody());
	                 } else {
	                     // Handle the case where fetching an order failed
	                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Failed to fetch orders for customer " + customerId);
	                 }
	             }

	             // Step 4: return orders response orders
	             return ResponseEntity.status(HttpStatus.OK).body(customerOrders);
	         } else {
	             // Handle the case where no order mapping is found for the customer
	             return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                     .body("No orders found for customer " + customerId);
	         }
	     } else {
	         // Handle the case where fetching customer details failed
	         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                 .body("Failed to fetch customer details for customer " + customerId);
	     }
	 }
	 
		
		public ResponseEntity<String> fallBack(Exception ex){
			  
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Order-Service is temporarily unavailable. "+ex.getMessage());
			
		}
			  public ResponseEntity<String> fallBackCategory(Exception ex){
				  
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Category-Service is temporarily unavailable. "+ex.getMessage());
					
				}
	public ResponseEntity<String> fallBackCart(Exception ex){
				  
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cart-Service is temporarily unavailable. "+ex.getMessage());
					
	}
	public ResponseEntity<String> fallBackCustomer(Exception ex){
		  
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Customer-Service is temporarily unavailable. "+ex.getMessage());
					
	}
	public ResponseEntity<String> fallBackProduct(Exception ex){
		  
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Product-Service is temporarily unavailable. "+ex.getMessage());
					
	}
			  
			
			  // get categories fallBackProduct
			  @GetMapping("/categories")
			  @CircuitBreaker(name = "shoppingService",fallbackMethod = "fallBackCategory")
			  public ResponseEntity<?> getAllCategories() {
				  
				  ResponseEntity<List<Category>> allCategories = categoryClient.getAllCategories();
				return new ResponseEntity<>(allCategories.getBody(),HttpStatus.OK);
				  
			  }
			  
			  @PostMapping("/categories")
			  public ResponseEntity<?> createCategory(@RequestBody Category c) {
				  
				 ResponseEntity<Category> category = categoryClient.createCategory(c);
				 if(category.getStatusCode()==HttpStatus.CREATED) {
					 return new ResponseEntity<>(category.getBody(),HttpStatus.CREATED);
				 }
				return new ResponseEntity<>("categeory not created",HttpStatus.INTERNAL_SERVER_ERROR);
				  
			  }
			  

}
