package com.notification.notyos10.customviews.slide;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Message;
import android.os.Process;
import android.provider.CalendarContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.notification.notyos10.R;
import com.notification.notyos10.alarmcontroler.AlarmReceiver;
import com.notification.notyos10.constants.Const;
import com.notification.notyos10.customviews.widgets.TextViewOSNormal;
import com.notification.notyos10.objects.EventEntity;
import com.notification.notyos10.objects.NotifyEntity;
import com.notification.notyos10.receivers.UpdateCurrentTimeReceiver;
import com.notification.notyos10.services.NotificationMonitorService;
import com.notification.notyos10.services.NotifyService;
import com.notification.notyos10.threadsapp.CalendarEventTask;
import com.notification.notyos10.utils.DpiUtil;
import com.notification.notyos10.customviews.others.ItemNotyTodayView;
import com.notification.notyos10.utils.NetworkUtil;
import com.notification.notyos10.utils.SharedPreferencesUtil;
import com.notification.notyos10.weather.ForecastUtil;
import com.notification.notyos10.weather.objects.CurrentWeather;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;
import static com.notification.notyos10.constants.Const.mFIOCurrentWeather;


public class ToDayLayout extends RelativeLayout {
    private static Context mContext;
    private static ViewGroup mViewGroup;
    private UpdateCurrentTimeReceiver mUpdateCurrentTimeReceiver;
    private TextView txvPartialTodayEvent;
    private TextView txvPartialTodayEventTomorow;
    private ArrayList<NotifyEntity> mNotyModelLeft;
    private LinearLayout lnlPartialTodayListNoty;
    private LinearLayout lnlPartialTodayInforEventContinue;
    private LinearLayout lnlPartialTodayWeatherParent;
    private ArrayList<ItemNotyTodayView> mItemNotyTodayViewArrayList;

    private ListView lvTodayEvents;
    private LinearLayout llPartialTodayEventCalendar;
    private ListView lvTomorrowEvents;
    private TextView txvDate;
    private TextViewOSNormal txvInforDate;
    private TextView txvEventNextTitle;
    private TextView txvEventNextTime;
    private TextView txvEventNextContent;
    private TextView txvPartialTodayNoEventContinue;
    private TextView txvPartialTodayPlaceWether;
    private TextView txvPartialTodayWeatherTemp;
    private TextView txvPartialTodayWeatherDes;
    private TextView txvPartialTodayWeatherHumility;
    private TextView txvPartialTodayWeatherPrecip;
    private ImageView imvPartialTodayWeatherIcon;

    public ToDayLayout(Context context) {
        super(context);
        mContext = context;
    }

    public ToDayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public static ToDayLayout fromXml(Context context, ViewGroup viewGroup) {
        ToDayLayout layout = (ToDayLayout) LayoutInflater.from(context)
                .inflate(R.layout.slide_layout_today, viewGroup, false);
        mViewGroup = viewGroup;
        return layout;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        lnlPartialTodayListNoty = (LinearLayout) findViewById(R.id.lnl_partial_today__list_noty);
        lnlPartialTodayInforEventContinue = (LinearLayout) findViewById(R.id.ll_partial_today_infor_event_continue);
        lnlPartialTodayWeatherParent = (LinearLayout) findViewById(R.id.ll_partial_today__weather_parent);
        txvInforDate = (TextViewOSNormal) findViewById(R.id.txv_infor_date);
        txvEventNextTitle = (TextView) findViewById(R.id.txv_partial_today__event_next_title);
        txvEventNextTime = (TextView) findViewById(R.id.txv_partial_today__event_next_time);
        txvEventNextContent = (TextView) findViewById(R.id.txv_partial_today__event_next_content);
        txvPartialTodayPlaceWether = (TextView) findViewById(R.id.txv_partial_today_place_weather);
        txvPartialTodayWeatherTemp = (TextView) findViewById(R.id.txv_partial_today_weather_template);
        txvPartialTodayWeatherDes = (TextView) findViewById(R.id.txv_partial_today_weather_des);
        txvPartialTodayWeatherHumility = (TextView) findViewById(R.id.txv_partial_today_weather_humility);
        txvPartialTodayWeatherPrecip = (TextView) findViewById(R.id.txv_partial_today_weather_precip);
        txvPartialTodayNoEventContinue = (TextView) findViewById(R.id.txv_partial_today__no_event_continue);
        imvPartialTodayWeatherIcon = (ImageView) findViewById(R.id.imv_partial_today_weather_icon);
        txvPartialTodayNoEventContinue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                long startMillis = System.currentTimeMillis();
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, startMillis);
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                NotifyService.getInstance().toolbarPanelLayout.closePanel();
            }
        });

    }

    public void updateVisibleWeather(boolean enable) {
        if (enable) {
            lnlPartialTodayWeatherParent.setVisibility(VISIBLE);
        } else {
            lnlPartialTodayWeatherParent.setVisibility(GONE);
        }
    }

    public void openLayout(ArrayList<NotifyEntity> listNotyLeft) {
        Log.e("test_run","openLayout todaypartial");
        mNotyModelLeft = listNotyLeft;
        mViewGroup.addView(this);
        initData();
        mMainHandler = new SendMassgeHandler();
        requestFocus();
        requestLayout();
        Calendar mCalendar = Calendar.getInstance();
        int mYear = mCalendar.get(Calendar.YEAR);
        int mMonth = mCalendar.get(Calendar.MONTH) + 1;
        int mDay = mCalendar.get(Calendar.DATE);
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay + 1);
        mCalendar.set(Calendar.HOUR_OF_DAY, 0);
        mCalendar.set(Calendar.MINUTE, 0);
        mCalendar.set(Calendar.SECOND, 0);
        new AlarmReceiver().setRepeatAlarm(mContext,mCalendar,101, 24 * 60 * 60 * 100);

    }

    public void updateNoty(ArrayList<NotifyEntity> listNotyLeft) {
        mNotyModelLeft.clear();
        mNotyModelLeft = listNotyLeft;
        lnlPartialTodayListNoty.removeAllViews();
        mItemNotyTodayViewArrayList.clear();
        loadRemoveView();
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
        new CalendarEventTask(mContext, new CalendarEventTask.OnCalendarEventListener() {
            @Override
            public void onTodayEvent(final ArrayList<EventEntity> arrEventTodays) {
                Log.e("test_event", "xong thread");
                if (arrEventTodays.size() != 0) {
                    lnlPartialTodayInforEventContinue.setVisibility(VISIBLE);
                    txvPartialTodayNoEventContinue.setVisibility(INVISIBLE);
                    txvEventNextTitle.setText(arrEventTodays.get(0).getNameOfEvent());
                    SimpleDateFormat sdf1 = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                    Date resultdateStart = new Date(arrEventTodays.get(0).getStartEvent());
                    Date resultdateEnd = new Date(arrEventTodays.get(0).getEndEvent());
                    Log.e("test_format_time"," resulte date1 : " + sdf1.format(resultdateStart));
                    String hmsStart = sdf1.format(resultdateStart);
                    String hmsEnd = sdf1.format(resultdateEnd);
                    txvEventNextTime.setText(hmsStart + " - " + hmsEnd);
                    txvEventNextContent.setText(arrEventTodays.get(0).getDescriptions());

                    lnlPartialTodayInforEventContinue.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            long startMillis = arrEventTodays.get(0).getStartEvent();
                            Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                            builder.appendPath("time");
                            ContentUris.appendId(builder, startMillis);
                            Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                            NotifyService.getInstance().toolbarPanelLayout.closePanel();
                        }
                    });

                } else {
                    lnlPartialTodayInforEventContinue.setVisibility(INVISIBLE);
                    txvPartialTodayNoEventContinue.setVisibility(VISIBLE);
                }

            }

            @Override
            public void onTomorrowEvent(final ArrayList<EventEntity> arrEventTomorrows) {
            }

        }).execute();
        updateTime();
        loadRemoveView();

        Log.e("test_weather_show", " : " + SharedPreferencesUtil.isShowWeather(mContext));
        if (SharedPreferencesUtil.isShowWeather(mContext)) {
            lnlPartialTodayWeatherParent.setVisibility(VISIBLE);
            String mCity = SharedPreferencesUtil.getCurrentCity(mContext);
            txvPartialTodayPlaceWether.setText(mCity);
            updateWeather();
        } else {
            lnlPartialTodayWeatherParent.setVisibility(GONE);
        }
    }

    private class SendMassgeHandler extends android.os.Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            updateDisplay(mFIOCurrentWeather);
        }
    }

    private void updateDisplay(CurrentWeather mCurrentWeather) {
        txvPartialTodayPlaceWether.setText(SharedPreferencesUtil.getCityLocation(mContext));
        if (SharedPreferencesUtil.getKindTem(mContext).equalsIgnoreCase("F")){
            txvPartialTodayWeatherTemp.setText(((mCurrentWeather.getTemperature())) + mContext.getResources().getString(R.string.temp_f_show));
        }else{
            txvPartialTodayWeatherTemp.setText((int) ((mCurrentWeather.getTemperature() - 32) / 1.8) + mContext.getResources().getString(R.string.temp_c_show));
        }
        txvPartialTodayWeatherDes.setText(mCurrentWeather.getSummary());
        txvPartialTodayWeatherHumility.setText("Humidity : " + mCurrentWeather.getHumidity());
        txvPartialTodayWeatherPrecip.setText("Precip : " + mCurrentWeather.getPrecip());
        imvPartialTodayWeatherIcon.setImageResource(mCurrentWeather.getIconId());
    }

    public static SendMassgeHandler mMainHandler = null;
    private ForecastUtil mForecastUtil;

    public void updateWeather() {
        mForecastUtil = new ForecastUtil();
        double[] location = SharedPreferencesUtil.getLocation(mContext);
        final String forecastUrl = mForecastUtil.getForecastURL(location[0], location[1]);
        if (NetworkUtil.networkIsAvailable(mContext)) {
            Log.e("weather", forecastUrl);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(forecastUrl)
                    .build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.e("weather", jsonData);
                        if (response.isSuccessful()) {
                            Const.weatherSuccess = true;
                            mForecastUtil.setCurrentWeatherData(jsonData);
                            mForecastUtil.setFutureWeatherData(jsonData);
                            mFIOCurrentWeather = mForecastUtil.getCurrentWeather();
                            Log.e("test_weather", " nhiệt độ : " + mFIOCurrentWeather.getTemperature());
                            Log.e("test_weather", "summary : " + mFIOCurrentWeather.getSummary());
                            Log.e("test_weather", "humidity : " + mFIOCurrentWeather.getHumidity());
                            Log.e("test_weather", "iconurl : " + mFIOCurrentWeather.getIconUrl());
                            Log.e("test_weather", "precip : " + mFIOCurrentWeather.getPrecip());
                            Log.e("test_weather", "icon : " + mFIOCurrentWeather.getIcon());
                            Log.e("test_weather", "iconId : " + mFIOCurrentWeather.getIconId());
                            mMainHandler.sendEmptyMessage(0);
                        } else {
                        }
                    } catch (IOException e) {
                        Log.e("weather", "Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e("weather", "Exception caught: ", e);
                    }
                }
            });
        } else {

        }
    }



    public void updateWeather(ForecastUtil mForecastUtil) {
        String mCity = SharedPreferencesUtil.getCurrentCity(mContext);
        CurrentWeather currentWeather = mForecastUtil.getCurrentWeather();
        txvPartialTodayPlaceWether.setText(mCity);
        txvPartialTodayWeatherTemp.setText((int) ((currentWeather.getTemperature() - 32) / 1.8) + "");
        txvPartialTodayWeatherDes.setText(currentWeather.getSummary());
        txvPartialTodayWeatherHumility.setText("Humidity : " + currentWeather.getHumidity());
        txvPartialTodayWeatherPrecip.setText("Precip : " + currentWeather.getPrecip());
        imvPartialTodayWeatherIcon.setImageResource(currentWeather.getIconId());

    }


    public void amKillProcess(String process) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo runningProcess : runningProcesses) {
            Log.e("test_kill_service", "process : " + process + "----- running :" + runningProcess.processName);
            if (runningProcess.processName.equals(process)) {
                android.os.Process.sendSignal(runningProcess.pid, android.os.Process.SIGNAL_KILL);
            }
        }
    }

    public void KillApplication(String KillPackage) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(startMain);
        am.killBackgroundProcesses(KillPackage);
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
                                        Log.e("test_noti_app", "action up noti other app");
                                        try {
                                            if (notyModel.getPendingIntent() != null) {
                                                notyModel.getPendingIntent().send();
                                            }
                                        } catch (PendingIntent.CanceledException e) {
                                            AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                                            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), notyModel.getPendingIntent());
                                        }
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
                final ItemNotyTodayView itemNotyTodayView = new ItemNotyTodayView(mContext);
                itemNotyTodayView.listenerNotyTodayView(new ItemNotyTodayView.CommucationNotyTodayView() {
                    @Override
                    public void clickClose() {
                        Log.e("test_noty_custom", "click close noty today view");
                        try {
                            lnlPartialTodayListNoty.removeView(itemNotyTodayView);
                            ActivityManager am = (ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE);
                            List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(50);

                            for (int i = 0; i < rs.size(); i++) {
                                ActivityManager.RunningServiceInfo
                                        rsi = rs.get(i);
                                Log.e("test_noty_custom", "Process " + rsi.process + " with component " + rsi.service.getClassName());
                                amKillProcess(notyModel.getPackageName());
                                ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
                                activityManager.killBackgroundProcesses(notyModel.getPackageName());
                                Log.e("test_noty_custom", " : " + notyModel.getPackageName());
                                KillApplication(notyModel.getPackageName());
                                if (notyModel.getPackageName().equals(rsi.process)) {
                                    Process.killProcess(rsi.pid);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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
                Log.e("test_noti_app", " : " + notyModel.getAppName());
                itemNotyTodayView.setTitle(notyModel.getAppName());
                itemNotyTodayView.setTitleColor();
                itemNotyTodayView.addView(preview);
                Log.e("test_noty_app", "addview to pager");

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

    public void updateTime() {
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        txvInforDate.setText(date);
    }
}
