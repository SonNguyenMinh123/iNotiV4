package com.notification.notyos10.activitys;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.notification.notyos10.R;
import com.notification.notyos10.constants.Const;
import com.notification.notyos10.customviews.dialogs.ChangeNameNetworkDialog;
import com.notification.notyos10.customviews.dialogs.ShowTempDialog;
import com.notification.notyos10.customviews.widgets.TextViewOSBold;
import com.notification.notyos10.services.NotifyService;
import com.notification.notyos10.utils.NetworkUtil;
import com.notification.notyos10.utils.SharedPreferencesUtil;
import com.notification.notyos10.weather.services.AppLocationService;
import com.sevenheaven.iosswitch.ShSwitchView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import a.a.AdConfig;
import a.a.AdsListener;
import a.a.App;
import a.a.RequestListener;
import cn.pedant.sweetalert.SweetAlertDialog;
import cn.pedant.sweetalert.SweetAlertDialogFullScreen;


public class SettingNotyActivity extends AppCompatActivity implements View.OnClickListener {
    private static String mCity;
    private RelativeLayout rllActivityMainEnableNotify;
    private TextView txvActivitySettingSettings;
    private TextView txvActivityMainSignalStrength;
    private ShSwitchView swvActivityMainSignalStrength;
    private RelativeLayout rllActivitySettingBattery;
    private TextView txvActivityMainBattery;
    private ImageView show_activity_setting__color_statusbar;
    private ShSwitchView swvActivityMainBattery;
    private RelativeLayout rllActivityMainClockFormat, view_activity_setting__background;
    private TextView txvActivityMainClockFormat;
    private ImageView imvActivityMainClockFormat;
    private RelativeLayout rllActivityMainRate;
    private RelativeLayout rllSettingWeather;
    private TextView txvActivityMainClockFormatType;
    private ShSwitchView swvActivityMainCarierName;
    private RelativeLayout rllActivityMainCustomCarierName;
    private ShSwitchView swvActivitySettingStatusbar;
    private ShSwitchView getSwvActivityMainShowWeather;
    private RelativeLayout view_activity_setting__color_statusbar;
    private RelativeLayout rll_activity_setting__privacy_policy;
    private NetworkBroadCast mBroadCast;
    private AppLocationService mAppLocationService;

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        swvActivityMainSignalStrength.setOn(SharedPreferencesUtil.isSignalStrength(SettingNotyActivity.this));
        swvActivityMainBattery.setOn(SharedPreferencesUtil.isShowBattery(SettingNotyActivity.this));
        swvActivityMainCarierName.setOn(SharedPreferencesUtil.isShowCarrierName(SettingNotyActivity.this));

        AdConfig.setAdListener(new AdsListener() {
            @Override
            public void onDismissed(String s) {
                if (s.equals("back_pressed")) {
                    finish();
                }
            }

            @Override
            public void onError(String s, String s1) {
                if (s.equals("back_pressed")) {
                    finish();
                }
            }

            @Override
            public void onLoaded(String s) {

            }
        });
        App.start(this, 2, new RequestListener() {

            @Override
            public void onFinish(int i, String s) {
                if (i == 1) {
                    AdConfig.showBanner(SettingNotyActivity.this, 4);
                }
                AdConfig.loadAndShowAds("start_app", SettingNotyActivity.this);
            }


        });
        AdConfig.setDefaultAds(this, "start_app", "fb", "427898810885947_427898994219262", 1, 1, 0, 0, 0, 0);
        AdConfig.setDefaultAds(this, "background_to_main", "fb", "427898810885947_427898994219262", 1, 1, 0, 0, 0, 0);
        AdConfig.setDefaultAds(this, "back_pressed", "fb", "427898810885947_427898994219262", 1, 1, 0, 0, 0, 0);
        AdConfig.setDefaultAds(this, "default", "admob", "ca-app-pub-1694574612714654/3318143129", 1, 1, 0, 0, 0, 0);

        swvActivitySettingStatusbar.setOn(SharedPreferencesUtil.isShowStatusbar(SettingNotyActivity.this));

    }

    private void getCurrentLocation() {
        if (NetworkUtil.networkIsAvailable(this)) {
            //gps or network
            mAppLocationService = new AppLocationService(this);
            Location gpsLocation = mAppLocationService.getLocation(LocationManager.GPS_PROVIDER);
            Location nwLocation = mAppLocationService.getLocation(LocationManager.NETWORK_PROVIDER);
            if (gpsLocation != null) {
                SharedPreferencesUtil.saveLocation(this, gpsLocation.getLatitude(), gpsLocation.getLongitude());
                getAddress();
            } else if (nwLocation != null) {
                SharedPreferencesUtil.saveLocation(this, nwLocation.getLatitude(), nwLocation.getLongitude());
                getAddress();
            } else {
            }
            mAppLocationService.stopLocation();
        } else {
        }
    }

    private void getAddress() {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            double[] location = SharedPreferencesUtil.getLocation(this);
            addresses = geocoder.getFromLocation(location[0], location[1], 1);
            mCity = addresses.get(0).getAddressLine(1);
            SharedPreferencesUtil.savePreferences(this, Const.CITYLOCATION, mCity);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initData();
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        view_activity_setting__color_statusbar = (RelativeLayout) findViewById(R.id.view_activity_setting__color_statusbar);
        txvActivitySettingSettings = (TextViewOSBold) findViewById(R.id.txv_activity_setting_settings);
        rllActivityMainEnableNotify = (RelativeLayout) findViewById(R.id.rll_activity_setting__enable_notify);
        rll_activity_setting__privacy_policy = (RelativeLayout) findViewById(R.id.rll_activity_setting__privacy_policy);
        txvActivityMainSignalStrength = (TextView) findViewById(R.id.txv_activity_setting__signal_strength);
        swvActivityMainSignalStrength = (ShSwitchView) findViewById(R.id.swv_activity_setting__signal_strength);
        swvActivitySettingStatusbar = (ShSwitchView) findViewById(R.id.swv_activity_setting__statusbar);
        rllActivitySettingBattery = (RelativeLayout) findViewById(R.id.rll_activity_setting_battery);
        txvActivityMainBattery = (TextView) findViewById(R.id.txv_activity_setting__battery);
        swvActivityMainBattery = (ShSwitchView) findViewById(R.id.swv_activity_setting__battery);
        rllActivityMainClockFormat = (RelativeLayout) findViewById(R.id.rll_activity_setting__clock_format);
        txvActivityMainClockFormat = (TextView) findViewById(R.id.txv_activity_setting__clock_format);
        imvActivityMainClockFormat = (ImageView) findViewById(R.id.imv_activity_setting__clock_format);
        rllActivityMainRate = (RelativeLayout) findViewById(R.id.rll_activity_setting__rate);
        txvActivityMainClockFormatType = (TextView) findViewById(R.id.txv_activity_setting__clock_format_type);
        swvActivityMainCarierName = (ShSwitchView) findViewById(R.id.swv_activity_setting__carier_name);
        rllActivityMainCustomCarierName = (RelativeLayout) findViewById(R.id.rll_activity_setting__custom_carier_name);
        rllSettingWeather = (RelativeLayout) findViewById(R.id.rll_activity_setting_weather);
        view_activity_setting__background = (RelativeLayout) findViewById(R.id.view_activity_setting__background);

        view_activity_setting__color_statusbar.setOnClickListener(this);
        rllActivityMainEnableNotify.setOnClickListener(this);
        rllActivityMainClockFormat.setOnClickListener(this);
        rllActivityMainCustomCarierName.setOnClickListener(this);
        rllActivityMainRate.setOnClickListener(this);
        rll_activity_setting__privacy_policy.setOnClickListener(this);
        view_activity_setting__background.setOnClickListener(this);

        swvActivityMainSignalStrength.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                SharedPreferencesUtil.setSignalStrength(SettingNotyActivity.this, isOn);
                NotifyService notifyService = NotifyService.getInstance();
                if (notifyService != null) {
                    notifyService.updateShowSignalStrength();
                }
            }
        });
        swvActivityMainBattery.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                SharedPreferencesUtil.setShowBattery(SettingNotyActivity.this, isOn);
                NotifyService notifyService = NotifyService.getInstance();
                if (notifyService != null) {
                    notifyService.updateShowBattery();
                }
            }
        });
        swvActivityMainCarierName.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                SharedPreferencesUtil.setShowCarrierName(SettingNotyActivity.this, isOn);
                NotifyService notifyService = NotifyService.getInstance();
                if (notifyService != null) {
                    notifyService.updateShowCarrierName();

                }
            }
        });
        swvActivitySettingStatusbar.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
            @Override
            public void onSwitchStateChange(boolean isOn) {
                SharedPreferencesUtil.setShowStatusbar(SettingNotyActivity.this, isOn);
                NotifyService notifyService = NotifyService.getInstance();
                if (notifyService != null) {
                    notifyService.updateStatusBar();
                }
            }
        });
        rllSettingWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NotifyService.getInstance() != null) {
                    Intent intent = new Intent(SettingNotyActivity.this, WeatherActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SettingNotyActivity.this, getResources().getString(R.string.toast_turn_on), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(final View v) {
        if (v == rllActivityMainEnableNotify) {
            Log.e("test_device", " : " + getDeviceName());
            if (getDeviceName().contains("Xiaomi")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    boolean enablePopup = Settings.canDrawOverlays(this);
                    if (!enablePopup) {
                        final SweetAlertDialogFullScreen dialogFullScreen = new SweetAlertDialogFullScreen(this, SweetAlertDialogFullScreen.CUSTOM_IMAGE_TYPE);
                        dialogFullScreen.setTitleText(getResources().getString(R.string.str_noti_author) );
                        dialogFullScreen.setContentText(getResources().getString(R.string.str_noti_content_author));
                        dialogFullScreen.setCustomImage(R.drawable.xiaomi_permission);
                        dialogFullScreen.setCancelText(getString(R.string.str_later));
                        dialogFullScreen.setConfirmText(getString(R.string.yes));
                        dialogFullScreen.showCancelButton(true);
                        dialogFullScreen.setCancelClickListener(new SweetAlertDialogFullScreen.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialogFullScreen sweetAlertDialog) {
                                dialogFullScreen.dismissWithAnimation();
                                return;
                            }
                        });
                        dialogFullScreen.setConfirmClickListener(new SweetAlertDialogFullScreen.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialogFullScreen sweetAlertDialog) {
                                dialogFullScreen.dismissWithAnimation();
                                openPermissionManager();
                            }
                        });
                        dialogFullScreen.show();
                    } else {
                        try {
                            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                        } catch (ActivityNotFoundException e) {
                            try {
                                startActivity(new Intent("android.settings.NOTIFICATION_LISTENER_SETTINGS"));
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                } else {
                    try {
                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    } catch (ActivityNotFoundException e) {
                        try {
                            startActivity(new Intent("android.settings.NOTIFICATION_LISTENER_SETTINGS"));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            } else {
                try {
                    startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                } catch (ActivityNotFoundException e) {
                    try {
                        startActivity(new Intent("android.settings.NOTIFICATION_LISTENER_SETTINGS"));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } else if (v == view_activity_setting__color_statusbar) {
            if (NotifyService.getInstance() != null) {
                if (SharedPreferencesUtil.getColorStatusBar2(SettingNotyActivity.this) == -1) {
                    ColorPickerDialogBuilder
                            .with(v.getContext())
                            .setTitle(getResources().getString(R.string.choose_color))
                            .initialColor(Color.RED)
                            .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                            .density(10)
                            .setOnColorSelectedListener(new OnColorSelectedListener() {
                                @Override
                                public void onColorSelected(int selectedColor) {
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.w_ok), new ColorPickerClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                    NotifyService notifyService = NotifyService.getInstance();
                                    if (notifyService != null) {
                                        SharedPreferencesUtil.setColorStatusBar(SettingNotyActivity.this, "#" + Integer.toHexString(selectedColor));
                                        SharedPreferencesUtil.setColorStatusBar2(SettingNotyActivity.this, selectedColor);
                                        notifyService.updateBackgroudStatusBar();
                                    } else {
                                        Toast.makeText(v.getContext(), SettingNotyActivity.this.getResources().getString(R.string.toast_turn_on), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.w_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .build().show();
                } else {
                    ColorPickerDialogBuilder
                            .with(v.getContext())
                            .setTitle(getResources().getString(R.string.choose_color))
                            .initialColor(SharedPreferencesUtil.getColorStatusBar2(SettingNotyActivity.this))
                            .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                            .density(10)
                            .setOnColorSelectedListener(new OnColorSelectedListener() {
                                @Override
                                public void onColorSelected(int selectedColor) {
                                }
                            })
                            .setPositiveButton(getResources().getString(R.string.w_ok), new ColorPickerClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                    NotifyService notifyService = NotifyService.getInstance();
                                    if (notifyService != null) {
                                        Log.e("test3", "selectedColor : " + selectedColor);
                                        SharedPreferencesUtil.setColorStatusBar(SettingNotyActivity.this, "#" + Integer.toHexString(selectedColor));
                                        SharedPreferencesUtil.setColorStatusBar2(SettingNotyActivity.this, selectedColor);
                                        notifyService.updateBackgroudStatusBar();
                                    } else {
                                        Toast.makeText(v.getContext(), v.getContext().getResources().getString(R.string.toast_turn_on), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.w_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .build().show();
                }
            } else
                Toast.makeText(SettingNotyActivity.this, getResources().getString(R.string.toast_turn_on), Toast.LENGTH_SHORT).show();

        } else if (v == rllActivityMainClockFormat) {
//            Intent intent = new Intent(this, DateFormatActivity.class);
//            startActivity(intent);
            new ShowTempDialog(this).show();
            overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
        } else if (v == rllActivityMainCustomCarierName) {
//            final Dialog dialog = new Dialog(this);
//            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//            lp.copyFrom(dialog.getWindow().getAttributes());
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            DisplayMetrics metrics = getResources().getDisplayMetrics();
//            int screenWidth = (int) (metrics.widthPixels * 0.80);
//            lp.width = screenWidth;
//            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//            dialog.setContentView(R.layout.dialog_carrier_name);
//            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//            final EditText edtDialogCustomCarrierNameInput = (EditText) dialog.findViewById(R.id.edt_dialog_custom_carrier_name__input);
//            ButtonOSNormal btnDialogCustomCarrierNameOk = (ButtonOSNormal) dialog.findViewById(R.id.btn_dialog_custom_carrier_name__ok);
//            btnDialogCustomCarrierNameOk.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    NotifyService notifyService = NotifyService.getInstance();
//                    String carrierName = edtDialogCustomCarrierNameInput.getText().toString();
//                    if (carrierName.equals("")) {
//                        carrierName = " ";
//                    }
//                    SharedPreferencesUtil.setCustomCarrierName(SettingNotyActivity.this, carrierName);
//                    if (notifyService != null) {
//                        notifyService.updateCustomCarrierName();
//                    }
//                    dialog.dismiss();
//                }
//            });
//            ButtonOSNormal btnDialogCustomCarrierNameCancel = (ButtonOSNormal) dialog.findViewById(R.id.btn_dialog_custom_carrier_name__cancel);
//            btnDialogCustomCarrierNameCancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//            ButtonOSNormal btnDialogCustomCarrierNameDefault = (ButtonOSNormal) dialog.findViewById(R.id.btn_dialog_custom_carrier_name__default);
//            btnDialogCustomCarrierNameDefault.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String carrierName = SystemUtil.getCarrierName(SettingNotyActivity.this).toUpperCase();
//                    edtDialogCustomCarrierNameInput.setText(carrierName);
//                }
//            });
//            dialog.show();
//            dialog.getWindow().setAttributes(lp);
            ChangeNameNetworkDialog changeNameNetworkDialog = new ChangeNameNetworkDialog(this);
            changeNameNetworkDialog.show();

        } else if (v == rllActivityMainRate) {
            launchMarket();
        } else if (v == view_activity_setting__background) {
            initBroadcast();
            NotifyService notifyService = NotifyService.getInstance();
            if (notifyService != null) {
                Intent intent = new Intent(SettingNotyActivity.this, SetBackgroundActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
                Log.e("NotifyService_TRUE", "OKE");
            } else {
                Toast.makeText(SettingNotyActivity.this, getResources().getString(R.string.toast_turn_on), Toast.LENGTH_SHORT).show();
                Log.e("NotifyService_FALSE", "OKE");
            }

        } else if (v == rll_activity_setting__privacy_policy) {
            String url = "https://sites.google.com/site/vechumedia/home";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }

    private void initBroadcast() {
        mBroadCast = new NetworkBroadCast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mBroadCast, intentFilter);
    }

    public boolean checkConnected() {
        if (!SetBackgroundActivity.isNetworkConnected(this)) {
            Toast.makeText(this, "No Internet can't load image", Toast.LENGTH_LONG).show();

            return false;
        } else {
            return true;
        }
    }

    private void initData() {
        if (SharedPreferencesUtil.isSignalStrength(this)) {
            swvActivityMainSignalStrength.setOn(true);
        }
        if (SharedPreferencesUtil.isShowBattery(this)) {
            swvActivityMainBattery.setOn(true);
        }
        if (SharedPreferencesUtil.is24hFormat(this)) {
            txvActivityMainClockFormatType.setText(R.string.str_format_24h);

        } else {
            txvActivityMainClockFormatType.setText(R.string.str_format_12h);
        }
    }

    private void openPermissionManager() {
        Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivityForResult(i, 50);
    }

    public void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                return;
            } catch (Exception localException) {

                Toast toast = Toast.makeText(this, "unable to find market app", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu

        return true;
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences("launchMarket", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        int checkFirst = getSharedPreferences("launchMarket", MODE_PRIVATE).getInt("check", 0);
        if (checkFirst == 0) {
            final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
            dialog.setTitleText(getResources().getString(R.string.please_rate));
            dialog.setContentText(getResources().getString(R.string.rate_text));
            dialog.setCustomImage(R.drawable.ic_star);
            dialog.setCancelText(getResources().getString(R.string.later));
            dialog.setConfirmText(getResources().getString(R.string.yes));
            dialog.showCancelButton(true);
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
//                    AdConfig.onBackPress(SettingNotyActivity.this);
//                    finish();
                    dialog.dismiss();
                    AdConfig.loadAndShowAds("back_pressed", SettingNotyActivity.this);
                }
            });
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    launchMarket();
                    editor.putInt("check", 1);
                    editor.commit();
                    dialog.dismiss();
                }
            })
                    .show();
        } else {
//            AdConfig.onBackPress(this);
//            finish();
            AdConfig.loadAndShowAds("back_pressed", SettingNotyActivity.this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        AdConfig.hideBanner(SettingNotyActivity.this);
    }

    private class NetworkBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                NetworkInfo networkInfo =
                        intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected()) {

                }
            } else if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                NetworkInfo networkInfo =
                        intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI && !networkInfo.isConnected()) {
                    checkConnected();

                }
            }
        }
    }
}
