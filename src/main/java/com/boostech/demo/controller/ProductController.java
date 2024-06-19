package com.boostech.demo.controller;

import com.boostech.demo.dto.CustomProductResponse;
import com.boostech.demo.dto.ProductCreateDto;
import com.boostech.demo.entity.Product;
import com.boostech.demo.service.ProductService;
import com.boostech.demo.util.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping()
    public ResponseEntity<CustomResponse<List<Product>>> getAllProducts() {
        CustomResponse<List<Product>> response = new CustomResponse<>("Success", 200, productService.getAllProducts());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/advanced")
    public CustomProductResponse<Product> getProductsAdvanced(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortType,
            @RequestParam(defaultValue = "true") boolean status
    ) {
        return productService.getProductsAdvanced(name, page, limit, sortBy, sortType, status);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Product>> getProductById(@PathVariable UUID id)  {
        Product product = productService.getProductById(id);
        if(product != null) {
            CustomResponse<Product> response = new CustomResponse<>("Success", 200, productService.getProductById(id));
            return new ResponseEntity<>(response, HttpStatus.OK);

        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping()
    public ResponseEntity<CustomResponse<Product>> saveProduct(@RequestBody ProductCreateDto productCreateDto)  {
        Product product = productService.saveProduct(productCreateDto);
        if(product == null) {
            CustomResponse<Product> response = new CustomResponse<>("Product fail!", 400, null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        CustomResponse<Product> response = new CustomResponse<>("Product created successfully", 201, product);
        return new ResponseEntity<CustomResponse<Product>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Product>> updateProduct(@PathVariable UUID id, @RequestBody ProductCreateDto productCreateDto)  {
        Product product = productService.updateProduct(id, productCreateDto);
        if(product == null) {
            CustomResponse<Product> response = new CustomResponse<>("Product not found", 404, null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        CustomResponse<Product> response = new CustomResponse<>("Product updated successfully", 200, product);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<String>> deleteProduct(@PathVariable UUID id)  {
        Product product = productService.deleteProduct(id);
        if(product == null) {;
            return new ResponseEntity<>(new CustomResponse<>("Product not found", 404, null), HttpStatus.NOT_FOUND);
        }else if(product.getDeletedAt() == null){
            return new ResponseEntity<>(new CustomResponse<>("Product deleted successfully", 200, null), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new CustomResponse<>("Product restored successfully", 200, null), HttpStatus.OK);
        }
    }
}
