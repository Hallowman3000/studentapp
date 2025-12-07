package com.example.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CustomerMainActivity extends AppCompatActivity {

    private RecyclerView recyclerProducts;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        db = FirebaseFirestore.getInstance();

        recyclerProducts = findViewById(R.id.recyclerProducts);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ProductAdapter(productList, product -> {
            CartManager.getInstance().addToCart(product);
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        });
        recyclerProducts.setAdapter(adapter);

        ImageButton btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class))
        );

        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnCartBottom = findViewById(R.id.btnCartBottom);
        ImageButton btnProfile = findViewById(R.id.btnProfile);

        btnHome.setOnClickListener(v -> recyclerProducts.smoothScrollToPosition(0));
        btnCartBottom.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class))
        );
        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );

        loadProducts();
    }

    private void loadProducts() {
        // Firestore collection "products" must exist
        db.collection("products")
                .get()
                .addOnSuccessListener(snapshots -> {
                    productList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Product p = doc.toObject(Product.class);
                        p.setId(doc.getId());
                        if (p.getImageResName() == null || p.getImageResName().isEmpty()) {
                            p.setImageResName("ic_product_placeholder");
                        }
                        productList.add(p);
                    }
                    if (productList.isEmpty()) {
                        productList.addAll(seedSamples());
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    productList.clear();
                    productList.addAll(seedSamples());
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this,
                            "Failed to load products: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
    }

    private List<Product> seedSamples() {
        List<Product> samples = new ArrayList<>();
        samples.add(new Product("seed-gym", "Gym Shoes", 1.0, "ic_gym_shoes"));
        samples.add(new Product("seed-run", "Running Shoes", 1.0, "ic_running_shoes"));
        samples.add(new Product("seed-casual", "Casual Shoes", 1.0, "ic_casual_shoes"));
        samples.add(new Product("seed-office", "Office Shoes", 1.0, "ic_office_shoes"));
        samples.add(new Product("seed-date", "Date Night Shoes", 1.0, "ic_date_night_shoes"));
        return samples;
    }
}
