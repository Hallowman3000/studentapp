package com.example.schoolapp;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static CartManager instance;
    private final List<CartItem> items = new ArrayList<>();

    private CartManager() {}

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addToCart(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }
        items.add(new CartItem(product));
    }

    public void removeOne(Product product) {
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            if (item.getProduct().getId().equals(product.getId())) {
                int q = item.getQuantity() - 1;
                if (q <= 0) {
                    items.remove(i);
                } else {
                    item.setQuantity(q);
                }
                return;
            }
        }
    }

    public void removeItem(Product product) {
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            if (item.getProduct().getId().equals(product.getId())) {
                items.remove(i);
                return;
            }
        }
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotal();
        }
        return total;
    }

    public void clear() {
        items.clear();
    }
}
