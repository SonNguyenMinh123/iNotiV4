package com.notification.inotythemehhhhh.receiverapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.notification.inotythemehhhhh.mycenterutils.SystemUtil;


public class NetworkChangingReceiver extends BroadcastReceiver {
    private static int sType_Network = 0;
    public static final int WIFI_NETWORK = 1;
    public static final int MOBILE_NETWORK = 2;
    private NetworkChangeReceiverCallback mNetworkChangeReceiverCallback;
    private int mWifiLevel = 0;
    private String mMobileNetWorkName = "?";

    public NetworkChangingReceiver(NetworkChangeReceiverCallback networkChangeReceiverCallback) {
        mNetworkChangeReceiverCallback = networkChangeReceiverCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifi != null && wifi.isConnectedOrConnecting()) {
            sType_Network = WIFI_NETWORK;
            mWifiLevel = SystemUtil.getLevelWifi(context);
            mNetworkChangeReceiverCallback.onNetworkChangechanged(sType_Network, mWifiLevel);
        } else if (mobile != null && mobile.isConnectedOrConnecting()) {
            sType_Network = MOBILE_NETWORK;
            mMobileNetWorkName = SystemUtil.getMobileNetworkName(context);
            mNetworkChangeReceiverCallback.onNetworkChangechanged(sType_Network, mMobileNetWorkName);
        }else {
            sType_Network=0;
            mNetworkChangeReceiverCallback.onNetworkChangechanged(sType_Network, 0);
        }
    }

    public interface NetworkChangeReceiverCallback {
        public void onNetworkChangechanged(int sType_Network, int levelWifi);

        public void onNetworkChangechanged(int sType_Network, String mobileNetworkName);
    }

}
