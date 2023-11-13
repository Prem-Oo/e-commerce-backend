package com.catalogue.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.catalogue.entity.Product;
import com.catalogue.exception.ProductNotFoundException;
import com.catalogue.repositry.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + productId));
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product updatedProduct) {
        Product existingProduct = getProductById(productId);

        // Update the existing product properties with the new values
        existingProduct.setProductName(updatedProduct.getProductName());
        existingProduct.setProductDescription(updatedProduct.getProductDescription());
        existingProduct.setProductPrice(updatedProduct.getProductPrice());

        return productRepository.save(existingProduct);
    }

    public String deleteProduct(Long productId) {
        Product existingProduct = getProductById(productId);
        productRepository.delete(existingProduct);
        return "Product with "+productId+" ID deleted";
    }
}
