package com.internship.task1.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.openapitools.api.ProductsApi;
import org.openapitools.model.ProductDtoRead;
import org.openapitools.model.ProductDtoWrite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class ProductController implements ProductsApi {

    ProductService service;


    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ProductsApi.super.getRequest();
    }

    @Override
    public ResponseEntity<List<ProductDtoRead>> readAll(){
        return service.readAll();
    }

    @Override
    public ResponseEntity<ProductDtoRead> addProduct(@RequestBody @Valid ProductDtoWrite product) {
        return service.save(product);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long productId) {
        return service.deleteProduct(productId);
    }

    @Override
    public ResponseEntity<List<ProductDtoRead>> findProductsByCategory(@NotNull String category) {
        return service.findProductsByCategory(category);
    }

    @Override
    public ResponseEntity<ProductDtoRead> getProductById(Long productId) {
        return service.findProductById(productId);
    }


    @Override
    public ResponseEntity<Void> updateProduct(Long productId, ProductDtoWrite productDtoWrite) {
        return service.updateProduct(productId, productDtoWrite);
    }

    @Override
    public ResponseEntity<Void> updateProductWithForm(Long productId, String name, Float price, String category, LocalDateTime expiryDate) {
        return service.updateProduct(productId, name, price, category, expiryDate);
    }

}
