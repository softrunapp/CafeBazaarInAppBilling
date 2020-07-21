package com.softrunapp.cafebazaarbilling;

import com.softrunapp.cafebazaarbilling.util.Inventory;
import com.softrunapp.cafebazaarbilling.util.Purchase;

public interface CafebazaarBillingListener {

    void onStartConnectingToBazaar();

    void onConnectedToBazaar();

    void onIabPurchaseFinished(Purchase purchase);

    void onConsumeFinished();

    void onQueryInventoryFinished(Inventory inventory);

    void onCancel();

    void onFailed(String message);

}
