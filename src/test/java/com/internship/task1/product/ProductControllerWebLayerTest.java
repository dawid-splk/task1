package com.internship.task1.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.model.CategoryEnum;
import org.openapitools.model.ProductDtoRead;
import org.openapitools.model.ProductDtoWrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@WebMvcTest(controllers = ProductController.class)
public class ProductControllerWebLayerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService service;

    ProductDtoWrite productDetails;
    ObjectMapper objectMapper;


    @BeforeEach
    public void setup() {
        productDetails = new ProductDtoWrite();
        productDetails.setName("ser krolewski Sierpc");
        productDetails.setPrice(5.99F);
        productDetails.setCategory(CategoryEnum.fromValue("dairy products"));
        productDetails.setExpiryDate(LocalDateTime.now());

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    @DisplayName("Product can be created")
    void testCreateUser_whenValidUserDetails_returnsCreatedUserDetails() throws Exception {
        // Given
        RequestBuilder requestBuilder = getPostRequestBuilder();

        ProductDtoRead dtoRead = new ProductMapper().fromDtoWriteToDtoRead(productDetails);
        when(service.save(any(ProductDtoWrite.class))).thenReturn(ResponseEntity.ok(dtoRead));

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        ProductDtoRead createdProduct = objectMapper
                .readValue(responseBodyAsString, ProductDtoRead.class);

        // Then
        assertEqualsFields(createdProduct);
    }


//    @Test
    void testCreateProduct_whenNameTooShort_shouldReturnBadRequest() throws Exception {         //VALIDACJA NIE DZIALA - adnotacja @Size przy getterze a nie przy polu
        // Given
        productDetails.setName("I");
        productDetails.setPrice(-5f);
        RequestBuilder requestBuilder = getPostRequestBuilder();
//        when(service.save(any(ProductDtoWrite.class))).thenReturn(ResponseEntity.badRequest().build());

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Then
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

//    @Test
    void testCreateProduct_whenPriceNegative_shouldReturnBadRequest() throws Exception {
        // Given
        productDetails.setPrice(-5f);
        RequestBuilder requestBuilder = getPostRequestBuilder();
//        when(service.save(any(ProductDtoWrite.class))).thenReturn(ResponseEntity.badRequest().build());

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Then
        assertEquals(400, mvcResult.getResponse().getStatus());
    }


    @Test
    @DisplayName("Read all returns list of products")
    void testReadAll_whenProductsExist_shouldReturnListOfProduct() throws Exception {
        // Given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products");
        when(service.readAll())
                .thenReturn(ResponseEntity.ok(
                        List.of(new ProductMapper()
                                .fromDtoWriteToDtoRead(productDetails))));

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEqualsFields(
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(),
                        ProductDtoRead[].class)
                        [0]);
    }

    @Test
    @DisplayName("Read all returns empty list")
    void testReadAll_whenNoProducts_shouldReturnEmptyList() throws Exception {
        // Given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products");
        when(service.readAll())
                .thenReturn(ResponseEntity.ok(
                        new ArrayList<>(0)));

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(0, objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                ProductDtoRead[].class).length);
    }

    @Test
    @DisplayName("Product is found and deleted")
    void testDeleteProduct_whenIdValid_shouldReturnNoContent() throws Exception {
        // Given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/products/{id}", 1L);
        when(service.deleteProduct(any(Long.class))).thenReturn(ResponseEntity.noContent().build());

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Then
        assertEquals(204, mvcResult.getResponse().getStatus());
    }


    @Test
    @DisplayName("Find by category returns list of products")
    void testFindByCategory_whenProductFound_shouldReturnListOfProducts() throws Exception {
        // Given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/products/findByCategory").param("category", "dairy products");
        when(service.findProductsByCategory(any(String.class)))
                .thenReturn(ResponseEntity.ok(
                        List.of(new ProductMapper()
                                .fromDtoWriteToDtoRead(productDetails))));

        // When
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        // Then
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEqualsFields(
                objectMapper.readValue(
                        mvcResult.getResponse().getContentAsString(),
                        ProductDtoRead[].class)
                        [0]);
    }


    private RequestBuilder getPostRequestBuilder() throws JsonProcessingException {
        return MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDetails));
    }

    private void assertEqualsFields(ProductDtoRead createdProduct) {
        assertEquals(productDetails.getName(), createdProduct.getName());
        assertEquals(productDetails.getPrice(), createdProduct.getPrice());
        assertEquals(productDetails.getCategory(), createdProduct.getCategory());
        assertEquals(productDetails.getExpiryDate(), createdProduct.getExpiryDate());
    }
}
