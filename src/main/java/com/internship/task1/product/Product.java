package com.internship.task1.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import org.openapitools.model.CategoryEnum;

@Entity
@Table(name = "products")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private float price;
    private float quantity;
    private CategoryEnum category;
    private LocalDateTime expiryDate;

}
