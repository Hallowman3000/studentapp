package com.example.schoolapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GooglePayConfig {

    private static JSONObject getBaseCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = new JSONObject();
        cardPaymentMethod.put("type", "CARD");

        JSONObject parameters = new JSONObject();
        parameters.put("allowedAuthMethods", new JSONArray()
                .put("PAN_ONLY")
                .put("CRYPTOGRAM_3DS"));
        parameters.put("allowedCardNetworks", new JSONArray()
                .put("VISA")
                .put("MASTERCARD"));

        cardPaymentMethod.put("parameters", parameters);

        JSONObject tokenizationSpec = new JSONObject();
        tokenizationSpec.put("type", "PAYMENT_GATEWAY");

        JSONObject tokenParams = new JSONObject();
        // TODO: replace with your real gateway and merchant ID
        tokenParams.put("gateway", "example");
        tokenParams.put("gatewayMerchantId", "exampleMerchantId");
        tokenizationSpec.put("parameters", tokenParams);

        cardPaymentMethod.put("tokenizationSpecification", tokenizationSpec);
        return cardPaymentMethod;
    }

    public static JSONObject getPaymentDataRequest(double totalAmount) throws JSONException {
        JSONObject paymentDataRequest = new JSONObject();
        paymentDataRequest.put("apiVersion", 2);
        paymentDataRequest.put("apiVersionMinor", 0);

        JSONArray allowedPaymentMethods = new JSONArray();
        allowedPaymentMethods.put(getBaseCardPaymentMethod());
        paymentDataRequest.put("allowedPaymentMethods", allowedPaymentMethods);

        JSONObject transactionInfo = new JSONObject();
        transactionInfo.put("totalPriceStatus", "FINAL");
        // For demo, convert to simple string with 2 decimals
        transactionInfo.put("totalPrice", String.format("%.2f", totalAmount / 100.0)); // or just totalAmount if in your currency
        transactionInfo.put("currencyCode", "KES");
        paymentDataRequest.put("transactionInfo", transactionInfo);

        JSONObject merchantInfo = new JSONObject();
        merchantInfo.put("merchantName", "Demo Merchant");
        paymentDataRequest.put("merchantInfo", merchantInfo);

        return paymentDataRequest;
    }
}
