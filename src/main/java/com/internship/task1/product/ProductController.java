package com.internship.task1.product;

import org.openapitools.api.ProductApi;
import org.openapitools.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ProductController implements ProductApi {

    ProductService service;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return ProductApi.super.getRequest();
    }

    @Override
    public ResponseEntity<Product> addProduct(Product product) {
        Product response = service.save(product);
        return ResponseEntity.created(URI.create("/" + response.getId())).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long productId) {
        service.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<Product>> findProductsByCategory(List<String> category) {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Product> getProductById(Long productId) {
        Product result = service.findProductById(productId);            //TODO to mi sie nie podoba - czy zrobic to inaczej? (metoda w service i metoda w controller)
        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(result);
        }
    }

    @Override
    public ResponseEntity<Void> updateProduct(Product product) {
        boolean isSuccesful = service.updateProduct(product);
        if(isSuccesful){
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override   //TODO ile kodu ma byc w kontrolerze a ile w serwisie - updateProductWithForm vs updateProduct
    public ResponseEntity<Void> updateProductWithForm(Long productId, String newName, Float price, String category, OffsetDateTime expiryDate) {
        Product result = service.findProductById(productId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        } else {
            service.updateProduct(result, newName, price, category, expiryDate);
            return ResponseEntity.noContent().build();
        }
    }


}
