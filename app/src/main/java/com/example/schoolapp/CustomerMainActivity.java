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
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerMainActivity extends AppCompatActivity {

    private RecyclerView recyclerProducts;
    private ProductAdapter adapter;
    private final List<Product> productList = new ArrayList<>();

    private static final List<Product> SAMPLE_PRODUCTS = Arrays.asList(
            new Product("classic-notebook", "Classic Notebook", 5.99, "product_placeholder_1"),
            new Product("gel-pen-pack", "Gel Pen Pack", 3.49, "product_placeholder_2"),
            new Product("water-bottle", "Water Bottle", 12.99, "product_placeholder_3"),
            new Product("campus-hoodie", "Campus Hoodie", 29.99, "product_placeholder_banner")
    );

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
        ImageButton btnCartBottom = findViewById(R.id.btnCartBottom);
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnProfile = findViewById(R.id.btnProfile);

        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnCartBottom.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        btnHome.setOnClickListener(v -> recyclerProducts.smoothScrollToPosition(0));
        btnProfile.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));

        loadProducts();
    }

    private void loadProducts() {
        // Firestore collection "products" must exist
        db.collection("products")
                .get()
                .addOnSuccessListener(snapshots -> {
                    if (snapshots.isEmpty()) {
                        seedSampleProducts();
                        return;
                    }
                    productList.clear();
                    for (QueryDocumentSnapshot doc : snapshots) {
                        Product p = doc.toObject(Product.class);
                        p.setId(doc.getId());
                        if (p.getImageResName() == null) {
                            p.setImageResName("product_placeholder_1");
                        }
                        productList.add(p);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Failed to load products: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }

    private void seedSampleProducts() {
        WriteBatch batch = db.batch();
        for (Product sample : SAMPLE_PRODUCTS) {
            batch.set(db.collection("products").document(sample.getId()), sample);
        }

        batch.commit()
                .addOnSuccessListener(unused -> loadProducts())
                .addOnFailureListener(e ->
                        Toast.makeText(this,
                                "Failed to seed sample products: " + e.getMessage(),
                                Toast.LENGTH_LONG).show());
    }
}
