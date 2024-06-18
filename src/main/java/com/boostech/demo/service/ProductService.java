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

    //@Async
   //public CompletableFuture<List<Product>> getAllProducts() {
    public List<Product> getAllProducts() {
        //return CompletableFuture.completedFuture(productRepository.findAll());
        return productRepository.findAll();
    }

    @Async
    public CompletableFuture<Product> getProductById(UUID id) {
        return CompletableFuture.completedFuture(productRepository.findById(id).orElse(null));
    }

    //@Async
   // public CompletableFuture<Product> saveProduct(Product product) {
    public Product saveProduct(Product product) {
        for (long i = 0; i < 6122470; i++) {
            System.out.println("Printing " + i);
        }
       // return CompletableFuture.completedFuture(productRepository.save(product));

        return productRepository.save(product);
    }

    @Async
    public CompletableFuture<List<Product>> findProductsByCategory_Id(UUID id) {
        return CompletableFuture.completedFuture(productRepository.findProductsByCategory_Id(id));
    }
}
