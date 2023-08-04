package com.internship.task1.product;

import jakarta.validation.constraints.NotNull;
import org.openapitools.api.ProductsApi;
import org.openapitools.model.ProductDtoRead;
import org.openapitools.model.ProductDtoWrite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController implements ProductsApi {          //TODO ENDPOINT /PRODUCTS ZWERYFIKOWAC BO MIESZA

    ProductService service;

    @Override
    public ResponseEntity<List<ProductDtoRead>> readAll(){
        return ResponseEntity.ok(service.readAll());
    }

    public ProductController(ProductService service) {
        this.service = service;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ProductsApi.super.getRequest();
    }

    @Override
    public ResponseEntity<ProductDtoRead> addProduct(ProductDtoWrite product) {
        ProductDtoRead response = service.save(product);
        return ResponseEntity.created(URI.create("/" + response.getId())).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long productId) {
        if(service.deleteProduct(productId)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<List<ProductDtoRead>> findProductsByCategory(@NotNull String category) {
        return ResponseEntity.ok(service.findProductsByCategory(category));
    }

    @Override
    public ResponseEntity<ProductDtoRead> getProductById(Long productId) {
        ProductDtoRead result = service.findProductById(productId);            //TODO to mi sie nie podoba - czy zrobic to inaczej? (metoda w service i metoda w controller)
        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @Override
    public ResponseEntity<Void> updateProduct(ProductDtoRead product) {
        boolean isSuccesful = service.updateProduct(product);
        if(isSuccesful){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<Void> updateProductWithForm(Long productId, String name, Float price, String category, OffsetDateTime expiryDate) {
        boolean isSuccesful = service.updateProduct(productId, name, price, category, expiryDate);

        if(isSuccesful){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
