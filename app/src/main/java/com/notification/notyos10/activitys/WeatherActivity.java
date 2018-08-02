package com.notification.notyos10.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.notification.notyos10.R;
import com.notification.notyos10.alarmcontroler.AlarmReceiver;
import com.notification.notyos10.constants.Const;
import com.notification.notyos10.customviews.dialogs.ChooseLocationDialog;
import com.notification.notyos10.customviews.widgets.ButtonIos;
import com.notification.notyos10.customviews.widgets.RadioButtonIos;
import com.notification.notyos10.services.NotifyService;
import com.notification.notyos10.utils.SharedPreferencesUtil;
import com.sevenheaven.iosswitch.ShSwitchView;

import java.util.Calendar;

public class WeatherActivity extends Activity implements OnClickListener {
    private RelativeLayout rllActivityWeatherLocation;
    private RelativeLayout rllActivityWeatherTemp;
    private TextView txvActivityWeatherLocation2;
    private TextView txvActivityWeatherTemp2;
    private RelativeLayout rllActivityWeather;
    private ShSwitchView swcActivityWeather;
    private boolean activedLocation = false;

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (!SharedPreferencesUtil.getpreferences(this, Const.CITYLOCATION).equalsIgnoreCase("0")
                && !SharedPreferencesUtil.getpreferences(this, Const.CITYLOCATION).equalsIgnoreCase("")) {
            txvActivityWeatherLocation2.setText(SharedPreferencesUtil.getpreferences(this, Const.CITYLOCATION));
        } else {
            txvActivityWeatherLocation2.setText("n/a");
        }
        String temp = SharedPreferencesUtil.getpreferences(this, Const.TEMP);
        if (temp.equalsIgnoreCase("0")) {
            txvActivityWeatherTemp2.setText(R.string.temp_f);
        } else {
            txvActivityWeatherTemp2.setText(R.string.temp_c);
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        swcActivityWeather = (ShSwitchView) findViewById(R.id.swc_activity_weather);
        rllActivityWeatherLocation = (RelativeLayout) findViewById(R.id.rll_activity_weather_location);
        rllActivityWeatherTemp = (RelativeLayout) findViewById(R.id.rll_activity_weather_temp);
        txvActivityWeatherLocation2 = (TextView) findViewById(R.id.txv_activity_weather_location2);
        txvActivityWeatherTemp2 = (TextView) findViewById(R.id.txv_activity_weather_temp2);
        rllActivityWeatherLocation.setOnClickListener(this);
        rllActivityWeatherTemp.setOnClickListener(this);

        swcActivityWeather.setOn(SharedPreferencesUtil.isShowWeather(this));
        swcActivityWeather.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                Log.e("test_switch", "vao swietch state change");
                SharedPreferencesUtil.setShowWeather(WeatherActivity.this, isOn);
                NotifyService.getInstance().updateVisibleWeather(isOn);

                if (isOn && !isLocationEnabled(WeatherActivity.this)) {
                    Toast.makeText(WeatherActivity.this, "You need to enable location to receive information of weather", Toast.LENGTH_SHORT).show();
                    swcActivityWeather.setOn(false);
                    SharedPreferencesUtil.setShowWeather(WeatherActivity.this, false);
                    NotifyService.getInstance().updateVisibleWeather(false);
                    onBackPressed();
                }

                if (isOn) {
                    /*
                    bật chức năng hiển thị thời tiết
                    * */
                    if (SharedPreferencesUtil.getpreferences(WeatherActivity.this, Const.CITYLOCATION).equalsIgnoreCase("0") ||
                            SharedPreferencesUtil.getpreferences(WeatherActivity.this, Const.CITYLOCATION).equalsIgnoreCase("")) {
                        /*
                        * nếu location chưa được bật thì tự động tắt tính năng và yêu cầu ng dùng bật lên
                        * */
                        Toast.makeText(WeatherActivity.this, "You should choose location to receive information of weather", Toast.LENGTH_SHORT).show();
                    } else {
                        NotifyService.getInstance().updateWeather();
                    }

                    Calendar mCalendar = Calendar.getInstance();
                    int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
                    int mMinute = mCalendar.get(Calendar.MINUTE);
                    int mYear = mCalendar.get(Calendar.YEAR);
                    int mMonth = mCalendar.get(Calendar.MONTH) + 1;
                    int mDay = mCalendar.get(Calendar.DATE);
                    mCalendar.set(Calendar.MONTH, --mMonth);
                    mCalendar.set(Calendar.YEAR, mYear);
                    mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                    mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                    mCalendar.set(Calendar.MINUTE, mMinute);
                    mCalendar.set(Calendar.SECOND, 0);
                    new AlarmReceiver().setRepeatAlarm(WeatherActivity.this,
                            mCalendar, 100, 60 * 60 * 1000);
                    finish();
                } else {
                    new AlarmReceiver().cancelAlarm(WeatherActivity.this, 100);
                }

            }
        });
    }

    public void chooseCityLocation() {
        txvActivityWeatherLocation2.setText(SharedPreferencesUtil.getCityLocation(this));
        NotifyService.getInstance().updateWeather();
    }

    public void showTemp() {
        final Dialog localDialog = new Dialog(this);
        localDialog.requestWindowFeature(1);
        localDialog.setCanceledOnTouchOutside(false);
        localDialog.setContentView(R.layout.dialog_choose_temp);
        RadioButtonIos rdbDialogTempF = (RadioButtonIos) localDialog.findViewById(R.id.rdb_dialog_temp_f);
        RadioButtonIos rdbDialogTempC = (RadioButtonIos) localDialog.findViewById(R.id.rdb_dialog_temp_c);
        ButtonIos btnDialogTempCancel = (ButtonIos) localDialog.findViewById(R.id.btn_dialog_temp_cancel);

        Resources res = getResources();
        Drawable drawableTempF = res.getDrawable(R.drawable.bg_pressed_color_item_main);
        Drawable drawableTempC = res.getDrawable(R.drawable.bg_pressed_color_item_main);

        rdbDialogTempF.setBackgroundDrawable(drawableTempF);
        rdbDialogTempC.setBackgroundDrawable(drawableTempC);

        String temp = SharedPreferencesUtil.getpreferences(this, Const.TEMP);
        if (temp.equalsIgnoreCase("0")) {
            rdbDialogTempF.setChecked(true);
        } else {
            rdbDialogTempC.setChecked(true);
        }
        localDialog.show();
        btnDialogTempCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View paramView) {
                localDialog.cancel();
            }
        });
        rdbDialogTempF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean) {
                if (paramBoolean) {
                    SharedPreferencesUtil.savePreferences2(WeatherActivity.this, Const.TEMP, "0");
                    SharedPreferencesUtil.setKindTem(WeatherActivity.this, "F");
                    txvActivityWeatherTemp2.setText(R.string.temp_f);
                    if (NotifyService.getInstance() != null) {
                        NotifyService.getInstance().updateWeather();
                    }
                    localDialog.cancel();
                }
            }
        });
        rdbDialogTempC.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean) {
                if (paramBoolean) {
                    SharedPreferencesUtil.savePreferences(WeatherActivity.this, Const.TEMP, "1");
                    SharedPreferencesUtil.setKindTem(WeatherActivity.this, "C");
                    txvActivityWeatherTemp2.setText(R.string.temp_c);
                    if (NotifyService.getInstance() != null) {
                        NotifyService.getInstance().updateWeather();
                    }
                    localDialog.cancel();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == rllActivityWeatherLocation) {
            ChooseLocationDialog chooseLocationDialog = new ChooseLocationDialog(this);
            chooseLocationDialog.show();
            Log.e("test_dialog", "show dialog");
        } else if (v == rllActivityWeatherTemp) {
            showTemp();
        }
    }
}
