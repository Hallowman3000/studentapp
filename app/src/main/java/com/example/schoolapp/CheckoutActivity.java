package com.example.schoolapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvCheckoutTotal;
    private EditText etAddress;
    private Button btnPayGoogle;

    private double totalAmount;
    private PaymentsClient paymentsClient;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private final ActivityResultLauncher<Intent> paymentLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                int resultCode = result.getResultCode();
                Intent data = result.getData();

                if (resultCode == Activity.RESULT_OK && data != null) {
                    PaymentData paymentData = PaymentData.getFromIntent(data);
                    handlePaymentSuccess(paymentData);
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // User cancelled
                } else if (resultCode == AutoResolveHelper.RESULT_ERROR && data != null) {
                    Status status = AutoResolveHelper.getStatusFromIntent(data);
                    Toast.makeText(this, "Payment error: " + status, Toast.LENGTH_LONG).show();
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        tvCheckoutTotal = findViewById(R.id.tvCheckoutTotal);
        etAddress = findViewById(R.id.etDeliveryAddress);
        btnPayGoogle = findViewById(R.id.btnPayGoogle);

        totalAmount = CartManager.getInstance().getTotal();
        tvCheckoutTotal.setText(String.format("Total: KSh %.2f", totalAmount));

        paymentsClient = Wallet.getPaymentsClient(
                this,
                new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST) // TEST for demo
                        .build()
        );

        btnPayGoogle.setOnClickListener(v -> startGooglePay());
    }

    private void startGooglePay() {
        String address = etAddress.getText().toString().trim();
        if (address.isEmpty()) {
            etAddress.setError("Required");
            etAddress.requestFocus();
            return;
        }

        try {
            JSONObject paymentDataRequestJson = GooglePayConfig.getPaymentDataRequest(totalAmount);
            if (paymentDataRequestJson == null) {
                Toast.makeText(this, "Google Pay config error", Toast.LENGTH_LONG).show();
                return;
            }
            PaymentDataRequest request = PaymentDataRequest.fromJson(paymentDataRequestJson.toString());
            if (request != null) {
                AutoResolveHelper.resolveTask(
                        paymentsClient.loadPaymentData(request),
                        this,
                        991 // request code (not used because we use launcher)
                );
                // NOTE: using launcher+AutoResolveHelper is a bit redundant, but OK for demo.
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error starting Google Pay: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void handlePaymentSuccess(PaymentData paymentData) {
        // For school demo, assume payment success if we get here
        String address = etAddress.getText().toString().trim();
        String uid = auth.getUid();

        Map<String, Object> order = new HashMap<>();
        order.put("userId", uid);
        order.put("deliveryAddress", address);
        order.put("totalAmount", totalAmount);
        order.put("paymentStatus", "PAID");
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
