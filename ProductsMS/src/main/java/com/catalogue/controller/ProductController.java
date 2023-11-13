package com.catalogue.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.catalogue.entity.Category;
import com.catalogue.entity.Product;
import com.catalogue.repositry.CategoryRepository;
import com.catalogue.service.ProductService;
import com.catalogue.util.ImageUtils;

@RestController
@RequestMapping("/product")
//@CrossOrigin(origins = "http://localhost:3000" )
public class ProductController {
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CategoryRepository categoryRepository; 

    @GetMapping
    public ResponseEntity< List<Product>> getAllProducts() {
    	List<Product> products = productService.getAllProducts();

        // Decompress the image data for each product
        for (Product product : products) {
            byte[] decompressedImage = ImageUtils.decompressImage(product.getPhoto());
            product.setPhoto(decompressedImage);
        }
        return new ResponseEntity< List<Product>>( products,HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") Long productId) {
    	Product product = productService.getProductById(productId);
    	byte[] decompressImage = ImageUtils.decompressImage(product.getPhoto());
    	product.setPhoto(decompressImage);
        return new ResponseEntity<Product>( product,HttpStatus.OK);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Product> createProduct( @RequestParam("productName") String productName,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("productPrice") double productPrice,  @RequestParam("categoryID") Long categoryID,
            @RequestPart("photo") MultipartFile photo) throws IOException {
    	
    	Category c;
            Optional<Category> op = categoryRepository.findById(categoryID); // Implement this service method
            
            if (op.isPresent()) {
                c=op.get();
            }
            else {
            	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
    	Product p = new Product();
    	p.setProductName(productName);
    	p.setProductDescription(productDescription);
    	p.setProductPrice(productPrice);
    	
    	p.setCategory(c);
    	 if (photo != null && !photo.isEmpty()) {
             p.setPhoto(photo.getBytes());
    	 }
    	return new ResponseEntity<Product>( productService.createProduct(p),HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Long productId,
            @RequestParam("productName") String productName,
            @RequestParam("productDescription") String productDescription,
            @RequestParam("productPrice") double productPrice,
            @RequestParam(value = "photo", required = false) MultipartFile photo) throws IOException {
    	Product p = new Product();
    	p.setProductName(productName);
    	p.setProductDescription(productDescription);
    	p.setProductPrice(productPrice);
    	 if (photo != null && !photo.isEmpty()) {
             p.setPhoto(photo.getBytes());
    	 }
        return new ResponseEntity<Product>( productService.updateProduct(productId, p),HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
      
        return new ResponseEntity<String>(productService.deleteProduct(productId),HttpStatus.OK);
    }
}

