package com.example.schoolapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartChangedListener {
        void onCartUpdated();
    }

    private final List<CartItem> items;
    private final OnCartChangedListener listener;

    public CartAdapter(List<CartItem> items, OnCartChangedListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.tvName.setText(item.getProduct().getName());
        holder.tvQty.setText("Qty: " + item.getQuantity());
        holder.tvAmount.setText(String.format("KSh %.2f", item.getTotal()));
        holder.tvPrice.setText(String.format("Unit: KSh %.2f", item.getProduct().getPrice()));
        holder.ivProduct.setImageResource(item.getProduct().getImageResource(holder.itemView.getContext()));

        holder.btnPlus.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(holder.getAdapterPosition());
            notifyCartUpdated();
        });

        holder.btnMinus.setOnClickListener(v -> {
            int q = item.getQuantity() - 1;
            if (q <= 0) {
                CartManager.getInstance().removeItem(item.getProduct().getId());
                notifyItemRemoved(holder.getAdapterPosition());
            } else {
                item.setQuantity(q);
                notifyItemChanged(holder.getAdapterPosition());
            }
            notifyCartUpdated();
        });

        holder.btnRemove.setOnClickListener(v -> {
            CartManager.getInstance().removeItem(item.getProduct().getId());
            notifyItemRemoved(holder.getAdapterPosition());
            notifyCartUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void notifyCartUpdated() {
        if (listener != null) {
            listener.onCartUpdated();
        }
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvQty, tvAmount, tvPrice;
        ImageButton btnPlus, btnMinus, btnRemove;
        ImageView ivProduct;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartName);
            tvQty = itemView.findViewById(R.id.tvCartQty);
            tvAmount = itemView.findViewById(R.id.tvCartAmount);
            tvPrice = itemView.findViewById(R.id.tvCartPrice);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            ivProduct = itemView.findViewById(R.id.imgCartItem);
        }
    }
}
