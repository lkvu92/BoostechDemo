package com.boostech.demo.controller;

import com.boostech.demo.dto.CustomProductResponse;
import com.boostech.demo.dto.GetOneProductDto;
import com.boostech.demo.dto.ProductUpdateDto;
import com.boostech.demo.dto.reqDto.ProductCreateDto;
import com.boostech.demo.dto.resDto.FindAllProductCustom;
import com.boostech.demo.entity.Product;
import com.boostech.demo.service.ProductService;
import com.boostech.demo.util.CustomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/products")
public class ProductController extends BaseController{
    private final ProductService productService;

    @GetMapping()
    public ResponseEntity<CustomResponse<List<FindAllProductCustom>>> getAllProducts() {
        return new ResponseEntity<>(new CustomResponse<>("Success", 200, productService.getAllProducts()), HttpStatus.OK);
    }

    @GetMapping("/advanced")
    public CustomResponse<CustomProductResponse<FindAllProductCustom>> getProductsAdvanced(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortType,
            @RequestParam(defaultValue = "true") boolean status // true: active, false: all
    ) {
        CustomResponse customResponse = new CustomResponse("Success", 200, productService.getProductsAdvanced(name, page, limit, sortBy, sortType, status));
        return customResponse;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Product>> getProductById(@PathVariable UUID id, @RequestParam(defaultValue = "true") boolean status )  {
        Product product = productService.getProductById(id,status);
        if(product != null) {
            return new ResponseEntity<>(new CustomResponse<>("Success", 200, product), HttpStatus.OK);

        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/createv1")
    public ResponseEntity<CustomResponse<Product>> saveProduct1(@RequestBody ProductCreateDto productCreateDto)  {
        Product product = productService.saveProduct(productCreateDto);
        if(product == null) {
            return new ResponseEntity<>(new CustomResponse<>("Product fail!", 400, null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<CustomResponse<Product>>(new CustomResponse<>("Product created successfully", 201, product), HttpStatus.CREATED);
    }

    @PutMapping("/post_v1/{id}")
    public ResponseEntity<CustomResponse<Product>> updateProductV1(@PathVariable UUID id, @RequestBody ProductCreateDto productCreateDto)  {
        Product product = productService.updateProduct(id, productCreateDto);
        if(product == null) {
            return new ResponseEntity<>(new CustomResponse<>("Product not found", 404, null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new CustomResponse<>("Product updated successfully", 200, product), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<String>> deleteProduct(@PathVariable UUID id)  {
        boolean delete = productService.deleteProduct(id);
      if(delete){
            return new ResponseEntity<>(new CustomResponse<>("Product deleted successfully", 200, null), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new CustomResponse<>("Product restored successfully", 200, null), HttpStatus.OK);
        }
    }

    @GetMapping("/getoneproduct/{id}")
    public CustomResponse<GetOneProductDto> getoneproduct(@PathVariable UUID id) {
        CustomResponse customResponse = new CustomResponse("Success", 200, productService.getOneProductDtos(id));
        return customResponse;
    }

    @PostMapping()
    public ResponseEntity<CustomResponse<Void>> saveProduct(@RequestBody ProductCreateDto productCreateDto)  {
        productService.createProductWithAttributes(productCreateDto);
//        if(product == null) {
//            return new ResponseEntity<>(new CustomResponse<>("Product fail!", 400, null), HttpStatus.BAD_REQUEST);
//        }
        return new ResponseEntity<>(new CustomResponse<>("Product created successfully", 201, null), HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Void>> updateProduct(@PathVariable UUID id, @RequestBody ProductUpdateDto productCreateDto)  {
        productService.updateProductWithAttributes(productCreateDto, id);

        return new ResponseEntity<>(new CustomResponse<>("Product updated successfully", 200, null), HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CustomResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(new CustomResponse<>(e.getMessage(), 400, null), HttpStatus.BAD_REQUEST);
    }
}
