package com.finalproject.finalproject.model;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class ProductModel {

    @Id
    @Column(name = "product_url")
    private String url;

    @Column(name = "product_description")
    private String price;

    @Column(name = "product_price")
    private String description;

    public ProductModel() {

    }

    public ProductModel(String url, String price, String description) {
        this.url = url;
        this.price = price;
        this.description = description;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product [url=" + url + ", price=" + price + ", description=" + description + "]";
    }
}
