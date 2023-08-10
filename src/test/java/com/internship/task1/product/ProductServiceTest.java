package com.internship.task1.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.CategoryEnum;
import org.openapitools.model.ProductDtoRead;
import org.openapitools.model.ProductDtoWrite;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        dtoWrite.setExpiryDate(OffsetDateTime.now());

        mockDatabase = new HashMap<>();
    }


    @Test
    void testCreateProduct_whenValidProductDtoWritePassed_shouldReturnProductDtoRead(){
        // Given
        when(repository.save(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId((long) (mockDatabase.size() + 1));
            mockDatabase.put(product.getId(), product);

            return product;
        });
        when(mapper.fromDtoWriteToProduct(any(ProductDtoWrite.class), any(Long.class), any(Float.class))).thenCallRealMethod();
        when(mapper.toDtoRead(any(Product.class))).thenCallRealMethod();
        when(kafka.send(any(), any(), any())).thenReturn(null);

        // When
        ProductDtoRead result = service.save(dtoWrite);

        //Then
        assertEquals(1L, result.getId());     //TODO testy integracyjne (repository.save() by nadac id)
        assertEquals(1, mockDatabase.size());
        assertEquals(result.getName(), mockDatabase.get(1L).getName());
        assertEquals(result.getPrice(), mockDatabase.get(1L).getPrice());
        assertEquals(result.getCategory(), mockDatabase.get(1L).getCategory());
        assertEquals(result.getExpiryDate().toLocalDateTime(), mockDatabase.get(1L).getExpiryDate());
    }

    @Test
    void testDeleteProduct_whenIdValid_shouldReturnNoContent(){
        // Given
        when(repository.findProductById(any(Long.class))).thenReturn(Optional.of(new Product()));
        doAnswer(invocationOnMock -> {
            return mockDatabase.remove(1L);
        }).when(repository).delete(any(Product.class));

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
        Product product = Product.builder()
                .id(1L)
                .name("ser krolewski Sierpc")
                .price(5.99F)
                .quantity(4.0F)
                .category(CategoryEnum.DAIRY_PRODUCTS)
                .expiryDate(LocalDateTime.now())
                .build();
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
        Product product = Product.builder()
                .id(1L)
                .name("ser krolewski Sierpc")
                .price(5.99F)
                .quantity(4.0F)
                .category(CategoryEnum.DAIRY_PRODUCTS)
                .expiryDate(LocalDateTime.now())
                .build();
        when(repository.findProductById(any(Long.class))).thenReturn(Optional.of(product));
        when(repository.save(any(Product.class))).thenReturn(null);
        when(mapper.fromDtoWriteToProduct(any(ProductDtoWrite.class), any(Long.class), any(Float.class))).thenCallRealMethod();

        // When
        var response = service.updateProduct(204L, dtoWrite);

        //Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }




}
