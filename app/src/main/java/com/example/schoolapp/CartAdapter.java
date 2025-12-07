package com.example.schoolapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final List<CartItem> items;
    private final CartChangedListener listener;

    public CartAdapter(List<CartItem> items, CartChangedListener listener) {
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
        holder.tvPrice.setText(String.format("KSh %.2f each", item.getProduct().getPrice()));
        holder.tvQty.setText("Qty: " + item.getQuantity());
        holder.tvAmount.setText(String.format("KSh %.2f", item.getTotal()));
        holder.ivThumb.setImageResource(resolveImage(item, holder.itemView));

        holder.btnPlus.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return;
            }
            item.setQuantity(item.getQuantity() + 1);
            notifyItemChanged(adapterPosition);
            listener.onCartChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition == RecyclerView.NO_POSITION) {
                return;
            }
            int q = item.getQuantity() - 1;
            if (q <= 0) {
                items.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
            } else {
                item.setQuantity(q);
                notifyItemChanged(adapterPosition);
            }
            listener.onCartChanged();
        });

        holder.btnRemove.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                CartItem removed = items.get(adapterPosition);
                CartManager.getInstance().removeItem(removed.getProduct());
                notifyItemRemoved(adapterPosition);
                listener.onCartChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private int resolveImage(CartItem item, View itemView) {
        String drawableName = item.getProduct().getImageResName();
        if (drawableName != null && !drawableName.isEmpty()) {
            int id = itemView.getResources().getIdentifier(drawableName, "drawable",
                    itemView.getContext().getPackageName());
            if (id != 0) {
                return id;
            }
        }
        return R.drawable.ic_product_placeholder;
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvQty, tvAmount, tvPrice;
        ImageButton btnPlus, btnMinus;
        ImageView ivThumb;
        Button btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCartName);
            tvQty = itemView.findViewById(R.id.tvCartQty);
            tvAmount = itemView.findViewById(R.id.tvCartAmount);
            tvPrice = itemView.findViewById(R.id.tvCartPrice);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            ivThumb = itemView.findViewById(R.id.ivCartThumb);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }

    public interface CartChangedListener {
        void onCartChanged();
    }
}
