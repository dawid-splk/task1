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
        ProductDtoRead result = new ProductDtoRead();

        result.setId(product.getId());
        result.setName(product.getName());
        result.setPrice(product.getPrice());
        result.setQuantity(product.getQuantity());
        result.setCategory(product.getCategory());
        result.setExpiryDate(product.getExpiryDate());

        return result;

    }

    public ProductDtoRead fromDtoWriteToDtoRead(ProductDtoWrite product){

        ProductDtoRead result = new ProductDtoRead();

        result.setName(product.getName());
        result.setPrice(product.getPrice());
        result.setCategory(product.getCategory());
        result.setExpiryDate(product.getExpiryDate());

        return result;
    }

    public Product fromDtoWriteToProduct(ProductDtoWrite dto, Long id, float quantity){

        return Product.builder()
                .id(id != -1 ? id : null)
                .name(dto.getName())
                .price(dto.getPrice())
                .quantity(quantity)
                .category(dto.getCategory())
                .expiryDate(dto.getExpiryDate())
                .build();
    }
}
