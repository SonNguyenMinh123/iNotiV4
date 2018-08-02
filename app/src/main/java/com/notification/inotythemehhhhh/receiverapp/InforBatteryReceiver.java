package com.notification.inotythemehhhhh.receiverapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.notification.inotythemehhhhh.R;


public class InforBatteryReceiver extends BroadcastReceiver {
    private BatteryReceiverCallback mBatteryReceiverCallback;

    public InforBatteryReceiver(BatteryReceiverCallback batteryReceiverCallback) {
        mBatteryReceiverCallback = batteryReceiverCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int percent = intent.getIntExtra("level", 0);
        int idLevelDrawable = 0;
        if (intent.getIntExtra("plugged", 0) == 0) {
            if (percent <= 25) {
                idLevelDrawable = R.drawable.ic_battery1;
            } else if ((percent > 25) && (percent <= 60)) {
                idLevelDrawable = R.drawable.ic_battery2;
            } else if ((percent > 60) && (percent <= 80)) {
                idLevelDrawable = R.drawable.ic_battery3;
            } else {
                idLevelDrawable = R.drawable.ic_battery4;
            }
            mBatteryReceiverCallback.onInfoBattery(idLevelDrawable, percent, false);
        } else {
            if (percent <= 25) {
                idLevelDrawable = R.drawable.ic_battery_charging1;
            } else if ((percent > 25) && (percent <= 60)) {
                idLevelDrawable = R.drawable.ic_battery_charging2;
            } else if ((percent > 60) && (percent <= 80)) {
                idLevelDrawable = R.drawable.ic_battery_charging3;
            } else {
                idLevelDrawable = R.drawable.ic_battery_charging4;
            }
            mBatteryReceiverCallback.onInfoBattery(idLevelDrawable, percent, true);
        }
    }

    public interface BatteryReceiverCallback {
        public void onInfoBattery(int idLevel, int percent, boolean isPlug);
    }
}
