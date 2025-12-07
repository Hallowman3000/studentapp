package com.example.schoolapp;

public class Product {

    private String id;
    private String name;
    private double price;
<<<<<<< HEAD
    private String imageUrl;
    private boolean available;
=======
    private String imageResName;
>>>>>>> 9820f5e673f6959a4f4a47758a47272a917df61d

    public Product() {
        // required by Firestore
    }

<<<<<<< HEAD
    public Product(String id, String name, double price, String imageUrl, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.available = available;
=======
    public Product(String id, String name, double price, String imageResName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResName = imageResName;
>>>>>>> 9820f5e673f6959a4f4a47758a47272a917df61d
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

<<<<<<< HEAD
    public void setPrice(double price) { this.price = price; }
=======
    public String getImageResName() {
        return imageResName;
    }

    // ----------- SETTERS -----------
>>>>>>> 9820f5e673f6959a4f4a47758a47272a917df61d

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public boolean isAvailable() {
        return available;
    }

<<<<<<< HEAD
    public void setAvailable(boolean available) { this.available = available; }
=======
    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageResName(String imageResName) {
        this.imageResName = imageResName;
    }
>>>>>>> 9820f5e673f6959a4f4a47758a47272a917df61d
}
