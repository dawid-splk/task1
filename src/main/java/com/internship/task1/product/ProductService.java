package com.internship.task1.product;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.openapitools.model.CategoryEnum;
import org.openapitools.model.ProductDtoRead;
import org.openapitools.model.ProductDtoWrite;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository repository;
    private ProductMapper mapper;
    private KafkaTemplate<String, Object> kafkaTemplate;


    @KafkaListener(topics = "store_status")
    void listener(ConsumerRecord<String, Float> record) {
        long id = Long.parseLong(record.key());
        Product productToUpdate = repository.findProductById(id).orElseThrow(() -> new IllegalStateException());
        productToUpdate.setQuantity(record.value());
        repository.save(productToUpdate);
    }


    ResponseEntity<ProductDtoRead> save(@Valid ProductDtoWrite product) {
        Product productToAdd = mapper.fromDtoWriteToProduct(product, -1L, 0.0F);
        repository.save(productToAdd);

        kafkaTemplate.send("store_control", String.valueOf(productToAdd.getId()), null);

        ProductDtoRead response = mapper.toDtoRead(productToAdd);

        return ResponseEntity.created(URI.create("/" + response.getId())).body(response);
    }

    ResponseEntity<Void> deleteProduct(Long id) {       //TODO integration test
        Optional<Product> productOptional = repository.findProductById(id);
        if(productOptional.isPresent()){
            productOptional.ifPresent(repository::delete);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<ProductDtoRead>> findProductsByCategory(String category) {       //TODO integration test
        return ResponseEntity.ok
                (repository.findAllByCategory(CategoryEnum.fromValue(category))
                .stream()
                .map(mapper::toDtoRead)
                .toList());
    }

    ResponseEntity<ProductDtoRead> findProductById(Long id) {
        Optional<Product> productOptional = repository.findProductById(id);
        if(productOptional.isPresent()){
            return ResponseEntity.ok(mapper.toDtoRead(productOptional.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    ResponseEntity<Void> updateProduct(Long productId, ProductDtoWrite productDtoWrite) {
        Optional<Product> productOptional = repository.findProductById(productId);

        if(productOptional.isPresent()){
            repository.save(mapper.fromDtoWriteToProduct(productDtoWrite, productOptional.get().getId(), productOptional.get().getQuantity()));
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    ResponseEntity<Void> updateProduct(Long productId, String name, Float price, String category, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryDate) {
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
                toUpdate.setCategory(CategoryEnum.fromValue(category));
            }
            if(expiryDate != null) {
                toUpdate.setExpiryDate(expiryDate);
            }
            repository.save(toUpdate);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<List<ProductDtoRead>> readAll() {
        return ResponseEntity.ok
                (repository.findAll()
                .stream()
                .map(mapper::toDtoRead)
                .collect(Collectors.toList()));
    }
}
