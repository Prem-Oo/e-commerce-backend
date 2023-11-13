package com.catalogue.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.catalogue.entity.Category;
import com.catalogue.exception.ProductNotFoundException;
import com.catalogue.repositry.CategoryRepository;

@RestController
@RequestMapping("/category")
public class CategoryController {

	 @Autowired
	    private CategoryRepository categoryRepository;
	 
	 @GetMapping
	    public ResponseEntity< List<Category>> getAllCategories() {
	        return new ResponseEntity< List<Category>>( categoryRepository.findAll(),HttpStatus.OK);
	    }

	    @GetMapping("/{categoryId}")
	    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Long categoryId) {
	        return new ResponseEntity<Category>( categoryRepository.findById(categoryId).orElseThrow(() -> new ProductNotFoundException("Category not found with ID: " + categoryId)),HttpStatus.NOT_FOUND);
	    }

	    @PostMapping
	    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
	        return new ResponseEntity<Category>( categoryRepository.save(category),HttpStatus.CREATED);
	    }

//	    @PutMapping("/{categoryId}")
//	    public ResponseEntity<Category> updateProduct(@PathVariable("categoryId") Long categoryId, @RequestBody Category updatedCategory) {
//	        return new ResponseEntity<Category>( categoryRepository.updateProduct(categoryId, updatedCategory),HttpStatus.OK);
//	    }

	    @DeleteMapping("/{categoryId}")
	    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long categoryId) {
	    	Optional<Category> findById = categoryRepository.findById(categoryId);
	    	if(findById.isPresent()) {
	    		categoryRepository.deleteById(categoryId);
	    		  return new ResponseEntity<String>("deleted succesfully",HttpStatus.OK);
	    	}
	    	else
	    		return null;
	    	
	      
	    }
//	    @GetMapping("/products/{categoryId}")
//	    public ResponseEntity<List<Category>> getProductsByCategory(@PathVariable Long categoryId) {
//	        // Implement logic to retrieve products by category
//	        List<Category> products = categoryRepository.getProductsByCategory(categoryId);
//
//	        // Return the list of products as a response
//	        return new ResponseEntity<>(products, HttpStatus.OK);
//	    }
}
