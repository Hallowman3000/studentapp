package com.example.schoolapp;

import android.app.Application;

import com.paypal.checkout.PayPalCheckout;
import com.paypal.checkout.config.CheckoutConfig;
import com.paypal.checkout.config.Environment;
import com.paypal.checkout.config.SettingsConfig;
import com.paypal.checkout.order.CurrencyCode;
import com.paypal.checkout.order.UserAction;

public class SchoolApp extends Application {

    private static final String PAYPAL_CLIENT_ID = "Afpzo6rnOTWS660bPQqo79G94n90zLcMxTHgPtmEbeFRWydhbH3g5MygXg28E2O0_JntGcMgT1FOOxpR";

    @Override
    public void onCreate() {
        super.onCreate();

        CheckoutConfig checkoutConfig = new CheckoutConfig(
                this,
                PAYPAL_CLIENT_ID,
                Environment.SANDBOX,
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                new SettingsConfig(true, false)
        );
        PayPalCheckout.setConfig(checkoutConfig);
    }
}
