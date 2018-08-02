package com.notification.notyos10.threadsapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.notification.notyos10.objects.EventEntity;
import com.notification.notyos10.utils.ReadCalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;


public class CalendarEventTask extends AsyncTask<Integer, Void, Void> {
    private Context mContext;
    private OnCalendarEventListener mOnCalendarEventListener;

    private ArrayList<EventEntity> arrEventTodays;
    private ArrayList<EventEntity> arrEventTomorrows;

    public CalendarEventTask(Context context, OnCalendarEventListener onCalendarEventListener) {
        mContext = context;
        mOnCalendarEventListener = onCalendarEventListener;

        arrEventTodays = new ArrayList<>();
        arrEventTomorrows = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Integer... params) {
        ArrayList<EventEntity> eventEntityArrayList = new ArrayList<>();
        eventEntityArrayList = ReadCalendarUtil.readCalendarEvent(mContext);
        Log.e("test_event","ket thuc doInbackground get event : " + eventEntityArrayList.size());
        getEventTodayAndTomomrow(eventEntityArrayList);
        return null;
    }

    @Override
    protected void onPostExecute(Void eventEntities) {
        super.onPostExecute(eventEntities);
        Log.e("test_event", "ket thuc lay event , today : " + arrEventTodays.size());
        Log.e("test_event", "ket thuc lay event , tomorrow : " + arrEventTomorrows.size());
        mOnCalendarEventListener.onTodayEvent(arrEventTodays);
        mOnCalendarEventListener.onTomorrowEvent(arrEventTomorrows);
    }


    private void getEventTodayAndTomomrow(ArrayList<EventEntity> eventEntityArrayList) {
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();
        Calendar calendarEndTomorrow = Calendar.getInstance();
        int year = calendarStart.get(Calendar.YEAR);
        int month = calendarStart.get(Calendar.MONTH);
        int day = calendarStart.get(Calendar.DAY_OF_MONTH);

        calendarStart.set(Calendar.YEAR,year);
        calendarStart.set(Calendar.MONTH,month);
        calendarStart.set(Calendar.DAY_OF_MONTH,day);
        calendarStart.set(Calendar.HOUR_OF_DAY,0);
        calendarStart.set(Calendar.MINUTE,0);


        calendarEnd.set(Calendar.YEAR,year);
        calendarEnd.set(Calendar.MONTH,month);
        calendarEnd.set(Calendar.DAY_OF_MONTH,day+1);
        calendarEnd.set(Calendar.HOUR_OF_DAY,0);
        calendarEnd.set(Calendar.MINUTE,0);


        calendarEndTomorrow.set(Calendar.YEAR,year);
        calendarEndTomorrow.set(Calendar.MONTH,month);
        calendarEndTomorrow.set(Calendar.DAY_OF_MONTH,day+2);
        calendarEndTomorrow.set(Calendar.HOUR_OF_DAY,0);
        calendarEndTomorrow.set(Calendar.MINUTE,0);


        for (EventEntity eventEntity : eventEntityArrayList) {
                if (eventEntity.getStartEvent() >= (calendarStart.getTimeInMillis()) && (calendarEnd.getTimeInMillis()) > eventEntity.getEndEvent()) {
//                    if (arrEventTodays.size() == 0)
                    arrEventTodays.add(eventEntity);
                }
            Log.e("test_check_event","111111");
            }

        for (EventEntity eventEntity : eventEntityArrayList) {
            if (eventEntity.getStartEvent() >= (calendarEnd.getTimeInMillis()) && (calendarEndTomorrow.getTimeInMillis()) > eventEntity.getEndEvent()) {
                arrEventTomorrows.add(eventEntity);
            }
        }
    }

    public interface OnCalendarEventListener {

        void onTodayEvent(ArrayList<EventEntity> arrEventTodays);

        void onTomorrowEvent(ArrayList<EventEntity> arrEventTomorrows);
    }

}
