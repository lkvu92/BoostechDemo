package com.boostech.demo.service;

import com.boostech.demo.dto.*;
import com.boostech.demo.dto.reqDto.AttributeValueDto;
import com.boostech.demo.dto.reqDto.ProductCreateDto;
import com.boostech.demo.dto.resDto.FindAllProductByCategoryIdAndAttributeIdAndValueResponse;
import com.boostech.demo.dto.resDto.FindAllProductCustom;
import com.boostech.demo.entity.*;
import com.boostech.demo.exception.ProductNotFoundException;
import com.boostech.demo.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PValueRepository pValueRepository;
    private final AttributeRepository attributeRepository;
    private final IPValueService pValueService;
    private final EntityManager _entityManager;

    public List<FindAllProductCustom> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<FindAllProductCustom> productCustoms = new ArrayList<>();
        for (Product product : products) {
            FindAllProductCustom productCustom = new FindAllProductCustom();
            productCustom.setId(product.getId());
            productCustom.setName(product.getName());
            FindAllProductCustom.CategoryCustom categoryCustom = new FindAllProductCustom.CategoryCustom();
            categoryCustom.setId(product.getCategory().getId());
            categoryCustom.setName(product.getCategory().getName());

            productCustom.setCategory(categoryCustom);
            productCustoms.add(productCustom);
        }
        return productCustoms;
    }

    public Product getProductById(UUID id,boolean status) {
        Product product = productRepository.findByIdWithCategoryAndAttributes(id).orElse(null);
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

    public boolean deleteProduct(UUID id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException(id);
        }

        Product product = productOptional.get();

        boolean delete = true;
        if(product.getDeletedAt() != null){
            product.setDeletedAt(null);
            delete = false;
        }else {
            product.setDeletedAt(LocalDateTime.now());
        }

        productRepository.save(product);
        return delete;
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
    public CustomProductResponse<FindAllProductCustom> getProductsAdvanced(String name, int page, int limit, String sortBy, String sortType, boolean status) {
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
        Page<FindAllProductCustom> productPageCustom = productPage.map(product -> {
            FindAllProductCustom productCustom = new FindAllProductCustom();
            productCustom.setId(product.getId());
            productCustom.setName(product.getName());
            FindAllProductCustom.CategoryCustom categoryCustom = new FindAllProductCustom.CategoryCustom();
            categoryCustom.setId(product.getCategory().getId());
            categoryCustom.setName(product.getCategory().getName());
            productCustom.setCategory(categoryCustom);
            return productCustom;
        });

        return createCustomResponseFindAllProductCustom(productPageCustom);
    }


    /**
     * Get one product by id with custom response
     * @param productId
     * @return
     */
    public GetOneProductDto getOneProductDtos(UUID productId) {
        Product product = productRepository.findByIdWithCategoryAndAttributes(productId).orElse(null);
        if (product == null) {
            throw new ProductNotFoundException(productId);
        }
        GetOneProductDto dto = customResponse(product);
        return dto;
    }


    @Transactional
    public void createProductWithAttributes(ProductCreateDto productCreateDto) {
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
        List<CategoryAttribute> attributes = category.getCategoryAttributes();
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

            AttributeValueUnitTuple attributeValueUnitTuple = new AttributeValueUnitTuple(attribute, value, "create");
            attributeIdValueUnitTuples.add(attributeValueUnitTuple);
        }

        pValueService.createValueByProductIdAndAttributeIdValueUnitTuples(product, category, attributeIdValueUnitTuples);
    }

    @Transactional
    public void updateProductWithAttributes(ProductUpdateDto productCreateDto, UUID productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("Product not found");
        }
        if (!productCreateDto.getName().isEmpty()) {
           if(!product.getName().equals(productCreateDto.getName()) && productRepository.existsByName(productCreateDto.getName())){
                throw new IllegalArgumentException("Product name already exists");
            }
        }
        //input Product, List<Attribute>
        Category category = product.getCategory();
        List<CategoryAttribute> attributes = category.getCategoryAttributes();
        List<Attribute> attributes1 = attributeRepository.findAllByProductId(productId);
        //Check list attr isqual list attr in cate , if not return null

        Map<UUID, AttributeWithStatusDto> listAttributeOfCateMap = CheckListAttributeUpdate(attributes, productCreateDto.getAttributeValues(), attributes1);
        if(listAttributeOfCateMap == null){
            throw new IllegalArgumentException("Product's attributes are not valid");
        }
        //Update product
        product.setName(productCreateDto.getName());
        productRepository.save(product);

        List<AttributeValueUnitTuple>  attributeIdValueUnitTuples = new ArrayList<>();
        for (AttributeValueDto attributeValue : productCreateDto.getAttributeValues()) {
            UUID attributeId = attributeValue.getAttributeId();
            Attribute attribute = listAttributeOfCateMap.get(attributeId).getAttribute();
            String value = attributeValue.getValue();
            String status = listAttributeOfCateMap.get(attributeId).getStatus();

            AttributeValueUnitTuple attributeValueUnitTuple = new AttributeValueUnitTuple(attribute, value, status);
            attributeIdValueUnitTuples.add(attributeValueUnitTuple);

            listAttributeOfCateMap.remove(attributeId);
        }

        for (Map.Entry<UUID, AttributeWithStatusDto> entry : listAttributeOfCateMap.entrySet()) {
            Attribute attribute = entry.getValue().getAttribute();
            String status = entry.getValue().getStatus();

            AttributeValueUnitTuple attributeValueUnitTuple = new AttributeValueUnitTuple(attribute, null, status);
            attributeIdValueUnitTuples.add(attributeValueUnitTuple);
        }

        pValueService.update(product, category, attributeIdValueUnitTuples);
    }

    /**
     * Create a custom response for the product.
     *
     * @param page Page object
     * @return CustomProductResponse object
     */
    private CustomProductResponse<FindAllProductCustom> createCustomResponseFindAllProductCustom(Page<FindAllProductCustom> page) {
        CustomProductResponse<FindAllProductCustom> response = new CustomProductResponse<>();
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
            attributeDto.setUnit(pValue.getAttribute().getUnit().getUnitName());
            dto.getAttributes().add(attributeDto);
        }
        return dto;
    }
    /**
     * Create product full version
     */

    private Map<UUID, Attribute> CheckListAttribute(List<CategoryAttribute> listAttributeOfCate, List<AttributeValueDto> listAttributeOfProduct){
        if (listAttributeOfCate.size() < listAttributeOfProduct.size()){
            return null;
        }

        Map<UUID, Attribute> requiredMap = new HashMap<>();
        Map<UUID, Attribute> optionalMap = new HashMap<>();

        for (CategoryAttribute attribute : listAttributeOfCate) {
            if(attribute.getIsRequired()){
                requiredMap.put(attribute.getAttributeId(), attribute.getAttribute());
            }else{
                optionalMap.put(attribute.getAttributeId(), attribute.getAttribute());
            }
        }

        if (requiredMap.size() > listAttributeOfProduct.size()) {
            return null;
        }

        int requiredCount = 0;
        for (AttributeValueDto attributedto : listAttributeOfProduct) {
            if (requiredMap.containsKey(attributedto.getAttributeId())) {
                requiredCount++;
            }
            else if (!optionalMap.containsKey(attributedto.getAttributeId())) {
                return null;
            }
        }

        if (requiredCount < requiredMap.size()) {
            return null;
        }

        for (Map.Entry<UUID, Attribute> entry : optionalMap.entrySet()) {
            if (!requiredMap.containsKey(entry.getKey())) {
                requiredMap.put(entry.getKey(), entry.getValue());
            }
        }

        return requiredMap;
    }

    private Map<UUID, AttributeWithStatusDto> CheckListAttributeUpdate(
            List<CategoryAttribute> listAttributeOfCate,
            List<AttributeValueDto> listAttributeOfProductUpdate,
            List<Attribute> listAttributeOfProduct){
        if (listAttributeOfCate.size() < listAttributeOfProductUpdate.size()){
            return null;
        }

        Map<UUID, Attribute> requiredMap = new HashMap<>();
        Map<UUID, Attribute> optionalMap = new HashMap<>();

        for (CategoryAttribute attribute : listAttributeOfCate) {
            if(attribute.getIsRequired()){
                requiredMap.put(attribute.getAttributeId(), attribute.getAttribute());
            }else{
                optionalMap.put(attribute.getAttributeId(), attribute.getAttribute());
            }
        }

        if (requiredMap.size() > listAttributeOfProductUpdate.size()) {
            return null;
        }

        int requiredCount = 0;
        for (AttributeValueDto attributedto : listAttributeOfProductUpdate) {
            if (requiredMap.containsKey(attributedto.getAttributeId())) {
                requiredCount++;
            }
            else if (!optionalMap.containsKey(attributedto.getAttributeId())) {
                return null;
            }
        }

        if (requiredCount < requiredMap.size()) {
            return null;
        }

        Map<UUID, AttributeWithStatusDto> result = new HashMap<>();

        for (Map.Entry<UUID, Attribute> entry : requiredMap.entrySet()) {
            AttributeWithStatusDto attributeWithStatusDto = new AttributeWithStatusDto();
            attributeWithStatusDto.setAttribute(entry.getValue());
            attributeWithStatusDto.setStatus("update");
            result.put(entry.getKey(), attributeWithStatusDto);
        }

        Map<UUID, Attribute> attributeOfProductMap = new HashMap<>();

        for (Attribute attribute : listAttributeOfProduct) {
            attributeOfProductMap.put(attribute.getId(), attribute);
        }

        Map<UUID, Attribute> updateMap = new HashMap<>();

        for (AttributeValueDto attributedto : listAttributeOfProductUpdate) {
            updateMap.put(attributedto.getAttributeId(), attributeOfProductMap.get(attributedto.getAttributeId()));
        }

        for (Map.Entry<UUID, Attribute> entry : optionalMap.entrySet()) {
            if (!requiredMap.containsKey(entry.getKey())) {
                AttributeWithStatusDto attributeWithStatusDto = new AttributeWithStatusDto();
                attributeWithStatusDto.setAttribute(entry.getValue());
                if (updateMap.containsKey(entry.getKey())) {
                    attributeWithStatusDto.setStatus("update");
                } else if (!attributeOfProductMap.containsKey(entry.getKey())) {
                    attributeWithStatusDto.setStatus("create");
                }
                else {
                    attributeWithStatusDto.setStatus("delete");
                }
                result.put(entry.getKey(), attributeWithStatusDto);
            }
        }

        return result;
    }
}
