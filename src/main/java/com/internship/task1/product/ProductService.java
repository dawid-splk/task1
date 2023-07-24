package com.internship.task1.product;

import org.openapitools.model.ProductDTO;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private ProductRepository repository;
    private ProductMapper mapper;

//    public ProductService() {
//    }

    public ProductService(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }


    //TODO //konstruktor? -> jaki?

    ProductDTO save(ProductDTO product) {
        Product productToAdd = mapper.toProduct(product);
        repository.save(productToAdd);
        product.setId(productToAdd.getId());
        return product;
    }

    boolean deleteProduct(Long id) {
        Optional<Product> productOptional = repository.findProductById(id);
        boolean isFound = productOptional.isPresent();
        productOptional.ifPresent(repository::delete);
        return isFound;
    }

    public List<ProductDTO> findProductsByCategory(String category) {
        return repository.findAllByCategory(ProductDTO.CategoryEnum.fromValue(category)).stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    ProductDTO findProductById(Long id) {
        Optional<Product> productOptional = repository.findProductById(id);
        if(productOptional.isPresent()){
            return mapper.toDTO(productOptional.get());
        } else {
            return null;
        }
    }

    boolean updateProduct(ProductDTO productDto) {
        var id = productDto.getId();

        Optional<Product> productOptional = repository.findProductById(id);

        if(productOptional.isPresent()){
            repository.save(mapper.toProduct(productDto));                 //TODO sprawdzic
            return true;
        } else {
            return false;
        }
    }
    boolean updateProduct(Long productId, String name, Float price, String category, @RequestParam("expiryDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime expiryDate) {
        Optional<Product> productOptional = repository.findProductById(productId);

        if(productOptional.isPresent()){
            Product toUpdate = productOptional.get();
            if(name != null) {
                toUpdate.setName(name);
            }
            if(price != null) {
                toUpdate.setPrice(price);
            }
            if(category != null) {
                toUpdate.setCategory(ProductDTO.CategoryEnum.fromValue(category));
            }
            if(expiryDate != null) {
                toUpdate.setExpiryDate(expiryDate.toLocalDateTime());
            }
            repository.save(toUpdate);
            return true;
        } else {
            return false;
        }
    }

    public List<ProductDTO> readAll() {
        return repository.findAll().stream().map(mapper::toDTO).collect(Collectors.toList());
    }

//    private Optional<Product> exists(Long id) {
//        if(!(id instanceof Long) || id < 0) {       //TODO czemu krzyczy jak mozna przekazac stringa w request body? ...czy nie mozna?
//            return Optional.empty();
//        }
//        Optional<Product> productOptional = repository.findProductById(id);
//
//        return productOptional;
//
//    }
}
