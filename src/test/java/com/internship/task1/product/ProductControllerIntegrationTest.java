package com.internship.task1.product;

import com.internship.task1.kafka.KafkaProducerConfig;
import org.apache.coyote.Response;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.openapitools.model.CategoryEnum;
import org.openapitools.model.ProductDtoRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.net.URI;

import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductControllerIntegrationTest {

    @Autowired
    TestRestTemplate testRestTemplate;

           //TODO odpalic z kontenerami, jak nie wyjdzie to jeszcze z drugą apka i zobaczyc czy dziala

    @Test
    @Order(1)
    @DisplayName("Product can be created")
    void testCreateProduct_whenValidProductDtoWritePassed_shouldReturnProductDtoRead() throws JSONException {
        // Given
        LocalDateTime now = LocalDateTime.parse("2023-08-03T23:59:59.999");

        JSONObject productDetailsRequestJson = new JSONObject();
        productDetailsRequestJson.put("name", "ser krolewski Sierpc");
        productDetailsRequestJson.put("price", 5.99F);
        productDetailsRequestJson.put("category", "dairy products");
        productDetailsRequestJson.put("expiry_date", now);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");

        HttpEntity<String> request = new HttpEntity<>(productDetailsRequestJson.toString(), headers);

        // When
        ResponseEntity<ProductDtoRead> response = testRestTemplate.postForEntity("/products",
                request, ProductDtoRead.class);


        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("ser krolewski Sierpc", response.getBody().getName());
        assertEquals(5.99F, response.getBody().getPrice());
        assertEquals(CategoryEnum.fromValue("dairy products"), response.getBody().getCategory());
        assertEquals(now, response.getBody().getExpiryDate());
    }


    @Test
    @Order(2)
    @DisplayName("One product of given category is found")
    void testFindByCategory_whenProductFound_shouldReturnListWithOneProduct() throws JSONException {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");

        HttpEntity<String> request = new HttpEntity<>(headers);

        // When
//        ResponseEntity<ProductDtoRead> response = testRestTemplate.getForEntity("/products/findByCategory?category=dairy products",
//                request, );

        ResponseEntity<List<ProductDtoRead>> response = testRestTemplate.exchange("/products/findByCategory?category=dairy products",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<List<ProductDtoRead>>() {
                });


        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(1, response.getBody().get(0).getId());
        assertEquals("ser krolewski Sierpc", response.getBody().get(0).getName());
        assertEquals(5.99F, response.getBody().get(0).getPrice());
        assertEquals(CategoryEnum.fromValue("dairy products"), response.getBody().get(0).getCategory());
        assertEquals(LocalDateTime.parse("2023-08-03T23:59:59.999"), response.getBody().get(0).getExpiryDate());
    }

    @Test
    @Order(3)
    void testUpdateProduct_whenValidProductDtoWritePassed_shouldReturnProductDtoRead() throws JSONException {
        // Given
        LocalDateTime dateTime = LocalDateTime.parse("2024-12-03T23:59:59.999");

        JSONObject productDetailsRequestJson = new JSONObject();
        productDetailsRequestJson.put("name", "szynka Babuni kraina wedlin");
        productDetailsRequestJson.put("price", 7.99F);
        productDetailsRequestJson.put("category", "meat");
        productDetailsRequestJson.put("expiry_date", dateTime);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");

        HttpEntity<String> request = new HttpEntity<>(productDetailsRequestJson.toString(), headers);

        // When
        ResponseEntity<ProductDtoRead> response = testRestTemplate.exchange("/products/1",
                HttpMethod.PUT,
                request,
                ProductDtoRead.class);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @Order(4)
    void testGetProductById_whenIdFound_shouldReturnProduct() throws JSONException {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/json");

        HttpEntity<String> request = new HttpEntity<>(headers);

        // When
        ResponseEntity<ProductDtoRead> response = testRestTemplate.exchange("/products/1",
                HttpMethod.GET,
                request,
                ProductDtoRead.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("szynka Babuni kraina wedlin", response.getBody().getName());
        assertEquals(7.99F, response.getBody().getPrice());
        assertEquals(CategoryEnum.fromValue("meat"), response.getBody().getCategory());
        assertEquals(LocalDateTime.parse("2024-12-03T23:59:59.999"), response.getBody().getExpiryDate());
    }
}
