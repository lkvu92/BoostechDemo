package com.boostech.demo.service;

import com.boostech.demo.entity.Product;
import com.boostech.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Async
    public CompletableFuture<List<Product>> getAllProducts() {
        return CompletableFuture.completedFuture(productRepository.findAll());
    }

    @Async
    public CompletableFuture<Product> getProductById(UUID id) {
        return CompletableFuture.completedFuture(productRepository.findById(id).orElse(null));
    }

    @Async
    public CompletableFuture<Product> saveProduct(Product product) {
        return CompletableFuture.completedFuture(productRepository.save(product));
    }

    @Async
    public CompletableFuture<List<Product>> findProductsByCategory_Id(UUID id) {
        return CompletableFuture.completedFuture(productRepository.findProductsByCategory_Id(id));
    }
}
