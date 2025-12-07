package com.example.schoolapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private final List<Product> productList;

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
        Product product = productList.get(position);

        holder.name.setText(product.getName());
        holder.price.setText(String.format("KSh %.2f", product.getPrice()));
        holder.image.setImageResource(resolveImage(product, holder.itemView));

        holder.addBtn.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(product);
            Toast.makeText(context,
                    product.getName() + " added to cart",
                    Toast.LENGTH_SHORT).show();
        });
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

        ImageView image;
        TextView name, price;
        Button addBtn;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productImage);
            name = itemView.findViewById(R.id.productNameText);
            price = itemView.findViewById(R.id.productPriceText);
            addBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }
}
