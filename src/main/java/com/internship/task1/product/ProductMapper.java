package com.internship.task1.product;

import org.openapitools.model.ProductDtoRead;
import org.openapitools.model.ProductDtoWrite;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class ProductMapper {

    public ProductMapper() {
    }

    public ProductDtoRead toDtoRead(Product product){
        ZoneOffset zoneOffset = ZoneOffset.of("+01:00");
        LocalDateTime localDateTime = product.getExpiryDate();
        OffsetDateTime offsetDateTime = localDateTime.atOffset(zoneOffset);

        ProductDtoRead result = new ProductDtoRead();
        result.setId(product.getId());
        result.setName(product.getName());
        result.setPrice(product.getPrice());
        result.setCategory(product.getCategory());
        result.setExpiryDate(offsetDateTime);

        return result;
    }

    public Product fromDtoWriteToProduct(ProductDtoWrite dto){

        Product result = new Product();
        result.setName(dto.getName());
        result.setPrice(dto.getPrice());
        result.setCategory(dto.getCategory());
        result.setExpiryDate(dto.getExpiryDate().toLocalDateTime());

        return result;
    }

    public Product fromDtoReadToProduct(ProductDtoRead dto){

        Product result = new Product();

        result.setId(dto.getId());
        result.setName(dto.getName());
        result.setPrice(dto.getPrice());
        result.setCategory(dto.getCategory());
        result.setExpiryDate(dto.getExpiryDate().toLocalDateTime());

        return result;
    }
}
