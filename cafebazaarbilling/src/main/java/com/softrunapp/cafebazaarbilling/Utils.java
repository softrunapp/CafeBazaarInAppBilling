package com.softrunapp.cafebazaarbilling;

import android.content.Context;
import android.content.pm.PackageManager;

public class Utils {

    public static boolean cafebazaarIsInstalled(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        try {
            pm.getPackageInfo("com.farsitel.bazaar", PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
}
