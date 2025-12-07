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
        Map<String, Object> salt = new HashMap<>();
        salt.put("name", "Salt");
        salt.put("price", 10.0);
        salt.put("imageUrl", "https://cdn.pixabay.com/photo/2014/07/10/23/14/salt-391208_1280.jpg");
        salt.put("available", true);

        Map<String, Object> blackPepper = new HashMap<>();
        blackPepper.put("name", "Black Pepper");
        blackPepper.put("price", 15.0);
        blackPepper.put("imageUrl", "https://cdn.pixabay.com/photo/2018/05/07/13/53/peppercorns-3386330_1280.jpg");
        blackPepper.put("available", true);

        Map<String, Object> garlicPowder = new HashMap<>();
        garlicPowder.put("name", "Garlic Powder");
        garlicPowder.put("price", 18.0);
        garlicPowder.put("imageUrl", "https://cleangreensimple.com/wp-content/uploads/how-to-make-garlic-powder.jpg");
        garlicPowder.put("available", true);

        Map<String, Object> onionPowder = new HashMap<>();
        onionPowder.put("name", "Onion Powder");
        onionPowder.put("price", 12.0);
        onionPowder.put("imageUrl", "https://png.pngtree.com/png-vector/20241012/ourlarge/pngtree-onion-powder-jar-on-transparent-background-png-image_14079311.png");
        onionPowder.put("available", true);

        Map<String, Object> turmeric = new HashMap<>();
        turmeric.put("name", "Turmeric");
        turmeric.put("price", 14.0);
        turmeric.put("imageUrl", "https://png.pngtree.com/png-clipart/20241129/original/pngtree-turmeric-powder-in-a-wooden-spoon-isolated-png-image_17407509.png");
        turmeric.put("available", true);

        db.collection("products").document("salt").set(salt);
        db.collection("products").document("black_pepper").set(blackPepper);
        db.collection("products").document("garlic_powder").set(garlicPowder);
        db.collection("products").document("onion_powder").set(onionPowder);
        db.collection("products").document("turmeric").set(turmeric);
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
