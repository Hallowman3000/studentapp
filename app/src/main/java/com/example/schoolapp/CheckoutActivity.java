package com.example.schoolapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.error.ErrorInfo;
import com.paypal.checkout.order.Amount;
import com.paypal.checkout.order.AppContext;
import com.paypal.checkout.order.CurrencyCode;
import com.paypal.checkout.order.OrderIntent;
import com.paypal.checkout.order.OrderRequest;
import com.paypal.checkout.order.PurchaseUnit;
import com.paypal.checkout.order.UserAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvCheckoutTotal;
    private EditText etAddress;
    private EditText etPaypalAccount;
    private Button btnPayPaypal;

    private double totalAmount;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        tvCheckoutTotal = findViewById(R.id.tvCheckoutTotal);
        etAddress = findViewById(R.id.etDeliveryAddress);
        etPaypalAccount = findViewById(R.id.etPaypalAccount);
        btnPayPaypal = findViewById(R.id.btnPayPaypal);

        totalAmount = CartManager.getInstance().getTotal();
        tvCheckoutTotal.setText(String.format("Total: KSh %.2f", totalAmount));

        btnPayPaypal.setOnClickListener(v -> startPayPalCheckout());
    }

    private void startPayPalCheckout() {
        String address = etAddress.getText().toString().trim();
        String paypalAccount = etPaypalAccount.getText().toString().trim();

        if (address.isEmpty()) {
            etAddress.setError("Required");
            etAddress.requestFocus();
            return;
        }

        if (paypalAccount.isEmpty()) {
            etPaypalAccount.setError("Required");
            etPaypalAccount.requestFocus();
            return;
        }

        final String amountValue = String.format(Locale.US, "%.2f", totalAmount);

        PayPalCheckout.start(
                new CreateOrder() {
                    @Override
                    public void create(@NonNull CreateOrderActions createOrderActions) {
                        ArrayList<PurchaseUnit> purchaseUnits = new ArrayList<>();
                        purchaseUnits.add(
                                new PurchaseUnit.Builder()
                                        .amount(new Amount.Builder()
                                                .currencyCode(CurrencyCode.USD)
                                                .value(amountValue)
                                                .build())
                                        .description("Order for " + paypalAccount)
                                        .build()
                        );

                        OrderRequest orderRequest = new OrderRequest(
                                OrderIntent.CAPTURE,
                                new AppContext.Builder()
                                        .userAction(UserAction.PAY_NOW)
                                        .build(),
                                purchaseUnits
                        );
                        createOrderActions.create(orderRequest);
                    }
                },
                new com.paypal.checkout.approve.OnApprove() {
                    @Override
                    public void onApprove(@NonNull Approval approval) {
                        approval.getOrderActions().capture(result ->
                                runOnUiThread(() -> handlePaymentSuccess(address, paypalAccount))
                        );
                    }
                },
                () -> runOnUiThread(() -> Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show()),
                new com.paypal.checkout.error.OnError() {
                    @Override
                    public void onError(@NonNull ErrorInfo errorInfo) {
                        runOnUiThread(() -> Toast.makeText(CheckoutActivity.this,
                                "PayPal error: " + errorInfo.getReason(), Toast.LENGTH_LONG).show());
                    }
                }
        );
    }

    private void handlePaymentSuccess(String address, String paypalAccount) {
        String uid = auth.getUid();

        Map<String, Object> order = new HashMap<>();
        order.put("userId", uid);
        order.put("deliveryAddress", address);
        order.put("paypalAccount", paypalAccount);
        order.put("totalAmount", totalAmount);
        order.put("paymentStatus", "PAID");
        order.put("paymentProvider", "PayPal");
        order.put("createdAt", FieldValue.serverTimestamp());
        order.put("items", FirebaseOrderMapper.buildItemsArray(CartManager.getInstance().getItems()));

        db.collection("orders")
                .add(order)
                .addOnSuccessListener(docRef -> {
                    String orderId = docRef.getId();
                    CartManager.getInstance().clear();

                    Intent intent = new Intent(CheckoutActivity.this, ReceiptActivity.class);
                    intent.putExtra("orderId", orderId);
                    intent.putExtra("totalAmount", totalAmount);
                    intent.putExtra("address", address);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save order: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
