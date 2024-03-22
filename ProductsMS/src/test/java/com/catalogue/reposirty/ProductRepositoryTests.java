package com.catalogue.reposirty;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.catalogue.entity.Category;
import com.catalogue.entity.Product;
import com.catalogue.repositry.ProductRepository;

@DataJpaTest
public class ProductRepositoryTests {
	
	
    @Autowired
    private ProductRepository productRepo;

    @Test
    public void testSaveProduct() {
        Category category = new Category();
        category.setName("Electronics");
      

        Product p = new Product();
        p.setProductName("HP Laptop");
        p.setProductDescription("premium laptops");
        p.setProductPrice(30000);
        p.setCategory(category);

        Product save = productRepo.save(p);
       

        Assertions.assertThat(save).isNotNull();
        Assertions.assertThat(save.getProductId()).isGreaterThan(0);
		
	}

}
