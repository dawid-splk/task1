package com.internship.task1.product;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.CategoryEnum;
import org.openapitools.model.ProductDtoRead;
import org.openapitools.model.ProductDtoWrite;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockBean({KafkaProducer.class})
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    ProductService service;

    @Mock
    ProductRepository repository;

    @Mock
    ProductMapper mapper;

    @Mock
    KafkaTemplate<String, Object> kafka;

    ProductDtoWrite dtoWrite;
    Map<Long, Product> mockDatabase;

    @BeforeEach
    void setup() {
        dtoWrite = new ProductDtoWrite();
        dtoWrite.setName("ser krolewski Sierpc");
        dtoWrite.setPrice(5.99F);
        dtoWrite.setCategory(CategoryEnum.fromValue("dairy products"));
        dtoWrite.setExpiryDate(LocalDateTime.now());

    }


    @Test
    void testCreateProduct_whenValidProductDtoWritePassed_shouldReturnProductDtoRead(){
        // Given
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.<Product>getArgument(0));
        when(mapper.fromDtoWriteToProduct(any(ProductDtoWrite.class), any(Long.class), any(Float.class))).thenCallRealMethod();
        when(mapper.toDtoRead(any(Product.class))).thenCallRealMethod();

        // When
        ProductDtoRead result = service.save(dtoWrite).getBody();

        //Then      //TODO testy integracyjne (repository.save() by nadac id)
        assertEquals(result.getName(), dtoWrite.getName());
        assertEquals(result.getPrice(), dtoWrite.getPrice());
        assertEquals(result.getCategory(), dtoWrite.getCategory());
        assertEquals(result.getExpiryDate(), dtoWrite.getExpiryDate());
    }

    @Test
    void testDeleteProduct_whenIdValid_shouldReturnNoContent(){
        // Given
        when(repository.findProductById(any(Long.class))).thenReturn(Optional.of(new Product()));
        doNothing().when(repository).delete(any(Product.class));

        // When
        var response = service.deleteProduct(204L);

        //Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteProduct_whenIdNotFound_shouldReturnNotFound(){
        // Given
        when(repository.findProductById(any(Long.class))).thenReturn(Optional.empty());

        // When
        var response = service.deleteProduct(404L);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }


    @Test
    void testFindProductById_whenIdValid_shouldReturnProduct(){
        // Given
        when(mapper.fromDtoWriteToProduct(any(ProductDtoWrite.class), any(Long.class), any(Float.class))).thenCallRealMethod();
        Product product = mapper.fromDtoWriteToProduct(dtoWrite, 1L, 0.0F);
        when(repository.findProductById(any(Long.class))).thenReturn(Optional.of(product));

        // When
        var response = service.findProductById(200L);

        //Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mapper.toDtoRead(product), response.getBody());

    }

    @Test
    void testFindProductById_whenIdNotFound_shouldReturnNotFound(){
        // Given
        when(repository.findProductById(any(Long.class))).thenReturn(Optional.empty());

        // When
        var response = service.findProductById(404L);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());

    }

    @Test
    void testUpdateProduct_whenIdValid_shouldReturnNoContent(){
        // Given
        when(mapper.fromDtoWriteToProduct(any(ProductDtoWrite.class), any(Long.class), any(Float.class))).thenCallRealMethod();
        Product product = mapper.fromDtoWriteToProduct(dtoWrite, 1L, 15f);
        when(repository.findProductById(any(Long.class))).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenReturn(null);
//        when(mapper.fromDtoWriteToProduct(any(ProductDtoWrite.class), any(Long.class), any(Float.class))).thenCallRealMethod();
        // When
        var response = service.updateProduct(204L, dtoWrite);

        //Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testUpdateProduct_whenIdNotFound_shouldReturnNotFound(){
        // Given
        when(repository.findProductById(any(Long.class))).thenReturn(Optional.empty());

        // When
        var response = service.updateProduct(404L, dtoWrite);

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateProductWithForm_whenIdValid_shouldReturnNoContent(){
        // Given
        when(repository.findProductById(any(Long.class))).thenReturn(Optional.of(new Product()));
        when(repository.save(any(Product.class))).thenReturn(null);
        // When
        var response = service.updateProduct(204L, "test", 5.0F, null, LocalDateTime.now());

        //Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testUpdateProductWithForm_whenIdNotFound_shouldReturnNotFound(){
        // Given
        when(repository.findProductById(any(Long.class))).thenReturn(Optional.empty());

        // When
        var response = service.updateProduct(204L, "test", 5.0F, null, LocalDateTime.now());

        //Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testReadAll_whenNoProducts_shouldReturnEmptyListOfDto() {
        // Given
        when(repository.findAll()).thenReturn(new ArrayList<Product>());

        // When
        var result = service.readAll().getBody();

        // Then
        assertEquals(0, result.size());
        assertTrue(result instanceof ArrayList<ProductDtoRead>);

    }

    @Test
    void testReadAll_whenProductPresent_shouldReturnListOfDto() {
        // Given
        when(mapper.toDtoRead(any(Product.class))).thenCallRealMethod();
        when(mapper.fromDtoWriteToProduct(any(ProductDtoWrite.class), any(Long.class), any(Float.class))).thenCallRealMethod();
        Product product = mapper.fromDtoWriteToProduct(dtoWrite, 1L, 44.0F);
        when(repository.findAll()).thenReturn(new ArrayList<Product>(List.of(product)));

        // When
        var result = service.readAll().getBody();

        // Then
        assertEquals(1, result.size());
        assertTrue(equals(product, result.get(0)));
    }

    private static boolean equals(Product product, ProductDtoRead dtoRead) {
        return product.getId() == dtoRead.getId() &&
                Objects.equals(product.getName(), dtoRead.getName()) &&
                product.getPrice() == dtoRead.getPrice() &&
                product.getQuantity() == dtoRead.getQuantity() &&
                product.getCategory() == dtoRead.getCategory() &&
                product.getExpiryDate() == dtoRead.getExpiryDate();
    }


}


// Given


// When


// Then

