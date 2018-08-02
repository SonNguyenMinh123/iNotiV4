package com.notification.inotythemehhhhh.mycenterutils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.notification.inotythemehhhhh.objects.EventEntity;

import java.util.ArrayList;

public class ReadCalendarUtil {
//    public static final String TAG = "ReadCalendarUtil";
    public static ArrayList<EventEntity> nameOfEvent = new ArrayList<EventEntity>();

    public static ArrayList<EventEntity> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation"}, null,
                        null, null);
        nameOfEvent.clear();
        if (cursor.moveToFirst()) {
            do {
                try {
//                    LogUtil.getLogger().d(TAG, cursor.getString(cursor.getColumnIndex("title")) + "-" + cursor.getLong(cursor.getColumnIndex("dtstart")) + " - " + cursor.getLong(cursor.getColumnIndex("dtend")));
                    EventEntity eventEntity = new EventEntity();
                    eventEntity.setNameOfEvent(cursor.getString(cursor.getColumnIndex("title")));
                    eventEntity.setDescriptions(cursor.getString(cursor.getColumnIndex("description")));
                    eventEntity.setStartEvent(cursor.getLong(cursor.getColumnIndex("dtstart")));
                    eventEntity.setEndEvent(cursor.getLong(cursor.getColumnIndex("dtend")));
                    nameOfEvent.add(eventEntity);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());

        }

        // fetching calendars name
      /*  String CNames[] = new String[cursor.getCount()];

        // fetching calendars id

        for (int i = 0; i < CNames.length; i++) {

            CNames[i] = cursor.getString(1);
            cursor.moveToNext();

        }*/
        return nameOfEvent;
    }


}
