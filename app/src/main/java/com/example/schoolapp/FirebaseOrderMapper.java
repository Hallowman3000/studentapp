package com.example.schoolapp;

import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseOrderMapper {

    public static List<Map<String, Object>> buildItemsArray(List<CartItem> items) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CartItem ci : items) {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", ci.getProduct().getId());
            map.put("name", ci.getProduct().getName());
            map.put("price", ci.getProduct().getPrice());
            map.put("quantity", ci.getQuantity());
            list.add(map);
        }
        return list;
    }
}
