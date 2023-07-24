package com.internship.task1.product;

import jakarta.persistence.*;
import org.joda.time.DateTime;

import java.time.LocalDateTime;
import org.openapitools.model.ProductDTO.CategoryEnum;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private float price;
    private CategoryEnum category;
    private LocalDateTime expiryDate;

    public Product() {
    }

    public Product(Long id, String name, float price, CategoryEnum category, LocalDateTime expiryDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.expiryDate = expiryDate;
    }

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected float getPrice() {
        return price;
    }

    protected void setPrice(float price) {
        this.price = price;
    }

    protected CategoryEnum getCategory() {
        return category;
    }

    protected void setCategory(CategoryEnum category) {
        this.category = category;
    }

    protected LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    protected void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    protected Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }
}
