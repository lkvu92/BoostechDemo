package com.boostech.demo.service;

import com.boostech.demo.dto.CustomProductResponse;
import com.boostech.demo.dto.FindAllProductByCategoryIdAndAttributeIdValuePairsDto;
import com.boostech.demo.dto.GetOneProductDto;
import com.boostech.demo.dto.ProductCreateDto;
import com.boostech.demo.entity.Attribute;
import com.boostech.demo.entity.Category;
import com.boostech.demo.entity.PValue;
import com.boostech.demo.entity.Product;
import com.boostech.demo.repository.CategoryRepository;
import com.boostech.demo.repository.PValueRepository;
import com.boostech.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PValueRepository pValueRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(UUID id,boolean status) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null && (status && product.getDeletedAt() == null || !status && product.getDeletedAt() != null)) {
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

    /**
     * Get products with advanced search.
     *
     * @param name    Product name
     * @param page    Page number
     * @param limit   Number of items per page
     * @param sortBy  Sort by field
     * @param sortType Sort type (asc or desc)
     * @param status  Product status
     * @return CustomProductResponse object
     */
    public CustomProductResponse<Product> getProductsAdvanced(String name, int page, int limit, String sortBy, String sortType, boolean status) {
        Pageable pageable = sortType.equals("desc") ?
                PageRequest.of(page, limit, Sort.by(Sort.Order.desc(sortBy))) :
                PageRequest.of(page, limit, Sort.by(Sort.Order.asc(sortBy)));
        Page<Product> productPage;
        if (name != null && !name.isEmpty()) {
            productPage = !status ?
                    productRepository.findBySearchTerm(name, pageable) :
                    productRepository.findBySearchTermActive(name, pageable);
        } else {
            productPage = !status ?
                    productRepository.findAll(pageable) :
                    productRepository.findAllByDeletedAtIsNull(pageable);
        }

        return createCustomResponse(productPage);
    }

    /**
     * Create a custom response for the product.
     *
     * @param page Page object
     * @return CustomProductResponse object
     */
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

    /**
     * Construct the URL for the next and previous pages.
     *
     * @param pageable Pageable object
     * @return URL for the next or previous page
     */
    private String constructPageUrl(Pageable pageable) {
        return "/api/products?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
    }

    public GetOneProductDto getOneProductDtos(UUID productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return null;
        }
        GetOneProductDto dto = new GetOneProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        GetOneProductDto.CategoryDto categoryDto = new GetOneProductDto.CategoryDto();
        categoryDto.setId(product.getCategory().getId());
        categoryDto.setName(product.getCategory().getName());
        dto.setCategory(categoryDto);

        List<PValue> listPValue = pValueRepository.findAllByProductIdIn(List.of(productId));
        for (PValue pValue : listPValue) {
            GetOneProductDto.AttributeDto attributeDto = new GetOneProductDto.AttributeDto();
            attributeDto.setId(pValue.getAttribute().getId());
            attributeDto.setName(pValue.getAttribute().getAttributeName());
            attributeDto.setValue(pValue.getValue());
            attributeDto.setUnit(pValue.getAttribute().getUnit().getUnitName());

            dto.getAttributes().add(attributeDto);
        }

        return dto;
    }
}
