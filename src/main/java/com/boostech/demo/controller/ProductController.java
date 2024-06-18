package com.boostech.demo.controller;

import com.boostech.demo.entity.Product;
import com.boostech.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<List<Product>> getAllProducts() throws ExecutionException, InterruptedException {
        //return new ResponseEntity<>(productService.getAllProducts().get(), HttpStatus.OK);
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(productService.getProductById(id).get(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Product> saveProduct(@RequestBody Product product) throws ExecutionException, InterruptedException {
        //return new ResponseEntity<>(productService.saveProduct(product).get(), HttpStatus.CREATED);
        return new ResponseEntity<>(productService.saveProduct(product), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(UUID id, Product product) throws ExecutionException, InterruptedException {
        Product existingProduct = productService.getProductById(id).get();
        if (existingProduct == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        existingProduct.setName(product.getName());
        existingProduct.setCategory(product.getCategory());
       // return new ResponseEntity<>(productService.saveProduct(existingProduct).get(), HttpStatus.OK);
        return new ResponseEntity<>(productService.saveProduct(existingProduct), HttpStatus.OK);
    }
}
