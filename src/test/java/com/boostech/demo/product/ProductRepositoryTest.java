package com.boostech.demo.product;

import com.boostech.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.UUID;

@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = ProductRepository.class))
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    boolean deleteProduct(UUID id) {
         productRepository.findById(id);

        return true;
    }
}
