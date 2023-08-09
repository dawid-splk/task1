package com.internship.task1.product;

import org.openapitools.model.ProductDtoRead;
import org.openapitools.model.ProductDtoWrite;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class ProductMapper {        //TODO use lombok builder

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
        result.setQuantity(product.getQuantity());
        result.setCategory(product.getCategory());
        result.setExpiryDate(offsetDateTime);

        return result;
    }

    public Product fromDtoWriteToProduct(ProductDtoWrite dto, Long id, float quantity){

        Product result = new Product();
        if(id != -1){
            result.setId(id);
        }
        result.setQuantity(quantity);

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
        result.setQuantity(dto.getQuantity());
        result.setCategory(dto.getCategory());
        result.setExpiryDate(dto.getExpiryDate().toLocalDateTime());

        return result;
    }
}
