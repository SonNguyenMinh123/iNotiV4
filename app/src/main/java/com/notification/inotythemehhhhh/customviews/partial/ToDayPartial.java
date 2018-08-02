package com.notification.inotythemehhhhh.customviews.partial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.notification.inotythemehhhhh.R;
import com.notification.inotythemehhhhh.objects.EventEntity;
import com.notification.inotythemehhhhh.objects.NotifyEntity;
import com.notification.inotythemehhhhh.receiverapp.UpdateCurrentTimeReceiver;
import com.notification.inotythemehhhhh.myservices.NotificationMonitorService;
import com.notification.inotythemehhhhh.myservices.NotifyService;
import com.notification.inotythemehhhhh.threadsapp.LoadCalendarEventTask;
import com.notification.inotythemehhhhh.mycenterutils.DpiUtil;
import com.notification.inotythemehhhhh.customviews.others.ItemNotyTodayView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ToDayPartial extends RelativeLayout {
    private static Context mContext;
    private static ViewGroup mViewGroup;
    //    private TextViewOSNormal txvPartialTodayDate;
    private UpdateCurrentTimeReceiver mUpdateCurrentTimeReceiver;
    private TextView txvPartialTodayEvent;
    private TextView txvPartialTodayEventTomorow;
    private ArrayList<NotifyEntity> mNotyModelLeft;
    private LinearLayout lnlPartialTodayListNoty;
    private EventEntity mEventToday;
    private EventEntity mEventTomorrow;
    private ArrayList<ItemNotyTodayView> mItemNotyTodayViewArrayList;
    private static LinearLayout weather;

    public ToDayPartial(Context context) {
        super(context);
        mContext = context;
    }

    public ToDayPartial(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public static ToDayPartial fromXml(Context context, ViewGroup viewGroup) {
        ToDayPartial layout = (ToDayPartial) LayoutInflater.from(context)
                .inflate(R.layout.partial_today, viewGroup, false);
        mViewGroup = viewGroup;
        return layout;
    }

    @Override
    protected void onFinishInflate() {
        ;
        super.onFinishInflate();

//        txvPartialTodayDate = (TextViewOSNormal) findViewById(R.id.txv_partial_today__date);
//        weather = (LinearLayout) findViewById(R.id.weather);
//        Weatherlayout weatherlayout = Weatherlayout.fromXml(mContext, weather);
//        weatherlayout.openLayout();
//        Log.d("TAG", "input");

        txvPartialTodayEvent = (TextView) findViewById(R.id.txv_partial_today__event);
        txvPartialTodayEventTomorow = (TextView) findViewById(R.id.txv_partial_today__event_tomorow);
        lnlPartialTodayListNoty = (LinearLayout) findViewById(R.id.lnl_partial_today__list_noty);
        txvPartialTodayEvent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar cal = Calendar.getInstance();
                    Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, cal.getTimeInMillis());
                    Intent calIntent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                    calIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(calIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NotifyService.getInstance().toolbarPanelLayout.closePanel();
            }
        });
        txvPartialTodayEventTomorow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                    Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                    builder.appendPath("time");
                    ContentUris.appendId(builder, cal.getTimeInMillis());
                    Intent calIntent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                    calIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(calIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                NotifyService.getInstance().toolbarPanelLayout.closePanel();
            }
        });
    }

    public void openLayout(ArrayList<NotifyEntity> listNotyLeft) {

        mNotyModelLeft = listNotyLeft;
        mViewGroup.addView(this);
        initData();
        requestFocus();
        requestLayout();
    }

    public void updateNoty(ArrayList<NotifyEntity> listNotyLeft) {
        mNotyModelLeft.clear();
        mNotyModelLeft = listNotyLeft;
        lnlPartialTodayListNoty.removeAllViews();
        mItemNotyTodayViewArrayList.clear();
        loadRemoveView();
    }

    public void updateCalendar() {
        new LoadCalendarEventTask(mContext, new LoadCalendarEventTask.OnCalendarEventListener() {
            @Override
            public void onTodayEvent(EventEntity eventEntity) {
                mEventToday = eventEntity;
                if (eventEntity != null) {
                    txvPartialTodayEvent.setText(eventEntity.getNameOfEvent());
                } else {
                    txvPartialTodayEvent.setText(R.string.str_noevent_today);
                }
            }

            @Override
            public void onTomorrowEvent(EventEntity eventEntity) {
                mEventTomorrow = eventEntity;
                if (eventEntity != null) {
                    txvPartialTodayEventTomorow.setText(eventEntity.getNameOfEvent());
                } else {
                    txvPartialTodayEventTomorow.setText(R.string.str_noevent_tomorrow);
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void closeLayout() {

        mViewGroup.removeView(this);
        clearFocus();
    }

    private void initData() {
        mItemNotyTodayViewArrayList = new ArrayList<>();
        if (mUpdateCurrentTimeReceiver == null) {
            mUpdateCurrentTimeReceiver = new UpdateCurrentTimeReceiver(new UpdateCurrentTimeReceiver.UpdateCurrentTimeReceiverCallback() {
                @Override
                public void onUpdateCurrentTimeChanged() {
                    updateTime();
                }
            });
            mContext.registerReceiver(mUpdateCurrentTimeReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        }
        new LoadCalendarEventTask(mContext, new LoadCalendarEventTask.OnCalendarEventListener() {
            @Override
            public void onTodayEvent(EventEntity eventEntity) {
                mEventToday = eventEntity;
                if (eventEntity != null) {
                    txvPartialTodayEvent.setText(eventEntity.getNameOfEvent());
                } else {
                    txvPartialTodayEvent.setText(R.string.str_noevent_today);
                }
            }

            @Override
            public void onTomorrowEvent(EventEntity eventEntity) {
                mEventTomorrow = eventEntity;
                if (eventEntity != null) {
                    txvPartialTodayEventTomorow.setText(eventEntity.getNameOfEvent());
                } else {
                    txvPartialTodayEventTomorow.setText(R.string.str_noevent_tomorrow);
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        updateTime();
        loadRemoveView();

    }

    private void loadRemoveView() {
        ItemNoty:
        for (final NotifyEntity notyModel : mNotyModelLeft) {
            if (notyModel.getContentView() != null) {
                for (ItemNotyTodayView itemNotyTodayView : mItemNotyTodayViewArrayList) {
                    if (itemNotyTodayView.getTitle().equals(notyModel.getAppName())) {
                        {
                            LinearLayout linearLayout = new LinearLayout(mContext);
                            linearLayout.setMinimumHeight(2);
                            linearLayout.setBackgroundResource(R.color.line);
                            itemNotyTodayView.addLine(linearLayout);
                            View preview = notyModel.getContentView().apply(mContext, null);
                            preview.setOnTouchListener(new OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    if (event.getAction() == MotionEvent.ACTION_UP) {
                                        try {
                                            if (notyModel.getPendingIntent() != null) {
                                                notyModel.getPendingIntent().send();
                                            }
                                        } catch (PendingIntent.CanceledException e) {
                                            AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), notyModel.getPendingIntent());
                                        }


//                if (mHeaderNotyModelArrayList.get(position).isAction()) {
                                        Intent i = new Intent(NotificationMonitorService.ACTION_NLS_CONTROL);
                                        i.putExtra("command", "cancel_position");
                                        i.putExtra("id", notyModel.getId());
                                        i.putExtra("tag", notyModel.getTag());
                                        i.putExtra("packagename", notyModel.getPackageName());
                                        i.putExtra("pos", notyModel.getPosition());
                                        if (notyModel.getKey() != null) {
                                            i.putExtra("key", notyModel.getKey());
                                        }
                                        mContext.sendBroadcast(i);
                                        mNotyModelLeft.remove(notyModel);
                                        lnlPartialTodayListNoty.removeView(v);
                                        NotifyService.getInstance().toolbarPanelLayout.closePanel();
                                    }
                                    return true;
                                }
                            });
                            preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) DpiUtil.dipToPx(mContext, 70)));
                            itemNotyTodayView.addView(preview);
                            continue ItemNoty;
                        }
                    }

                }
                ItemNotyTodayView itemNotyTodayView = new ItemNotyTodayView(mContext);
                View preview = notyModel.getContentView().apply(mContext, null);
                preview.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            try {
                                if (notyModel.getPendingIntent() != null) {
                                    notyModel.getPendingIntent().send();
                                }
                            } catch (PendingIntent.CanceledException e) {
                                AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), notyModel.getPendingIntent());
                            }


//                if (mHeaderNotyModelArrayList.get(position).isAction()) {
                            Intent i = new Intent(NotificationMonitorService.ACTION_NLS_CONTROL);
                            i.putExtra("command", "cancel_position");
                            i.putExtra("id", notyModel.getId());
                            i.putExtra("tag", notyModel.getTag());
                            i.putExtra("packagename", notyModel.getPackageName());
                            i.putExtra("pos", notyModel.getPosition());
                            if (notyModel.getKey() != null) {
                                i.putExtra("key", notyModel.getKey());
                            }
                            mContext.sendBroadcast(i);
                            mNotyModelLeft.remove(notyModel);
                            mItemNotyTodayViewArrayList.remove(notyModel);
                            lnlPartialTodayListNoty.removeView(v);

                            NotifyService.getInstance().toolbarPanelLayout.closePanel();
                        }
                        return true;
                    }
                });
                preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) DpiUtil.dipToPx(mContext, 70)));
                itemNotyTodayView.setIconDrawable(notyModel.getDrawableIcon());
                itemNotyTodayView.setTitle(notyModel.getAppName());
                itemNotyTodayView.addView(preview);
                lnlPartialTodayListNoty.addView(itemNotyTodayView);
                mItemNotyTodayViewArrayList.add(itemNotyTodayView);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mContext.unregisterReceiver(mUpdateCurrentTimeReceiver);
        mUpdateCurrentTimeReceiver = null;
    }

    private void updateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,\ndd MMMM");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
//        txvPartialTodayDate.setText(dayOfTheWeek);
    }
}
