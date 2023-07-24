package com.internship.task1.product;

import jakarta.validation.constraints.NotNull;
import org.openapitools.api.ProductApi;
import org.openapitools.model.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController implements ProductApi {          //TODO ENDPOINT /PRODUCTS ZWERYFIKOWAC BO MIESZA

    ProductService service;

    @Override
    public ResponseEntity<List<ProductDTO>> readAll(){
        return ResponseEntity.ok(service.readAll());
    }

    public ProductController(ProductService service) {
        this.service = service;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ProductApi.super.getRequest();
    }

    @Override
    public ResponseEntity<ProductDTO> addProduct(ProductDTO product) {
        ProductDTO response = service.save(product);
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
    public ResponseEntity<List<ProductDTO>> findProductsByCategory(@NotNull String category) {
        return ResponseEntity.ok(service.findProductsByCategory(category));
    }

    @Override
    public ResponseEntity<ProductDTO> getProductById(Long productId) {
        ProductDTO result = service.findProductById(productId);            //TODO to mi sie nie podoba - czy zrobic to inaczej? (metoda w service i metoda w controller)
        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @Override
    public ResponseEntity<Void> updateProduct(ProductDTO product) {
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
