package com.tomcat360.lyqb.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.lyqb.walletsdk.model.Network;

public class NetworkUtil {

    public static Network getNetWorkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return networkInfo.getType() == ConnectivityManager.TYPE_WIFI ? Network.NETWORK_WIFI : Network.NETWORK_MOBILE;
        }
        return Network.NETWORK_NONE;
    }
}
