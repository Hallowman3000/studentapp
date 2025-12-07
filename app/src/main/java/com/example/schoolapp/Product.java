package com.example.schoolapp;

public class Product {

    private String id;
    private String name;
    private double price;
    private String imageUrl;
    private boolean available;
    private String imageResName;

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

    public Product(String id, String name, double price, String imageResName) {
        this(id, name, price, null, true);
        this.imageResName = imageResName;
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

    public String getImageResName() {
        return imageResName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) { this.available = available; }

    public void setImageResName(String imageResName) {
        this.imageResName = imageResName;
    }
}
