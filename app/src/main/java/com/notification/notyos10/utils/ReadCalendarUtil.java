package com.notification.notyos10.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.notification.notyos10.objects.EventEntity;

import java.util.ArrayList;

public class ReadCalendarUtil {
    public static ArrayList<EventEntity> nameOfEvent = new ArrayList<EventEntity>();
    public static ArrayList<EventEntity> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation"}, null,
                        null, null);
        nameOfEvent.clear();
        if (cursor!= null && cursor.moveToFirst()) {
            do {
                try {
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
        return nameOfEvent;
    }
}
