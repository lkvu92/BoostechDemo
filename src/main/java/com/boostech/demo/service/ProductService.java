package com.boostech.demo.service;

import com.boostech.demo.dto.CustomProductResponse;
import com.boostech.demo.dto.ProductCreateDto;
import com.boostech.demo.entity.Category;
import com.boostech.demo.entity.Product;
import com.boostech.demo.repository.CategoryRepository;
import com.boostech.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(UUID id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null && product.getDeletedAt() == null){
            return product;
        }
        return null;
    }

    public Product saveProduct(ProductCreateDto productCreateDto) {
        Category cate = categoryRepository.findById(productCreateDto.getCate_id()).orElse(null);
        if (cate == null) {
            return null;
        }
        Product product = new Product();
        product.setName(productCreateDto.getName());
        product.setCategory(cate);
        return productRepository.save(product);
    }

    public Product updateProduct(UUID id, ProductCreateDto productCreateDto) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        Category cate = categoryRepository.findById(productCreateDto.getCate_id()).orElse(null);
        if (cate == null) {
            return null;
        }
        product.setName(productCreateDto.getName());
        product.setCategory(cate);
        return productRepository.save(product);
    }

    public List<Product> findProductsByCategory_Id(UUID id) {
        return productRepository.findProductsByCategory_Id(id);
    }

    public Product deleteProduct(UUID id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        if(product.getDeletedAt() != null){
            product.setDeletedAt(null);
        }else {
            product.setDeletedAt(LocalDateTime.now());
        }
        productRepository.save(product);
        return product;
    }

    public CustomProductResponse<Product> getProductsAdvanced(String name, int page, int limit, String sortBy, String sortType, boolean status) {
        Pageable pageable = sortType.equals("desc") ?
                PageRequest.of(page, limit, Sort.by(Sort.Order.desc(sortBy))) :
                PageRequest.of(page, limit, Sort.by(Sort.Order.asc(sortBy)));
        Page<Product> productPage;
        if (name != null && !name.isEmpty()) {
            if(!status){
                productPage = productRepository.findBySearchTerm(name, pageable);
            }else{
                productPage = productRepository.findBySearchTermActive(name, pageable);
            }
        } else {
            if(!status){
                productPage = productRepository.findAll(pageable);
            }else {
                productPage = productRepository.findAllByDeletedAtIsNull(pageable);
            }
        }
        return createCustomResponse(productPage);
    }

    private CustomProductResponse<Product> createCustomResponse(Page<Product> page) {
        CustomProductResponse<Product> response = new CustomProductResponse<>();
        response.setData(page.getContent());

        CustomProductResponse.Details details = new CustomProductResponse.Details();
        details.setTotalItems(page.getTotalElements());
        details.setCurrentPage(page.getNumber());
        details.setLimit(page.getSize());
        details.setTotalPages(page.getTotalPages());
        details.setNextPageUrl(page.hasNext() ? constructPageUrl(page.nextPageable()) : null);
        details.setPrevPageUrl(page.hasPrevious() ? constructPageUrl(page.previousPageable()) : null);

        response.setDetails(details);
        return response;
    }

    private String constructPageUrl(Pageable pageable) {
        return "/api/products?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
    }

}
