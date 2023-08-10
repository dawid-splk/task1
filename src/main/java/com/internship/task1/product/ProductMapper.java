package com.internship.task1.product;

import lombok.NoArgsConstructor;
import org.openapitools.model.ProductDtoRead;
import org.openapitools.model.ProductDtoWrite;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@NoArgsConstructor
public class ProductMapper {

    public ProductDtoRead toDtoRead(Product product){

        ZoneOffset zoneOffset = ZoneOffset.of("+02:00");
        LocalDateTime localDateTime = product.getExpiryDate();
        OffsetDateTime offsetDateTime = localDateTime.atOffset(zoneOffset);

        return ProductDtoRead.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .category(product.getCategory())
                .expiryDate(offsetDateTime)
                .build();
    }

    public Product fromDtoWriteToProduct(ProductDtoWrite dto, Long id, float quantity){

        return Product.builder()
                .id(id != -1 ? id : null)
                .name(dto.getName())
                .price(dto.getPrice())
                .quantity(quantity)
                .category(dto.getCategory())
                .expiryDate(dto.getExpiryDate().toLocalDateTime())
                .build();
    }

}
