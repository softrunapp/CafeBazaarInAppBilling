package com.softrunapp.cafebazaarbilling;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.softrunapp.cafebazaarbilling.util.IabHelper;
import com.softrunapp.cafebazaarbilling.util.Purchase;


public class CafebazaarBilling {
    private static final String TAG = "CafebazaarBilling";
    private static final int RC_REQUEST = 1990;

    private IabHelper mHelper;
    private Activity activity;
    private String rsaKey;
    private CafebazaarBillingListener billingListener;
    private String sku;

    private CafebazaarBilling(Activity activity, String rsaKey, CafebazaarBillingListener billingListener) {
        this.activity = activity;
        this.rsaKey = rsaKey;
        this.billingListener = billingListener;
    }

    public void purchase(String sku) {
        this.sku = sku;
        connect();
    }

    private void lunchPayment() {
        Log.d(TAG, "Starting Payment");
        mHelper.launchPurchaseFlow(activity, sku, RC_REQUEST,
                mPurchaseFinishedListener, "bGoa+V9g/yQDXvKRqq+JTFn4uQZbPiQJo4Fp9RzJ");
    }

    private void connect() {
        if (Utils.cafebazaarIsInstalled(activity)) {
            billingListener.onStartConnectingToBazaar();
            new Handler().postDelayed(this::paymentConfig, 1000);
        } else {
            billingListener.onFailed("ابتدا باید کافه بازار را نصب کنید");
        }
    }

    private void paymentConfig() {
        // You can find it in your Bazaar console, in the Dealers section.
        // It is recommended to add more security than just pasting it in your source code;
        mHelper = new IabHelper(activity, rsaKey);

        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(result -> {
            Log.d(TAG, "Setup finished.");
            if (!result.isSuccess()) {
                // Oh noes, there was a problem.
                Log.d(TAG, "Problem setting up In-app Billing: " + result);
                billingListener.onFailed("not Connect");
            } else {
                billingListener.onConnectedToBazaar();
                lunchPayment();
            }
        });
    }

    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = (result, purchase) -> {
        if (result.isFailure()) {
            billingListener.onCancel();
            Log.d(TAG, "Error purchasing: " + result);
            return;
        } else {
            onIabPurchaseFinished(purchase);
        }
        Log.d(TAG, purchase.toString());
    };

    private void onIabPurchaseFinished(Purchase purchase) {
        billingListener.onIabPurchaseFinished(purchase);
    }

    public void consumePurchase(Purchase purchase) {
        mHelper.consumeAsync(purchase,
                mConsumeFinishedListener);
    }

    private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            (purchase, result) -> {
                if (result.isSuccess()) {
                    billingListener.onConsumeFinished();
                    // provision the in-app purchase to the user
                    // (for example, credit 50 gold coins to player's character)
                } else {
                    consumePurchase(purchase);
                }
            };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (mHelper.handleActivityResult(requestCode, resultCode, data)) {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    public void onDestroy() {
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    public static class Builder {
        private Activity activity;
        private String rsaKey;
        private CafebazaarBillingListener billingListener;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder setRsaKey(String rsaKey) {
            this.rsaKey = rsaKey;
            return this;
        }

        public Builder setBillingListener(CafebazaarBillingListener billingListener) {
            this.billingListener = billingListener;
            return this;
        }

        public CafebazaarBilling build() {
            if (rsaKey == null) {
                throw new RuntimeException("RSA key is null");
            } else if (billingListener == null) {
                throw new RuntimeException("CafeBazaarBillingListener is null");
            }
            return new CafebazaarBilling(activity, rsaKey, billingListener);
        }
    }
}
