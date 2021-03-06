package com.softrunapp.cafebazaarbillingsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.softrunapp.cafebazaarbilling.CafebazaarBilling;
import com.softrunapp.cafebazaarbilling.CafebazaarBillingListener;
import com.softrunapp.cafebazaarbilling.util.Inventory;
import com.softrunapp.cafebazaarbilling.util.Purchase;

public class MainActivity extends AppCompatActivity implements CafebazaarBillingListener {
    private static final String rsaKey = "Your RSA Key";
    private static final String sku = "Your SKU";

    private CafebazaarBilling cafebazaarBilling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cafebazaarBilling = new CafebazaarBilling.Builder(this)
                .setRsaKey(rsaKey)
                .setBillingListener(this)
                .build();
    }

    public void purchase(View view) {
        cafebazaarBilling.purchase(sku);
    }

    public void queryInventoryAsync(View view) {
        cafebazaarBilling.queryInventoryAsync();
    }

    @Override
    public void onFailed(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIabPurchaseFinished(Purchase purchase) {
        Toast.makeText(MainActivity.this, "onIabPurchaseFinished", Toast.LENGTH_SHORT)
                .show();
        cafebazaarBilling.consumePurchase(purchase);
    }

    @Override
    public void onConsumeFinished() {
        Toast.makeText(MainActivity.this, "onConsumeFinished", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(MainActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStartConnectingToBazaar() {
        Toast.makeText(MainActivity.this, "onStartConnectingToBazaar", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onConnectedToBazaar() {
        Toast.makeText(MainActivity.this, "onConnectedToBazaar", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onQueryInventoryFinished(Inventory inventory) {
        Toast.makeText(MainActivity.this, "onQueryInventoryFinished", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cafebazaarBilling.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        cafebazaarBilling.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
