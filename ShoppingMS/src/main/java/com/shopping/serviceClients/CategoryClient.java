package com.shopping.serviceClients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shopping.serviceModels.Category;



@FeignClient(name = "Products-MS/category")
public interface CategoryClient {

	
	 @GetMapping
	    public ResponseEntity< List<Category>> getAllCategories();

	    @GetMapping("/{categoryId}")
	    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") Long categoryId);
	    @PostMapping
	    public ResponseEntity<Category> createCategory(@RequestBody Category category) ;



	    @DeleteMapping("/{categoryId}")
	    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long categoryId);
}
