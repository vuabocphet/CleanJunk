package com.appplatform.commons.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by HoangSon on 2/28/2018.
 */

public final class NetworkUtils {
    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
    public static boolean isConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }


}
