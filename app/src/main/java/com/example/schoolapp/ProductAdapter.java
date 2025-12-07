package com.example.schoolapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
<<<<<<< HEAD
import android.widget.Toast;
=======
>>>>>>> 9820f5e673f6959a4f4a47758a47272a917df61d

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = (productList != null) ? productList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
<<<<<<< HEAD
        Product product = productList.get(position);

        holder.name.setText(product.getName());
        holder.price.setText("KSh " + product.getPrice());

        // You can add your own logic here to load images from local storage

        holder.addBtn.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product);
            Toast.makeText(context,
                    product.getName() + " added to cart",
                    Toast.LENGTH_SHORT).show();
        });
=======
        Product p = products.get(position);
        holder.tvName.setText(p.getName());
        holder.tvPrice.setText(String.format("KSh %.2f", p.getPrice()));
        holder.ivProductThumb.setImageResource(resolveImage(p, holder.itemView));
        holder.btnAdd.setOnClickListener(v -> listener.onAddToCart(p));
>>>>>>> 9820f5e673f6959a4f4a47758a47272a917df61d
    }

    @Override
    public int getItemCount() {
        return (productList == null) ? 0 : productList.size();
    }

    private int resolveImage(Product product, View itemView) {
        String drawableName = product.getImageResName();
        if (drawableName != null && !drawableName.isEmpty()) {
            int id = itemView.getResources().getIdentifier(drawableName, "drawable",
                    itemView.getContext().getPackageName());
            if (id != 0) {
                return id;
            }
        }
        return R.drawable.ic_product_placeholder;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

<<<<<<< HEAD
        ImageView image;
        TextView name, price;
        Button addBtn;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productNameText);
            price = itemView.findViewById(R.id.productPriceText);
            addBtn = itemView.findViewById(R.id.addToCartBtn);
=======
        TextView tvName, tvPrice;
        Button btnAdd;
        ImageView ivProductThumb;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            btnAdd = itemView.findViewById(R.id.btnAddToCart);
            ivProductThumb = itemView.findViewById(R.id.ivProductThumb);
>>>>>>> 9820f5e673f6959a4f4a47758a47272a917df61d
        }
    }

    }

