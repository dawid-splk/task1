package com.internship.task1.product;

//import org.openapitools.model.Product;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class ProductMapper {

    public org.openapitools.model.Product toDTO(Product product){
        ZoneOffset zoneOffset = ZoneOffset.of("+01:00");
        LocalDateTime localDateTime = product.getExpiryDate();
        OffsetDateTime offsetDateTime = localDateTime.atOffset(zoneOffset);

        org.openapitools.model.Product result = new org.openapitools.model.Product();
        result.setId(product.getId());
        result.setName(product.getName());
        result.setPrice(product.getPrice());
        result.setCategory(product.getCategory());
        result.setExpiryDate(offsetDateTime);

        return result;
    }

    public Product toProduct(org.openapitools.model.Product dto){

        Product result = new Product();
        result.setId(dto.getId());
        result.setName(dto.getName());
        result.setPrice(dto.getPrice());
        result.setCategory(dto.getCategory());
        result.setExpiryDate(dto.getExpiryDate().toLocalDateTime());

        return result;
    }
}
