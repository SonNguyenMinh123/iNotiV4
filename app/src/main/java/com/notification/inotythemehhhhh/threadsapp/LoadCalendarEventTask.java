package com.notification.inotythemehhhhh.threadsapp;

import android.content.Context;
import android.os.AsyncTask;

import com.notification.inotythemehhhhh.objects.EventEntity;
import com.notification.inotythemehhhhh.mycenterutils.ReadCalendarUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


public class LoadCalendarEventTask extends AsyncTask<Integer, Void, EventEntity[]> {
    private static final int EVENT_TODAY = 0;
    private static final int EVENT_TOMORROW = 1;
    private Context mContext;
    private OnCalendarEventListener mOnCalendarEventListener;

    public LoadCalendarEventTask(Context context, OnCalendarEventListener onCalendarEventListener) {
        mContext = context;
        mOnCalendarEventListener = onCalendarEventListener;
    }

    @Override
    protected EventEntity[] doInBackground(Integer... params) {
        ArrayList<EventEntity> eventEntityArrayList = new ArrayList<>();
//        if (ReadCalendarUtil.nameOfEvent != null && ReadCalendarUtil.nameOfEvent.size() > 0) {
//            eventEntityArrayList = ReadCalendarUtil.nameOfEvent;
//        } else {
        eventEntityArrayList = ReadCalendarUtil.readCalendarEvent(mContext);
//        }
        return getEventTodayAndTomorrow(eventEntityArrayList);
    }

    @Override
    protected void onPostExecute(EventEntity[] eventEntities) {
        super.onPostExecute(eventEntities);
        mOnCalendarEventListener.onTodayEvent(eventEntities[0]);
        mOnCalendarEventListener.onTomorrowEvent(eventEntities[1]);
    }


    private EventEntity getEventToday(ArrayList<EventEntity> eventEntityArrayList) {
        for (EventEntity eventEntity :
                eventEntityArrayList) {
            if (eventEntity.getStartEvent() >= System.currentTimeMillis() && System.currentTimeMillis() <= eventEntity.getEndEvent()) {
                return eventEntity;
            }
        }
        return null;
    }

    private EventEntity getEventTomorrow(ArrayList<EventEntity> eventEntityArrayList) {
        for (EventEntity eventEntity :
                eventEntityArrayList) {
            if (eventEntity.getStartEvent() >= (System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)) && (System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)) <= eventEntity.getEndEvent()) {
                return eventEntity;
            }
        }
        return null;
    }

    private EventEntity[] getEventTodayAndTomorrow(ArrayList<EventEntity> eventEntityArrayList) {
        EventEntity[] eventEntities = new EventEntity[2];
        boolean today = false;
        boolean tomorrow = false;
        for (EventEntity eventEntity :
                eventEntityArrayList) {
            if (eventEntity.getStartEvent() <= (System.currentTimeMillis()) && (System.currentTimeMillis()) <= eventEntity.getEndEvent()) {
                eventEntities[0] = eventEntity;
                today = true;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (eventEntity.getStartEvent() <= (calendar.getTimeInMillis()) && (calendar.getTimeInMillis()) <= eventEntity.getEndEvent()) {
                eventEntities[1] = eventEntity;
                tomorrow = true;
            }
            if (today && tomorrow) {
                break;
            }
        }
        return eventEntities;
    }

    public interface OnCalendarEventListener {
        void onTodayEvent(EventEntity eventEntity);

        void onTomorrowEvent(EventEntity eventEntity);
    }

}
