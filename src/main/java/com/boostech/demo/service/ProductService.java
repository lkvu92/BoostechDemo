package com.boostech.demo.service;

import com.boostech.demo.dto.*;
import com.boostech.demo.dto.reqDto.AttributeValueDto;
import com.boostech.demo.dto.reqDto.ProductCreateDto;
import com.boostech.demo.entity.Attribute;
import com.boostech.demo.entity.Category;
import com.boostech.demo.entity.PValue;
import com.boostech.demo.entity.Product;
import com.boostech.demo.repository.CategoryRepository;
import com.boostech.demo.repository.IAttributeRepository;
import com.boostech.demo.repository.PValueRepository;
import com.boostech.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PValueRepository pValueRepository;
    private final IAttributeRepository attributeRepository;
    private final IPValueService pValueService;

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
        return " /api/v1/products/advanced?page=" + pageable.getPageNumber() + "&limit=" + pageable.getPageSize();
    }

    /**
     * Get one product by id with custom response
     * @param productId
     * @return
     */
    public GetOneProductDto getOneProductDtos(UUID productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return null;
        }
        GetOneProductDto dto = customResponse(product);
        return dto;
    }

    private GetOneProductDto customResponse(Product product) {
        GetOneProductDto dto = new GetOneProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        GetOneProductDto.CategoryDto categoryDto = new GetOneProductDto.CategoryDto();
        categoryDto.setId(product.getCategory().getId());
        categoryDto.setName(product.getCategory().getName());
        dto.setCategory(categoryDto);

        List<PValue> listPValue = pValueRepository.findAllByProductIdIn(List.of(product.getId()), false);
        for (PValue pValue : listPValue) {
            GetOneProductDto.AttributeDto attributeDto = new GetOneProductDto.AttributeDto();
            attributeDto.setId(pValue.getAttribute().getId());
            attributeDto.setName(pValue.getAttribute().getAttributeName());
            attributeDto.setValue(pValue.getValue());

            dto.getAttributes().add(attributeDto);
        }
        return dto;
    }
    /**
     * Create product full version
     */
    @Transactional
    public GetOneProductDto createProductWithAttributes(ProductCreateDto productCreateDto) {
        //validate product name
        if (!productCreateDto.getName().isEmpty() && productRepository.existsByName(productCreateDto.getName())) {
            throw new IllegalArgumentException("Product name already exists");
        }
       //input Product, List<Attribute>
        Optional<Category> categoryOptional = categoryRepository.findById(productCreateDto.getCate_id());
        if (categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        Category category = categoryOptional.get();
        List<Attribute> attributes = category.getAttributes();
        //Check list attr isqual list attr in cate , if not return null

        Map<UUID, Attribute> listAttributeOfCateMap = CheckListAttribute(attributes, productCreateDto.getAttributeValues());
        if(listAttributeOfCateMap == null){
            throw new IllegalArgumentException("Product's attributes are not valid");
        }
        //Create product
        Optional<Category> cate = categoryRepository.findById(productCreateDto.getCate_id());
        Product product = new Product();
        product.setName(productCreateDto.getName());
        product.setCategory(cate.get());
        productRepository.save(product);
        //Create list value
        List<AttributeValueUnitTuple>  attributeIdValueUnitTuples = new ArrayList<>();
        for (AttributeValueDto attributeValue : productCreateDto.getAttributeValues()) {
            UUID attributeId = attributeValue.getAttributeId();
            Attribute attribute = listAttributeOfCateMap.get(attributeId);
            String value = attributeValue.getValue();
            UUID unitId = attributeValue.getUnitId();

            AttributeValueUnitTuple attributeValueUnitTuple = new AttributeValueUnitTuple(attribute, value);
            attributeIdValueUnitTuples.add(attributeValueUnitTuple);
        }

        pValueService.createValueByProductIdAndAttributeIdValueUnitTuples(product, attributeIdValueUnitTuples);

        GetOneProductDto productCustom = customResponse(product);
        return productCustom;
    }

    @Transactional
    public GetOneProductDto updateProductWithAttributes(ProductCreateDto productCreateDto, UUID productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        if (!productCreateDto.getName().isEmpty()
                && productRepository.existsByName(productCreateDto.getName())
                && productCreateDto.getName().equals(product.getName())
        ) {
            throw new IllegalArgumentException("Product name already exists");
        }
        //input Product, List<Attribute>
        Optional<Category> categoryOptional = categoryRepository.findById(productCreateDto.getCate_id());
        if (categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        Category category = categoryOptional.get();
        List<Attribute> attributes = category.getAttributes();
        //Check list attr isqual list attr in cate , if not return null

        Map<UUID, Attribute> listAttributeOfCateMap = CheckListAttribute(attributes, productCreateDto.getAttributeValues());
        if(listAttributeOfCateMap == null){
            throw new IllegalArgumentException("Product's attributes are not valid");
        }
        //Update product
        Optional<Category> cate = categoryRepository.findById(productCreateDto.getCate_id());
        product.setName(productCreateDto.getName());
        product.setCategory(cate.get());
        productRepository.save(product);
        //Create list value
        List<AttributeValueUnitTuple>  attributeIdValueUnitTuples = new ArrayList<>();
        for (AttributeValueDto attributeValue : productCreateDto.getAttributeValues()) {
            UUID attributeId = attributeValue.getAttributeId();
            Attribute attribute = listAttributeOfCateMap.get(attributeId);
            String value = attributeValue.getValue();
            UUID unitId = attributeValue.getUnitId();

            AttributeValueUnitTuple attributeValueUnitTuple = new AttributeValueUnitTuple(attribute, value, unitId);
            attributeIdValueUnitTuples.add(attributeValueUnitTuple);
        }

        pValueService.createValueByProductIdAndAttributeIdValueUnitTuples(product, attributeIdValueUnitTuples);

        GetOneProductDto productCustom = customResponse(product);
        return productCustom;
    }

    private Map<UUID, Attribute> CheckListAttribute(List<Attribute> listAttributeOfCate, List<AttributeValueDto> listAttributeOfProduct){
        if(listAttributeOfCate.size() > listAttributeOfProduct.size()){
            return null;
        }

        Map<UUID, Attribute> listAttributeOfCateMap = new HashMap<>();
        for (Attribute attribute : listAttributeOfCate) {
            listAttributeOfCateMap.put(attribute.getId(), attribute);
        }

        for (AttributeValueDto attributedto : listAttributeOfProduct) {
            if (!listAttributeOfCateMap.containsKey(attributedto.getAttributeId())) {
                return null;
            }
        }

        return listAttributeOfCateMap;
    }
}
