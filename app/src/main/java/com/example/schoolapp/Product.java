package com.example.schoolapp;

import android.content.Context;

public class Product {

    private String id;
    private String name;
    private double price;
    private String imageResName;

    // Empty constructor required by Firestore
    public Product() {
    }

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String id, String name, double price, String imageResName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResName = imageResName;
    }

    // ----------- GETTERS -----------

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageResName() {
        return imageResName;
    }

    public int getImageResource(Context context) {
        if (context == null) {
            return R.drawable.product_placeholder_1;
        }
        int resolved = context.getResources().getIdentifier(
                imageResName != null ? imageResName : "",
                "drawable",
                context.getPackageName());
        if (resolved == 0) {
            return R.drawable.product_placeholder_1;
        }
        return resolved;
    }

    // ----------- SETTERS -----------

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageResName(String imageResName) {
        this.imageResName = imageResName;
    }
}
