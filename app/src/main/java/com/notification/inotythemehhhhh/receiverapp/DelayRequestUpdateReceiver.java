package com.notification.inotythemehhhhh.receiverapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.notification.inotythemehhhhh.myservices.NotifyService;


public class DelayRequestUpdateReceiver extends BroadcastReceiver {
    public static final String TAG = DelayRequestUpdateReceiver.class.getSimpleName();
    public static final String IDREQUEST = "com.notification.inotythemehhhhh.DELAYREQUEST";
    public static boolean sSTOP_REQUEST = false;
    public static int countUpdateCalendar = 0;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (!sSTOP_REQUEST) {
           final NotifyService notifyService = NotifyService.getInstance();
            if (notifyService != null) {
                if (countUpdateCalendar >= 10) {
//                    notifyService.updateCalendar();
                    countUpdateCalendar = 0;
                }
                notifyService.updateStatusBarVisibilityControl();
            }

            countUpdateCalendar += 1;
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent serviceIntent = new Intent(IDREQUEST);
            PendingIntent pi = PendingIntent.getBroadcast(context, 1, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000, pi);
        }
    }
}