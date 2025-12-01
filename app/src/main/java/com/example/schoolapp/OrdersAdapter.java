package com.example.schoolapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    private final List<Order> orders;
    private final OnOrderClickListener listener;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());

    public OrdersAdapter(List<Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_admin, parent, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order o = orders.get(position);

        holder.tvOrderId.setText("Order: " + o.getId());
        holder.tvAmount.setText(String.format("KSh %.2f", o.getTotalAmount()));
        holder.tvStatus.setText("Status: " + o.getPaymentStatus());

        if (o.getCreatedAt() != null) {
            String dateStr = sdf.format(o.getCreatedAt().toDate());
            holder.tvDate.setText(dateStr);
        } else {
            holder.tvDate.setText("Date: -");
        }

        String addressPreview = o.getDeliveryAddress() != null ? o.getDeliveryAddress() : "";
        if (addressPreview.length() > 40) {
            addressPreview = addressPreview.substring(0, 40) + "...";
        }
        holder.tvAddress.setText(addressPreview);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onOrderClick(o);
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderId, tvAmount, tvStatus, tvDate, tvAddress;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvAmount = itemView.findViewById(R.id.tvOrderAmount);
            tvStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvDate = itemView.findViewById(R.id.tvOrderDate);
            tvAddress = itemView.findViewById(R.id.tvOrderAddress);
        }
    }
}
