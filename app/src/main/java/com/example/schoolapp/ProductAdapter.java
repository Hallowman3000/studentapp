package com.example.schoolapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface OnProductClickListener {
        void onAddToCart(Product product);
    }

    private final List<Product> products;
    private final OnProductClickListener listener;

    public ProductAdapter(List<Product> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_cart_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product p = products.get(position);
        holder.tvName.setText(p.getName());
        holder.tvPrice.setText(String.format("KSh %.2f", p.getPrice()));
        holder.ivProductThumb.setImageResource(resolveImage(p, holder.itemView));
        holder.btnAdd.setOnClickListener(v -> listener.onAddToCart(p));
    }

    @Override
    public int getItemCount() {
        return products.size();
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

        TextView tvName, tvPrice;
        Button btnAdd;
        ImageView ivProductThumb;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            btnAdd = itemView.findViewById(R.id.btnAddToCart);
            ivProductThumb = itemView.findViewById(R.id.ivProductThumb);
        }
    }
}
