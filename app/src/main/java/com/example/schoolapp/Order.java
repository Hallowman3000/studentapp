package com.example.schoolapp;

import com.google.firebase.Timestamp;

import java.util.List;
import java.util.Map;

public class Order {

    private String id;                 // Firestore document ID (set manually)
    private String userId;             // UID of customer
    private String deliveryAddress;    // Address written at checkout
    private double totalAmount;        // Total paid
    private String paymentStatus;      // "PAID", "PENDING", etc.
    private Timestamp createdAt;       // Server timestamp
    private List<Map<String, Object>> items; // Items array

    // Firestore requires an empty constructor
    public Order() {}

    // ----------------- GETTERS -----------------

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    // ----------------- SETTERS -----------------

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }
}
