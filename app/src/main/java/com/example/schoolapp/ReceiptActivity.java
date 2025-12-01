package com.example.schoolapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ReceiptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        TextView tvOrderId = findViewById(R.id.tvReceiptOrderId);
        TextView tvTotal = findViewById(R.id.tvReceiptTotal);
        TextView tvAddress = findViewById(R.id.tvReceiptAddress);
        TextView tvThanks = findViewById(R.id.tvReceiptThanks);

        String orderId = getIntent().getStringExtra("orderId");
        double total = getIntent().getDoubleExtra("totalAmount", 0);
        String address = getIntent().getStringExtra("address");

        tvOrderId.setText("Order ID: " + orderId);
        tvTotal.setText(String.format("Total paid: KSh %.2f", total));
        tvAddress.setText("Deliver to:\n" + address);
        tvThanks.setText("Thank you for your purchase!");
    }
}
