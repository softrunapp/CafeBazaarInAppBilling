package com.softrunapp.cafebazaarbillingsample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.softrunapp.cafebazaarbilling.CafebazaarBilling;
import com.softrunapp.cafebazaarbilling.CafebazaarBillingListener;
import com.softrunapp.cafebazaarbilling.util.Purchase;

public class MainActivity extends AppCompatActivity implements CafebazaarBillingListener {
    private static final String rsaKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwC8SR3KLxmir68Xgkn2G9AxCCXDwmoc78PVB3JeUXsglLE8CV9l9aetr7/hW/MXQl3QGkuK0vRPHUHxb3xxhZjXPIRZYHSvn74dkHXLuUEPi20Fr6FCCDbcNChkDR/Jv1oXEmlteQbqDQY+8ZtIsRFjsaiECB+J81OutOR2+YwmGnBLOt/MryTvpxOqXPFLehRLf2uPmDevMcHtrZKnMf5v0g7HiwkhYOO05Uy3loUCAwEAAQ==";
    private static final String sku = "test";

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

    public void payment(View view) {
        cafebazaarBilling.purchase(sku);
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