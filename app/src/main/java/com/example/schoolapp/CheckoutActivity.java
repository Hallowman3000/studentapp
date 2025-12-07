package com.example.schoolapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.approve.Approval;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.createorder.Amount;
import com.paypal.checkout.createorder.AppContext;
import com.paypal.checkout.createorder.CreateOrder;
import com.paypal.checkout.createorder.CreateOrderActions;
import com.paypal.checkout.createorder.CurrencyCode;
import com.paypal.checkout.createorder.Order;
import com.paypal.checkout.createorder.OrderIntent;
import com.paypal.checkout.createorder.PurchaseUnit;
import com.paypal.checkout.createorder.UserAction;
import com.paypal.checkout.error.ErrorInfo;
import com.paypal.checkout.error.OnError;
import com.paypal.checkout.order.actions.OnCancel;
import com.paypal.checkout.order.actions.OnApprove;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private static final String PAYPAL_CLIENT_ID = "Afpzo6rnOTWS660bPQqo79G94n90zLcMxTHgPtmEbeFRWydhbH3g5MygXg28E2O0_JntGcMgT1FOOxpR";

    private TextView tvCheckoutTotal;
    private EditText etAddress;
    private EditText etPaypalEmail;
    private Button btnPayPal;

    private double totalAmount;
    private String pendingAddress;
    private String pendingPaypalEmail;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        tvCheckoutTotal = findViewById(R.id.tvCheckoutTotal);
        etAddress = findViewById(R.id.etDeliveryAddress);
        etPaypalEmail = findViewById(R.id.etPaypalEmail);
        btnPayPal = findViewById(R.id.btnPayPal);

        totalAmount = CartManager.getInstance().getTotal();
        tvCheckoutTotal.setText(String.format("Total: KSh %.2f", totalAmount));

        configurePayPalCheckout();
        btnPayPal.setOnClickListener(v -> startPayPalCheckout());
    }

    private void configurePayPalCheckout() {
        CheckoutConfig checkoutConfig = new CheckoutConfig(
                getApplication(),
                PAYPAL_CLIENT_ID,
                Environment.LIVE,
                CurrencyCode.KES,
                UserAction.PAY_NOW,
                new SettingsConfig(true, false)
        );

        PayPalCheckout.setConfig(checkoutConfig);

        PayPalCheckout.registerCallbacks(
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        runOnUiThread(() -> handlePaymentSuccess());
                    }
                },
                new OnCancel() {
                    @Override
                    public void onCancel() {
                        runOnUiThread(() -> Toast.makeText(CheckoutActivity.this, "PayPal payment cancelled", Toast.LENGTH_LONG).show());
                    }
                },
                new OnError() {
                    @Override
                    public void onError(@NotNull ErrorInfo errorInfo) {
                        runOnUiThread(() ->
                                Toast.makeText(CheckoutActivity.this, "PayPal error: " + errorInfo.getReason(), Toast.LENGTH_LONG).show());
                    }
                }
        );
    }

    private void startPayPalCheckout() {
        String address = etAddress.getText().toString().trim();
        if (address.isEmpty()) {
            etAddress.setError("Required");
            etAddress.requestFocus();
            return;
        }

        String paypalEmail = etPaypalEmail.getText().toString().trim();
        if (paypalEmail.isEmpty()) {
            etPaypalEmail.setError("PayPal account required");
            etPaypalEmail.requestFocus();
            return;
        }

        pendingAddress = address;
        pendingPaypalEmail = paypalEmail;

        PayPalCheckout.start(new CreateOrder() {
            @Override
            public void create(@NotNull CreateOrderActions createOrderActions) {
                PurchaseUnit purchaseUnit = new PurchaseUnit.Builder()
                        .amount(new Amount.Builder()
                                .currencyCode(CurrencyCode.KES)
                                .value(String.format("%.2f", totalAmount))
                                .build())
                        .description("StudentApp order")
                        .build();

                Order order = new Order(
                        OrderIntent.CAPTURE,
                        new AppContext.Builder()
                                .userAction(UserAction.PAY_NOW)
                                .build(),
                        Collections.singletonList(purchaseUnit)
                );

                createOrderActions.create(order, null);
            }
        });
    }

    private void handlePaymentSuccess() {
        String address = pendingAddress != null ? pendingAddress : etAddress.getText().toString().trim();
        String uid = auth.getUid();

        Map<String, Object> order = new HashMap<>();
        order.put("userId", uid);
        order.put("deliveryAddress", address);
        order.put("paypalAccount", pendingPaypalEmail);
        order.put("totalAmount", totalAmount);
        order.put("paymentStatus", "PAID");
        order.put("createdAt", FieldValue.serverTimestamp());
        order.put("items", FirebaseOrderMapper.buildItemsArray(CartManager.getInstance().getItems()));

        db.collection("orders")
                .add(order)
                .addOnSuccessListener(docRef -> {
                    String orderId = docRef.getId();
                    CartManager.getInstance().clear();

                    android.content.Intent intent = new android.content.Intent(CheckoutActivity.this, ReceiptActivity.class);
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
