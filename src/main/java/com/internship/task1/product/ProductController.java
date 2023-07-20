package com.internship.task1.product;

import org.openapitools.api.ProductApi;
import org.openapitools.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController implements ProductApi {

    ProductRepository repository;


    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ProductApi.super.getRequest();
    }

    @Override
    public ResponseEntity<Product> addProduct(Product product) {
        var response = repository.save(product);
        return ResponseEntity.created();
    }

    @Override
    public ResponseEntity<Void> deleteProduct(String productName, String apiKey) {
        if(repository)
    }

    @Override
    public ResponseEntity<List<Product>> findProductsByCategory(List<String> category) {
        return ProductApi.super.findProductsByCategory(category);
    }

    @Override
    public ResponseEntity<Product> getProductByName(String productName) {
        return ProductApi.super.getProductByName(productName);
    }

    @Override
    public ResponseEntity<Product> updateProduct(Product product) {
        return ProductApi.super.updateProduct(product);
    }

    @Override
    public ResponseEntity<Void> updateProductWithForm(String productName, String newName, Float price, String category, OffsetDateTime expiryDate) {
        return ProductApi.super.updateProductWithForm(productName, newName, price, category, expiryDate);
    }
}
