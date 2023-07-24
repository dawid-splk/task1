package com.internship.task1.product;

//import org.openapitools.model.Product;

import org.openapitools.model.ProductDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class ProductMapper {

    public ProductMapper() {
    }

    public ProductDTO toDTO(Product product){
        ZoneOffset zoneOffset = ZoneOffset.of("+01:00");
        LocalDateTime localDateTime = product.getExpiryDate();
        OffsetDateTime offsetDateTime = localDateTime.atOffset(zoneOffset);

        ProductDTO result = new ProductDTO();
        result.setId(product.getId());
        result.setName(product.getName());
        result.setPrice(product.getPrice());
        result.setCategory(product.getCategory());
        result.setExpiryDate(offsetDateTime);

        return result;
    }

    public Product toProduct(ProductDTO dto){

        Product result = new Product();
        if(dto.getId() != null) {
            result.setId(dto.getId());
        }
        result.setName(dto.getName());
        result.setPrice(dto.getPrice());
        result.setCategory(dto.getCategory());
        result.setExpiryDate(dto.getExpiryDate().toLocalDateTime());

        return result;
    }
}
