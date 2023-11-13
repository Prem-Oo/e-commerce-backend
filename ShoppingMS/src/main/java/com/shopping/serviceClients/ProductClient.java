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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.shopping.serviceModels.Product;



@FeignClient(name = "Products-MS/product")
public interface ProductClient {

	 @GetMapping
	    public ResponseEntity< List<Product>> getAllProducts();

	    @GetMapping("/{productId}")
	    public ResponseEntity<Product> getProductById(@PathVariable("productId") Long productId);

	    @PostMapping(consumes = "multipart/form-data")
	    public ResponseEntity<Product> createProduct(@RequestParam("productName") String productName,
	            @RequestParam("productDescription") String productDescription,
	            @RequestParam("productPrice") double productPrice,  @RequestParam("categoryID") Long categoryID,
	            @RequestPart("photo") MultipartFile photo);

	    @PutMapping("/{productId}")
	    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Long productId, @RequestBody Product updatedProduct);

	    @DeleteMapping("/{productId}")
	    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId);
}
