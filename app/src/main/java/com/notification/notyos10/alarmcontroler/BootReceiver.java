


package com.notification.notyos10.alarmcontroler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;


public class BootReceiver extends BroadcastReceiver {
    private Calendar mCalendar;
    private AlarmReceiver mAlarmReceiver;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            mCalendar = Calendar.getInstance();
            mAlarmReceiver = new AlarmReceiver();
        }
    }
}