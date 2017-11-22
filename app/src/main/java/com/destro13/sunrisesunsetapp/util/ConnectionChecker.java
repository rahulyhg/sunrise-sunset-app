package com.destro13.sunrisesunsetapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class ConnectionChecker {
    public static boolean isConnected(Context context) {
        ConnectivityManager ConnectionManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true)
            return true;
        else
            return false;
    }

    private ConnectionChecker() {
        throw new AssertionError();
    }
}
