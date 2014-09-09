package com.github.sondt87.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by sondt_000 on 9/9/2014.
 */
public class NetworkUtil {
    private static final String DEFAULT_MAC_ADDRESS = "";
    /**
     * Require permission: "android.permission.ACCESS_WIFI_STATE" </br>
     *
     * @author son.dt
     * @param context
     * @return Mac address
     */
    public static final String getMacAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String MacAddress = wifiInfo.getMacAddress();
        if (MacAddress == null)
            MacAddress = DEFAULT_MAC_ADDRESS;
        return MacAddress;
    }

    public static final boolean checkInternetAvailable(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null)
            return false;
        if (!i.isConnected())
            return false;
        if (!i.isAvailable())
            return false;
        return true;
    }
}
