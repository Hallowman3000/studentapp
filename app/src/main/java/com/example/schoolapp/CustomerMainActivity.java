package com.example.schoolapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerMainActivity extends AppCompatActivity {

    private RecyclerView recyclerProducts;
    private ImageButton btnCart;

    private ProductAdapter productAdapter;
    private List<Product> productList;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        db = FirebaseFirestore.getInstance();

        recyclerProducts = findViewById(R.id.recyclerProducts);
        btnCart = findViewById(R.id.btnCart);
        ImageButton btnHome = findViewById(R.id.btnHome);
        ImageButton btnCartBottom = findViewById(R.id.btnCartBottom);
        ImageButton btnProfile = findViewById(R.id.btnProfile);

        productList = new ArrayList<>();

        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this, productList);
        recyclerProducts.setAdapter(productAdapter);

        seedSampleProducts();
        loadProductsFromFirestore();

        btnCart.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class))
        );

        btnHome.setOnClickListener(v -> recyclerProducts.smoothScrollToPosition(0));
        btnCartBottom.setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class))
        );
        btnProfile.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );
    }

    private void seedSampleProducts() {
        Map<String, Object> officeShoes = new HashMap<>();
        officeShoes.put("name", "Office Shoes");
        officeShoes.put("price", 1.0);
        officeShoes.put("available", true);
        officeShoes.put("imageResName", "shoe_office");

        Map<String, Object> officialShoes = new HashMap<>();
        officialShoes.put("name", "Official Shoes");
        officialShoes.put("price", 1.0);
        officialShoes.put("available", true);
        officialShoes.put("imageResName", "shoe_official");

        Map<String, Object> runningShoes = new HashMap<>();
        runningShoes.put("name", "Running Shoes");
        runningShoes.put("price", 1.0);
        runningShoes.put("available", true);
        runningShoes.put("imageResName", "shoe_running");

        Map<String, Object> sportShoes = new HashMap<>();
        sportShoes.put("name", "Sport Shoes");
        sportShoes.put("price", 1.0);
        sportShoes.put("available", true);
        sportShoes.put("imageResName", "shoe_sport");

        Map<String, Object> casualShoes = new HashMap<>();
        casualShoes.put("name", "Casual Shoes");
        casualShoes.put("price", 1.0);
        casualShoes.put("available", true);
        casualShoes.put("imageResName", "shoe_casual");

        db.collection("products").document("office_shoes").set(officeShoes);
        db.collection("products").document("official_shoes").set(officialShoes);
        db.collection("products").document("running_shoes").set(runningShoes);
        db.collection("products").document("sport_shoes").set(sportShoes);
        db.collection("products").document("casual_shoes").set(casualShoes);
    }

    private void loadProductsFromFirestore() {
        db.collection("products")
                .get()
                .addOnCompleteListener(this::handleProductsResult);
    }

    private void handleProductsResult(@NonNull Task<QuerySnapshot> task) {
        if (!task.isSuccessful()) {
            Toast.makeText(this,
                    "Error loading products: " +
                            (task.getException() != null ? task.getException().getMessage() : "unknown"),
                    Toast.LENGTH_LONG).show();
            return;
        }

        productList.clear();

        QuerySnapshot snap = task.getResult();
        if (snap != null) {
            for (DocumentSnapshot doc : snap.getDocuments()) {
                Product p = doc.toObject(Product.class);
                if (p != null) {
                    p.setId(doc.getId());
                    if (p.getImageResName() == null || p.getImageResName().isEmpty()) {
                        p.setImageResName("ic_product_placeholder");
                    }
                    productList.add(p);
                }
            }
        }

        productAdapter.notifyDataSetChanged();

        Toast.makeText(this,
                "Loaded " + productList.size() + " products",
                Toast.LENGTH_SHORT).show();
    }
}
