package com.internship.task1.product;

import org.openapitools.model.Product;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repository;

    ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    //TODO //konstruktor? -> jaki?

    Product save(Product product) {
        return repository.save(product);
    }

    void deleteProduct(Long id) {
        Optional<Product> productOptional = repository.findProductById(id);     //TODO nie wiem czy findProductById bedzie dzialalo Long vs int, moze bredze
        productOptional.ifPresent(repository::delete);
    }

//    public List<Product> findProductsByCategory(List<String> category) {
//        return repository.findAllByCategory((Product.CategoryEnum) category);
//    }

    Product findProductById(Long id) {
        Optional<Product> productOptional = repository.findProductById(id);
        return productOptional.orElse(null);
    }


    void updateProduct(Product product, String newName, Float price, String category, OffsetDateTime expiryDate) {
        if(newName != null) {
            product.setName(newName);
        }
        if(price != null) {
            product.setPrice(price);
        }
        if(category != null) {
            product.setCategory(org.openapitools.model.Product.CategoryEnum.valueOf(category));
        }
        if(expiryDate != null) {
            product.setExpiryDate(expiryDate);
        }

    }

    boolean updateProduct(Product product) {
        var id = product.getId();
        if(!(id instanceof Long) || id < 0) {     //TODO czemu krzyczy jak mozna przekazac stringa?
            return false;
        }
        Optional<Product> currentOptional = repository.findProductById(id);



        if(currentOptional.isPresent()){
            repository.save(product);
            return true;
        } else {
            return false;
        }

//        if(currentOptional.isPresent()){
//            updateFrom(currentOptional.get(), product);
//            repository.save(currentOptional.get())
//            return true;
//        } else {
//            return false;
//        }
    }

//    private void updateFrom(Product current, Product toUpdate) {
//        current.setName(toUpdate.getName());
//        current.setCategory(toUpdate.getCategory());
//        current.setPrice(toUpdate.getPrice());
//        current.setExpiryDate(toUpdate.getExpiryDate());
//    }
}
