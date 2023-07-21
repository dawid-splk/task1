/*
package com.internship.task1.product;

import jakarta.persistence.*;
import org.joda.time.DateTime;

import java.time.OffsetDateTime;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private float price;
    private org.openapitools.model.Product.CategoryEnum category;
    private OffsetDateTime expiryDate;

    public Product() {
    }

    public Product(org.openapitools.model.Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.category = product.getCategory();
        this.expiryDate = product.getExpiryDate();
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

    protected org.openapitools.model.Product.CategoryEnum getCategory() {
        return category;
    }

    protected void setCategory(org.openapitools.model.Product.CategoryEnum category) {
        this.category = category;
    }

    protected OffsetDateTime getExpiryDate() {
        return expiryDate;
    }

    protected void setExpiryDate(OffsetDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    protected Long getId() {
        return id;
    }
}
*/
