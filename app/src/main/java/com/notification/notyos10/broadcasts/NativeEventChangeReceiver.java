package com.notification.notyos10.broadcasts;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.notification.notyos10.services.NotifyService;

import java.util.Calendar;

public class NativeEventChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final SharedPreferences prefs = context.getSharedPreferences("USER_CONSTANTS", Context.MODE_PRIVATE);
        long lastTimeValidated = prefs.getLong("LONG_LAST_TIME_VALIDATED", 0);
        int numRowsLastTimeValidated = prefs.getInt("INT_NUM_ROWS_LAST_TIME_VALIDATED", 0);
        int numberOfInstances = getNumberOfInstances(lastTimeValidated, context);
        if (numberOfInstances != numRowsLastTimeValidated) {
            NotifyService mService = NotifyService.getInstance();
            if (mService == null) {
            } else {
                mService.updateGetEvent();
            }
        }
    }


    private int getNumberOfInstances(long lastTimeValidated, Context context) {
        Calendar beginTime = Calendar.getInstance();
        beginTime.setTimeInMillis(lastTimeValidated);
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.YEAR, 1);
        endTime.add(Calendar.DAY_OF_MONTH, 1);//now + 366
        long startMillis = beginTime.getTimeInMillis();
        long endMillis = endTime.getTimeInMillis();
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();
        // Construct the query with the desired date range.
        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, startMillis);
        ContentUris.appendId(builder, endMillis);
        // Submit the query
        cur = cr.query(builder.build(), null, null, null, null);
        //handle results
        return cur.getCount();
    }
}
