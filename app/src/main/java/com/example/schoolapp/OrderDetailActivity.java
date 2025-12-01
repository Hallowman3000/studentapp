package com.example.schoolapp;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvOrderId, tvTotal, tvStatus, tvAddress;
    private LinearLayout itemsContainer;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        db = FirebaseFirestore.getInstance();

        tvOrderId = findViewById(R.id.tvDetailOrderId);
        tvTotal = findViewById(R.id.tvDetailTotal);
        tvStatus = findViewById(R.id.tvDetailStatus);
        tvAddress = findViewById(R.id.tvDetailAddress);
        itemsContainer = findViewById(R.id.itemsContainer);

        String orderId = getIntent().getStringExtra("orderId");
        double totalAmount = getIntent().getDoubleExtra("totalAmount", 0);
        String address = getIntent().getStringExtra("address");
        String status = getIntent().getStringExtra("status");

        tvOrderId.setText("Order ID: " + orderId);
        tvTotal.setText(String.format("Total: KSh %.2f", totalAmount));
        tvAddress.setText("Delivery address:\n" + address);
        tvStatus.setText("Status: " + status);

        loadItems(orderId);
    }

    private void loadItems(String orderId) {
        db.collection("orders")
                .document(orderId)
                .get()
                .addOnSuccessListener(this::populateItems)
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load items: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void populateItems(DocumentSnapshot doc) {
        if (!doc.exists()) return;

        List<Map<String, Object>> items = (List<Map<String, Object>>) doc.get("items");
        if (items == null || items.isEmpty()) {
            return;
        }

        itemsContainer.removeAllViews();

        for (Map<String, Object> item : items) {
            String name = String.valueOf(item.get("name"));
            Object priceObj = item.get("price");
            Object qtyObj = item.get("quantity");

            double price = 0;
            int qty = 0;
            if (priceObj instanceof Number) {
                price = ((Number) priceObj).doubleValue();
            }
            if (qtyObj instanceof Number) {
                qty = ((Number) qtyObj).intValue();
            }

            TextView tv = new TextView(this);
            tv.setText(String.format("- %s (Qty: %d) – KSh %.2f", name, qty, price * qty));
            tv.setTextSize(14f);
            itemsContainer.addView(tv);
        }
    }
}
