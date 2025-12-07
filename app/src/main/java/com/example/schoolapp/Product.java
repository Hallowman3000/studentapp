package com.example.schoolapp;

public class Product {

    private String id;
    private String name;
    private double price;
    private String imageUrl;
    private boolean available;

    public Product() {
        // required by Firestore
    }

    public Product(String id, String name, double price, String imageUrl, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.available = available;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) { this.available = available; }
}
