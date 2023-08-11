package com.internship.task1.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
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
        assertEquals(productDetails.getName(), createdProduct.getName());
        assertEquals(productDetails.getPrice(), createdProduct.getPrice());
        assertEquals(productDetails.getCategory(), createdProduct.getCategory());
        assertEquals(productDetails.getExpiryDate(), createdProduct.getExpiryDate());

    }




    private RequestBuilder getPostRequestBuilder() throws JsonProcessingException {
        return MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDetails));
    }
}
