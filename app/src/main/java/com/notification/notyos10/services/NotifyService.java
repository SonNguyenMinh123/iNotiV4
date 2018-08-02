package com.notification.notyos10.services;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.WallpaperManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.notification.notyos10.R;
import com.notification.notyos10.adapters.AdapterAppInfo;
import com.notification.notyos10.adapters.AdapterViewpagerMenuSetting;
import com.notification.notyos10.adapters.AdapterViewpagerNoty;
import com.notification.notyos10.customviews.MyViewPager;
import com.notification.notyos10.customviews.others.PartialSignalView;
import com.notification.notyos10.customviews.slide.ToDayLayout;
import com.notification.notyos10.customviews.swipedown.ToolbarPanelLayout;
import com.notification.notyos10.customviews.swipedown.ToolbarPanelListener;
import com.notification.notyos10.objects.AppInfo;
import com.notification.notyos10.objects.NotifyEntity;
import com.notification.notyos10.receivers.BluetoothchangedReceiver;
import com.notification.notyos10.receivers.InforBatteryReceiver;
import com.notification.notyos10.receivers.NetworkChangerReceiver;
import com.notification.notyos10.receivers.SimChangedReceiver;
import com.notification.notyos10.receivers.UpdateCurrentTimeReceiver;
import com.notification.notyos10.receivers.UpdateReceiver;
import com.notification.notyos10.threadsapp.NotificationTask;
import com.notification.notyos10.utils.ConvertUtils;
import com.notification.notyos10.utils.ScreenUtil;
import com.notification.notyos10.utils.SharedPreferencesUtil;
import com.notification.notyos10.utils.SystemUtil;
import com.notification.notyos10.utils.Util;
import com.notification.notyos10.weather.ForecastUtil;
import com.qiushui.blurredview.BlurredView;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zhangyf.vir56k.androidimageblur.BlurUtil;

public class NotifyService extends Service implements ToolbarPanelListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener, View.OnLongClickListener, TextWatcher {

    private final static String TAG = "NotifyService";
    private static NotifyService mNotifyService;
    private static MyViewPager vpgPartialStatusbarService;
    public WindowManager mWindowManager;
    public ToolbarPanelLayout toolbarPanelLayout;
    private LayoutInflater mInflater = null;
    private View mStatusBarView;
    private Context mContext;
    private RelativeLayout rllPartialStatusbarServiceStatusbar;
    private PartialSignalView sgvPartialStatusbarServiceSignalStrength;
    private TextView txvPartialStatusbarServiceCarrierName;
    private ImageView imvPartialStatusbarServiceWifi;
    private ImageView imvPartialStatusbarServiceBluetooth;
    private ImageView imvPartialStatusbarServiceBattery;
    private ImageView imvPartialStatusbarServiceStorm;
    private TextView txvPartialStatusbarServiceCurrentTime;
    private ImageButton btnPartialStatusbarServiceSettings;
    private TextView txvPartialStatusbarServicePercentBattery;
    private TextView txvPartialStatusbarServiceNetworkName;
    private AdapterViewpagerNoty mViewpagerAdapter;
    private ToDayLayout mTodayLayout;
    private ImageView imvPartialStatusbarServiceToolbar;
    private InforBatteryReceiver mInforBatteryReceiver;
    private BluetoothchangedReceiver mBluetoothchangedReceiver;
    private UpdateCurrentTimeReceiver mUpdateCurrentTimeReceiver;
    private NetworkChangerReceiver mNetworkChangingReceiver;
    private SimChangedReceiver mSimChangedReceiver;
    private RelativeLayout rllPartialStatusbarServiceToolbar;
    private LinearLayout search;
    private RelativeLayout rlSearchResult;
    private RecyclerView recyclerView;
    private AdapterAppInfo adapter;
    private List<AppInfo> mList;
    private EditText searchEditText;
    private ImageView clearSearchBox;
    private Button cancel;
    private int realWidth = -1;
    private Util util;

    private Animation animation;
    private Animation animationTranslate;
    private Animation animationTranslateBack;
    private ImageButton btnPartialStatusbarServiceWifi;
    private ImageButton btnPartialStatusbarServiceMobileData;
    private ImageButton btnPartialStatusbarServiceBluetooth;
    private ImageButton btnPartialStatusbarServiceMute;
    private ImageButton btnPartialStatusbarServiceFlashtLight;
    private ImageButton btnPartialStatusbarServiceAutoRotate;
    private ImageView img_partial_notify__background;
    private View viewPartialTodayMenuSetting;
    //	private LinearLayout lnlPartialTodayMenuSetting;
    private HorizontalScrollView lnlPartialTodayMenuSetting;
    private View viewPartialTodayLineToolbar;
    private ArrayList<NotifyEntity> mRightNotyList = new ArrayList<>();
    private ArrayList<NotifyEntity> mLeftNotyList = new ArrayList<>();
    private boolean onclickWifif = false;
    private PageIndicatorView pageIndicatorView;
    private BlurredView blurredView;
    private ViewPager vpgMenuSettings;
    private Handler handler;
    private boolean touch = false;

    public static NotifyService getInstance() {
        return mNotifyService;
    }

    private int getDisplayHeight() {
        return this.getResources().getDisplayMetrics().heightPixels;
    }

    public MyViewPager getViewPager() {
        return vpgPartialStatusbarService;
    }

    public void setPaddingEnable(boolean b) {
        Log.e("test_padding_enable", "vao padding enable: " + b);
        vpgPartialStatusbarService.setPagingEnabled(b);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotifyService = this;
        mContext = this;
        util = new Util(mContext);
        handler = new Handler();
        if (mWindowManager == null) {
            initState();
            initView();
            attachView();
            showBlurBackground();
            initData();
        }
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return NotifyService.START_NOT_STICKY;
    }

    private void initState() {
        if (null == mWindowManager) {
            mWindowManager = ((WindowManager) getSystemService(WINDOW_SERVICE));
        }
    }

    public void updateCurrentPagerDot(int position) {
        pageIndicatorView.setSelection(position);
    }

    private void initView() {

        if (null == mInflater) {
            mInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (null == mStatusBarView) {
            mStatusBarView = mInflater.inflate(R.layout.partial_statusbar_service, null);
            sgvPartialStatusbarServiceSignalStrength = (PartialSignalView) mStatusBarView.findViewById(R.id.sgv_partial_statusbar_service__signal_strength);
            txvPartialStatusbarServiceCarrierName = (TextView) mStatusBarView.findViewById(R.id.txv_partial_statusbar_service__carrier_name);
            imvPartialStatusbarServiceWifi = (ImageView) mStatusBarView.findViewById(R.id.imv_partial_statusbar_service__wifi);
            imvPartialStatusbarServiceBluetooth = (ImageView) mStatusBarView.findViewById(R.id.imv_partial_statusbar_service__bluetooth);
            imvPartialStatusbarServiceBattery = (ImageView) mStatusBarView.findViewById(R.id.imv_partial_statusbar_service__battery);
            imvPartialStatusbarServiceStorm = (ImageView) mStatusBarView.findViewById(R.id.imv_partial_statusbar_service__storm);
            txvPartialStatusbarServicePercentBattery = (TextView) mStatusBarView.findViewById(R.id.txv_partial_statusbar_service__percent_battery);
            txvPartialStatusbarServiceNetworkName = (TextView) mStatusBarView.findViewById(R.id.txv_partial_statusbar_service__network_name);
            toolbarPanelLayout = (ToolbarPanelLayout) mStatusBarView.findViewById(R.id.sliding_down_toolbar_layout);
            rllPartialStatusbarServiceToolbar = (RelativeLayout) mStatusBarView.findViewById(R.id.rll_partial_statusbar_service__toolbar);
            img_partial_notify__background = (ImageView) mStatusBarView.findViewById(R.id.img_partial_notify__background);
            blurredView = (BlurredView) mStatusBarView.findViewById(R.id.blur_view);
            blurredView.setBlurredLevel(80);
            vpgPartialStatusbarService = (MyViewPager) mStatusBarView.findViewById(R.id.vpg_partial_statusbar__service);
            txvPartialStatusbarServiceCurrentTime = (TextView) mStatusBarView.findViewById(R.id.txv_partial_statusbar_service__current_time);
            imvPartialStatusbarServiceToolbar = (ImageView) mStatusBarView.findViewById(R.id.imv_partial_statusbar_service__toolbar);
            pageIndicatorView = (PageIndicatorView) mStatusBarView.findViewById(R.id.pageIndicatorView);
            pageIndicatorView.setCount(2);
            pageIndicatorView.setRadius(3);
            Log.e("test", "khoi tao lai : " + SharedPreferencesUtil.getDotPosition(mContext));
            pageIndicatorView.setSelection(SharedPreferencesUtil.getDotPosition(mContext));
            search = (LinearLayout) mStatusBarView.findViewById(R.id.ll_search);
            rlSearchResult = (RelativeLayout) mStatusBarView.findViewById(R.id.rl_search_result);
            rlSearchResult.setVisibility(View.GONE);
            mList = new ArrayList<>();
            adapter = new AdapterAppInfo(mContext, mList, toolbarPanelLayout);
            recyclerView = (RecyclerView) mStatusBarView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
            recyclerView.setAdapter(adapter);
            cancel = (Button) mStatusBarView.findViewById(R.id.btnCancelSearchBox);
            searchEditText = (EditText) mStatusBarView.findViewById(R.id.search_box);
            clearSearchBox = (ImageView) mStatusBarView.findViewById(R.id.clear_search_box);
            viewPartialTodayLineToolbar = mStatusBarView.findViewById(R.id.view_partial_today__line_toolbar);
            viewPartialTodayMenuSetting = (View) mStatusBarView.findViewById(R.id.view_partial_today__menu_setting);
            rllPartialStatusbarServiceStatusbar = (RelativeLayout) mStatusBarView.findViewById(R.id.rll_partial_statusbar_service__statusbar);
            vpgMenuSettings = (ViewPager) mStatusBarView.findViewById(R.id.vpg_statusbar_service__menu_setting);
            AdapterViewpagerMenuSetting adapterViewpagerMenuSetting = new AdapterViewpagerMenuSetting(mContext);
            adapterViewpagerMenuSetting.listener(new AdapterViewpagerMenuSetting.CommucationMenuSetting() {
                @Override
                public void clickSettingMore() {
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    toolbarPanelLayout.closePanel();
                }

                @Override
                public void clickDataMobile(ImageButton btnDataMobile) {
                    if (SystemUtil.isMobileDataEnable(mContext)) {
                        try {
                            SystemUtil.setMobileDataState(mContext, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            final Class<?> conmanClass = Class.forName(conman.getClass().getName());
                            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                            iConnectivityManagerField.setAccessible(true);
                            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                            final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                            setMobileDataEnabledMethod.setAccessible(true);
                            setMobileDataEnabledMethod.invoke(iConnectivityManager, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                SystemUtil.setMobileDataState(mContext, false);
                            } catch (Exception ex) {
                                Intent intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                                startActivity(intent);
                                toolbarPanelLayout.closePanel();
                            }
                        }
                    } else {
                        try {
                            SystemUtil.setMobileDataState(mContext, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            final Class<?> conmanClass = Class.forName(conman.getClass().getName());
                            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                            iConnectivityManagerField.setAccessible(true);
                            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                            final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                            setMobileDataEnabledMethod.setAccessible(true);
                            setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                SystemUtil.setMobileDataState(mContext, false);
                            } catch (Exception ex) {
                                Intent intent = new Intent();
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                                startActivity(intent);
                                toolbarPanelLayout.closePanel();
                            }
                        }
                    }
                }

                @Override
                public void clickLoccation() {
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    toolbarPanelLayout.closePanel();
                }

                @Override
                public void clickBatterySaver() {
                    Intent intent1 = new Intent(Settings.ACTION_BATTERY_SAVER_SETTINGS);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent1);
                    toolbarPanelLayout.closePanel();
                }

                @Override
                public void clickManagerApp() {
                    Intent intent1 = new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent1);
                    toolbarPanelLayout.closePanel();
                }

                @Override
                public void clickModeAirPlane() {
                    Intent intent1 = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent1);
                    toolbarPanelLayout.closePanel();
                }

            });
            img_partial_notify__background.setAlpha((float) 0.9);
            img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img_partial_notify__background.setBackgroundColor(Color.TRANSPARENT);

            vpgMenuSettings.setAdapter(adapterViewpagerMenuSetting);
            viewPartialTodayLineToolbar.setMinimumHeight(viewPartialTodayLineToolbar.getHeight() + 1);
            viewPartialTodayLineToolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    viewPartialTodayLineToolbar.setLayoutParams(new LinearLayout.LayoutParams(viewPartialTodayLineToolbar.getWidth(), viewPartialTodayLineToolbar.getHeight() + 1));
                    viewPartialTodayLineToolbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
            toolbarPanelLayout.setToolbarPanelListener(this);
            searchEditText.addTextChangedListener(this);
            searchEditText.setOnClickListener(this);
            clearSearchBox.setOnClickListener(this);

            cancel.setOnClickListener(this);

            cancel.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            try {
                                cancel.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                                cancel.setPivotX(cancel.getMeasuredWidth());
                                if (realWidth == -1) {
                                    realWidth = cancel.getMeasuredWidth();
                                    setWidthToCancelButton(0);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        animationTranslate = AnimationUtils.loadAnimation(mContext, R.anim.translate);
        animationTranslateBack = AnimationUtils.loadAnimation(mContext, R.anim.anim_back);
        animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.anim_name_network);
    }

    private void showBlurBackground() {
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        final Drawable wallpaperDrawable = wallpaperManager.getDrawable();
        //img_partial_notify__background.setImageDrawable(wallpaperDrawable);
        Bitmap img1;
        try {
            img1 = ((BitmapDrawable) wallpaperDrawable).getBitmap();
            //缩放并显示
            Bitmap newImg = BlurUtil.doBlur(img1, 20, 10);
            img1.recycle();
            img_partial_notify__background.setBackground(new BitmapDrawable(getResources(), newImg));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void showHideCancelControl(boolean show) {
        if (!show) {
            ValueAnimator anim = ValueAnimator.ofInt(cancel.getMeasuredWidth(),
                    0);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(cancel, View.ALPHA, 1f,
                    0f);
            AnimatorSet animator = new AnimatorSet();
            animator.playTogether(anim, alpha);
            animator.start();
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    setWidthToCancelButton(val);
                }
            });

            animator.start();

        } else {
            if (cancel.getMeasuredWidth() > 0) return;
            ValueAnimator anim = ValueAnimator.ofInt(0, realWidth);
            ObjectAnimator alpha = ObjectAnimator.ofFloat(cancel, View.ALPHA, 0f,
                    1f);
            AnimatorSet animator = new AnimatorSet();
            animator.playTogether(anim, alpha);
            animator.start();
            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    setWidthToCancelButton(val);
                }
            });

            animator.start();
        }
    }

    private void setWidthToCancelButton(int width) {
        ViewGroup.LayoutParams layoutParams = cancel
                .getLayoutParams();
        layoutParams.width = width;
        cancel.setLayoutParams(layoutParams);
    }

    private void showToolBar(boolean show) {
        try {
            if (show) {
                imvPartialStatusbarServiceToolbar.setVisibility(View.VISIBLE);
                rllPartialStatusbarServiceToolbar.setBackgroundResource(android.R.color.transparent);
            } else {
                imvPartialStatusbarServiceToolbar.setVisibility(View.INVISIBLE);
                rllPartialStatusbarServiceToolbar.setBackgroundResource(android.R.color.transparent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void attachView() {
        if (null != mWindowManager && null != mStatusBarView) {
            try {
                mWindowManager.addView(mStatusBarView, getCoverLayoutParams());
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (SecurityException e) {

            }
            mStatusBarView.setFocusableInTouchMode(true);
        }
        hideNavigationBar();

        Log.e("test_restart", "attchView");

    }

    public void updateStatusBarVisibilityControl() {
        WindowManager localWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        Display localDisplay = localWindowManager.getDefaultDisplay();
        Point localPoint = new Point();
        try {
            localDisplay.getSize(localPoint);
            int j = localPoint.y;
            localLayoutParams.gravity = Gravity.AXIS_CLIP;
            localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
            localLayoutParams.gravity = 53;
            localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            localLayoutParams.width = 1;
            localLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            localLayoutParams.format = PixelFormat.TRANSPARENT;
            final View helperWnd = new View(this);
            localWindowManager.addView(helperWnd, localLayoutParams);
            helperWnd.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    try {
                        helperWnd.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                        if (ScreenUtil.getScreenHeight(mContext) == helperWnd.getHeight()) {
                            if (mStatusBarView != null)
                                mStatusBarView.setVisibility(View.GONE);
                            return;
                        }
                        if (mStatusBarView != null)
                            mStatusBarView.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            localWindowManager.removeView(helperWnd);
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private WindowManager.LayoutParams getCoverLayoutParams() {

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                // this is to enable the inotystyle to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = ScreenUtil.getStatusBarHeight(mContext);
        localLayoutParams.format = PixelFormat.TRANSPARENT;
        return localLayoutParams;
    }

    private WindowManager.LayoutParams getSlideLayoutParams() {

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags =
                // this is to enable the inotystyle to recieve touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        // Draws over status bar
                        WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.format = PixelFormat.TRANSPARENT;
        return localLayoutParams;
    }

    public void hideNavigationBar() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mStatusBarView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                int uiOptions = /*View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        |*/ View.SYSTEM_UI_FLAG_FULLSCREEN;
                mStatusBarView.setSystemUiVisibility(uiOptions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showNavigationBar() {
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        mStatusBarView.setSystemUiVisibility(uiOptions);
    }

    private void initData() {
        // register broadcast receiver
        if (mInforBatteryReceiver == null) {
            mInforBatteryReceiver = new InforBatteryReceiver(new InforBatteryReceiver.BatteryReceiverCallback() {
                @Override
                public void onInfoBattery(int idLevelDrawable, int percent, boolean isPlug) {
                    if (isPlug) {
                        imvPartialStatusbarServiceStorm.setVisibility(View.VISIBLE);
                        Animation blinkAnim = AnimationUtils.loadAnimation(NotifyService.this, R.anim.anim_blink);
//                        imvPartialStatusbarServiceStorm.startAnimation(blinkAnim);
                    } else {
                        imvPartialStatusbarServiceStorm.clearAnimation();
                        imvPartialStatusbarServiceStorm.setVisibility(View.INVISIBLE);
                    }
                    txvPartialStatusbarServicePercentBattery.setText(percent + "%");
                    imvPartialStatusbarServiceBattery.setImageResource(idLevelDrawable);
                }
            });
            registerReceiver(mInforBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }
        if (mBluetoothchangedReceiver == null) {
            mBluetoothchangedReceiver = new BluetoothchangedReceiver(new BluetoothchangedReceiver.BluetoothReceiverCallback() {
                @Override
                public void onBluetoothchanged(int idDrawable, boolean isEnable) {
                    updateBluetoothState(idDrawable, isEnable);
                    if (isEnable){
                        imvPartialStatusbarServiceBluetooth.setVisibility(View.VISIBLE);
                    } else {
                        imvPartialStatusbarServiceBluetooth.setVisibility(View.GONE);
                    }
                    Log.e("AAAAAAAAAAAAAAAAA", "OKE....." + isEnable);
                }
            });
            registerReceiver(mBluetoothchangedReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        }
        if (mUpdateCurrentTimeReceiver == null) {
            mUpdateCurrentTimeReceiver = new UpdateCurrentTimeReceiver(new UpdateCurrentTimeReceiver.UpdateCurrentTimeReceiverCallback() {
                @Override
                public void onUpdateCurrentTimeChanged() {
                    updateFormatTime();
                }
            });
            registerReceiver(mUpdateCurrentTimeReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
        }
        if (mNetworkChangingReceiver == null) {
            mNetworkChangingReceiver = new NetworkChangerReceiver(new NetworkChangerReceiver.NetworkChangeReceiverCallback() {
                @Override
                public void onNetworkChangechanged(int sType_Network, int levelWifi) {
                    if (sType_Network == 0 && !onclickWifif) {
                        txvPartialStatusbarServiceNetworkName.setVisibility(View.INVISIBLE);
                        imvPartialStatusbarServiceWifi.setVisibility(View.INVISIBLE);
//                        btnPartialStatusbarServiceWifi.setSelected(false);
//                        btnPartialStatusbarServiceMobileData.setSelected(false);
                        return;
                    } else if (onclickWifif) {
                        onclickWifif = false;
                    }

                    imvPartialStatusbarServiceWifi.setVisibility(View.VISIBLE);
                    txvPartialStatusbarServiceNetworkName.setVisibility(View.INVISIBLE);
//                    btnPartialStatusbarServiceWifi.setSelected(true);
//                    btnPartialStatusbarServiceMobileData.setSelected(false);
                    int level = 0;
                    if (levelWifi > 0 && levelWifi < 20) {
                        level = 0;
                    } else if (levelWifi >= 20 && levelWifi < 50) {
                        level = 1;
                    } else if (levelWifi >= 50 && levelWifi < 75) {
                        level = 2;
                    } else if (levelWifi >= 75) {
                        level = 3;
                    }
                    int id = NotifyService.this.getResources().getIdentifier("ic_wifi_" + level, "drawable", NotifyService.this.getPackageName());
                    if (id > 0) {
                        imvPartialStatusbarServiceWifi.setImageResource(id);

                    }
                }

                @Override
                public void onNetworkChangechanged(int sType_Network, String mobileNetworkName) {
                    txvPartialStatusbarServiceNetworkName.setVisibility(View.VISIBLE);
                    imvPartialStatusbarServiceWifi.setVisibility(View.INVISIBLE);
                    txvPartialStatusbarServiceNetworkName.setText(mobileNetworkName);
//                    btnPartialStatusbarServiceMobileData.setSelected(true);

                }
            });
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
            registerReceiver(mNetworkChangingReceiver, intentFilter);
            updateShowMenuSetting();
        }
        if (mSimChangedReceiver == null) {
            mSimChangedReceiver = new SimChangedReceiver(new SimChangedReceiver.SimChangedReceiverCallback() {
                @Override
                public void onSimChangedChanged(int type, String carrierName) {
                    sgvPartialStatusbarServiceSignalStrength.setType(type);
                    if (carrierName != null && type != 0) {
                        if (!SharedPreferencesUtil.getCustomCarrierName(mContext).equals("")) {
                            txvPartialStatusbarServiceCarrierName.setText(SharedPreferencesUtil.getCustomCarrierName(mContext));
                        } else
                            txvPartialStatusbarServiceCarrierName.setText(carrierName.toUpperCase());
                    }
                }
            });
            registerReceiver(mSimChangedReceiver, new IntentFilter("android.intent.action.SIM_STATE_CHANGED"));
        }

        updateShowSignalStrength();
        updateShowBattery();
        updateStatusBar();updateFormatTime();
        updateBackgroudStatusBar();
        updateBluetoothState(R.drawable.ic_bluetooth_on, SystemUtil.isBluetoothEnble());

        if (!SharedPreferencesUtil.getCustomCarrierName(mContext).equals("")) {
            txvPartialStatusbarServiceCarrierName.setText(SharedPreferencesUtil.getCustomCarrierName(mContext));
        } else
            txvPartialStatusbarServiceCarrierName.setText(SystemUtil.getCarrierName(this).toUpperCase());
        new NotificationTask(this, new NotificationTask.OnNotyLoadListener() {
            @Override
            public void onLoadNoty(ArrayList<NotifyEntity> listNotyLeft, ArrayList<NotifyEntity> listNotyRight) {
                mRightNotyList = listNotyRight;
                mLeftNotyList = listNotyLeft;

                mViewpagerAdapter = new AdapterViewpagerNoty(NotifyService.this, mLeftNotyList, mRightNotyList, vpgPartialStatusbarService);
                vpgPartialStatusbarService.setAdapter(mViewpagerAdapter);
                vpgPartialStatusbarService.setOnPageChangeListener(new MyViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        Log.e("test", "onPageSelected : " + position);
                        pageIndicatorView.setSelection(position);
                        SharedPreferencesUtil.setDotPosition(mContext, position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                mTodayLayout = mViewpagerAdapter.getLayoutToday();

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        UpdateReceiver.sSTOP_REQUEST = false;

        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent serviceIntent = new Intent(UpdateReceiver.IDREQUEST);
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 1, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 10, pi);

        updateBackgroundNotifyFirst();
    }

    public ArrayList<NotifyEntity> getmRightNotyList() {
        return mRightNotyList;
    }

    public void setmRightNotyList(ArrayList<NotifyEntity> arrayList) {
        mRightNotyList = arrayList;
    }

    public void updateVisibleWeather(boolean enable) {
        mTodayLayout.updateVisibleWeather(enable);
    }

    public void updateWeather(ForecastUtil forecastUtil) {
        mTodayLayout.updateWeather(forecastUtil);
    }

    public void updateWeather() {
        mTodayLayout.updateWeather();
    }

    public void updateTimeClock() {
        mTodayLayout.updateTime();
    }

    public void updateGetEvent() {
        new NotificationTask(this, new NotificationTask.OnNotyLoadListener() {
            @Override
            public void onLoadNoty(ArrayList<NotifyEntity> listNotyLeft, ArrayList<NotifyEntity> listNotyRight) {
                mRightNotyList = listNotyRight;
                mLeftNotyList = listNotyLeft;
                mViewpagerAdapter = new AdapterViewpagerNoty(NotifyService.this, mLeftNotyList, mRightNotyList);
                vpgPartialStatusbarService.setAdapter(mViewpagerAdapter);
                vpgPartialStatusbarService.setOnPageChangeListener(new MyViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void updateBackgroundNotifyFirst() {
        String kindBackground = SharedPreferencesUtil.getKindBackground(mContext);
        Log.e("test_background", " kind : " + kindBackground);
        if (kindBackground.equals("background_app_photo")) {
            int levelBllur = SharedPreferencesUtil.getLevelBlurred(mContext);
            String pathFile = SharedPreferencesUtil.getPathBAckground(mContext);
            Log.e("test_background", "pathFile : " + pathFile);
            updateBackgroundNotifyImage(pathFile, levelBllur);
        } else if (kindBackground.equals("background_your_photo")) {
            int levelBllur = SharedPreferencesUtil.getLevelBlurred(mContext);
            String pathFile = SharedPreferencesUtil.getPathBAckground(mContext);
            Log.e("test_background", "pathFile : " + pathFile);
            updateBackgroundNotifyImage(pathFile, levelBllur);
        } else if (kindBackground.equals("default")) {
            updateBackgroundNotifyDefault();
        } else if (kindBackground.equals("color")) {
            int red = SharedPreferencesUtil.getRedColor(mContext);
            int green = SharedPreferencesUtil.getGreenColor(mContext);
            int blue = SharedPreferencesUtil.getBlueColor(mContext);
            int alpha = SharedPreferencesUtil.getAlphaColor(mContext);
            updateBackgroundNotify(Color.rgb(red, green, blue));
        }
    }

    public void updateBackgroudStatusBar() {
        String color = SharedPreferencesUtil.getColorStatusBar(NotifyService.this);
        if (color != "") {
            try {
                rllPartialStatusbarServiceStatusbar.setBackgroundColor(Color.parseColor(color));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateBackgroundNotify(int color) {
        blurredView.setVisibility(View.GONE);
        img_partial_notify__background.setAlpha((float) 0.9);
        img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_partial_notify__background.setBackgroundColor(color);
        Picasso.with(NotifyService.this).load((R.drawable.bgr_trongsuot)).into(img_partial_notify__background);
    }

    public void updateBackgroundNotifyDefault() {
        blurredView.setVisibility(View.GONE);
        img_partial_notify__background.setAlpha((float) 0.9);
        img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_partial_notify__background.setBackgroundColor(Color.TRANSPARENT);
        //   Picasso.with(NotifyService.this).load((R.drawable.bground)).into(img_partial_notify__background);
    }

    public void updateBackgroundNotifyImage(int idDrawable, int levelBlur) {
        blurredView.setVisibility(View.VISIBLE);
        img_partial_notify__background.setAlpha((float) 0.9);
        img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_partial_notify__background.setBackgroundColor(Color.TRANSPARENT);

        Bitmap bmp = ConvertUtils.loadBitmapFromresID(mContext, idDrawable, null);
        if (bmp != null) {
            blurredView.setBlurredImg(bmp);
            blurredView.setBlurredLevel(levelBlur);
        }
    }

    public void updateBackgroundNotifyImage(Uri uri, int levelBlur) {
        blurredView.setVisibility(View.VISIBLE);
        img_partial_notify__background.setAlpha((float) 0.9);
        img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_partial_notify__background.setBackgroundColor(Color.TRANSPARENT);
        Bitmap bmp = ConvertUtils.loadBitmapFromUri(this, uri, null);
        if (bmp != null) {
            blurredView.setBlurredImg(bmp);
            Log.e("test_background", " bmp khac null");
        } else {
            Log.e("test_background", " bmp null");

        }
        blurredView.setBlurredLevel(levelBlur);
    }

    public void updateBackgroundNotifyImage(String pathFile, int levelBlur) {
        Log.e("test_background", "lay lai bitmap da decode");
        byte[] imageAsBytes = Base64.decode(pathFile.getBytes(), 0);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        blurredView.setVisibility(View.VISIBLE);
        img_partial_notify__background.setAlpha((float) 0.9);
        img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_partial_notify__background.setBackgroundColor(Color.TRANSPARENT);
        if (bitmap != null) {
            blurredView.setBlurredImg(bitmap);
            Log.e("test_background", "bitmap k null");
        } else {
            Log.e("test_background", "bitmap bi null......");
        }

        blurredView.setBlurredLevel(levelBlur);
    }

    public void updateBackgroundNotifyImage(Bitmap bitmap, int levelBlur) {
        blurredView.setVisibility(View.VISIBLE);
        img_partial_notify__background.setAlpha((float) 0.9);
        img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
        img_partial_notify__background.setBackgroundColor(Color.TRANSPARENT);
        blurredView.setBlurredImg(bitmap);
        blurredView.setBlurredLevel(levelBlur);
    }

    public void updateStatusBar() {
        if (SharedPreferencesUtil.isShowStatusbar(NotifyService.this)) {
            rllPartialStatusbarServiceStatusbar.setVisibility(View.VISIBLE);
        } else {
            rllPartialStatusbarServiceStatusbar.setVisibility(View.GONE);
        }
    }

    public void updateNotification() {
        Log.d(TAG, "updateNotification: ");
        new NotificationTask(this, new NotificationTask.OnNotyLoadListener() {
            @Override
            public void onLoadNoty(ArrayList<NotifyEntity> listNotyLeft, ArrayList<NotifyEntity> listNotyRight) {
                mLeftNotyList = listNotyLeft;
                mRightNotyList = listNotyRight;
                if (mViewpagerAdapter != null)
                    mViewpagerAdapter.updateNotyAndToday(mLeftNotyList, mRightNotyList);
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void updateShowSignalStrength() {
        if (SharedPreferencesUtil.isSignalStrength(NotifyService.this)) {
            sgvPartialStatusbarServiceSignalStrength.setVisibility(View.VISIBLE);
            sgvPartialStatusbarServiceSignalStrength.startAnimation(animation);
        } else {
            sgvPartialStatusbarServiceSignalStrength.setVisibility(View.GONE);
        }
    }

    public void updateShowMenuSetting() {
//        if (SharedPreferencesUtil.isShoMenuSetting(NotifyService.this)) {
//            viewPartialTodayMenuSetting.setVisibility(View.VISIBLE);
//            lnlPartialTodayMenuSetting.setVisibility(View.VISIBLE);
//        } else {
//            viewPartialTodayMenuSetting.setVisibility(View.GONE);
//            lnlPartialTodayMenuSetting.setVisibility(View.GONE);
//        }
    }

    public void updateShowBattery() {
        if (SharedPreferencesUtil.isShowBattery(NotifyService.this)) {
            txvPartialStatusbarServicePercentBattery.setVisibility(View.VISIBLE);
        } else {
            txvPartialStatusbarServicePercentBattery.setVisibility(View.GONE);
        }
    }

    public void updateFormatTime() {
        if (SharedPreferencesUtil.is24hFormat(this)) {
            Date date = new Date();
            SimpleDateFormat localSimpleDateFormat1 = new SimpleDateFormat("HH:mm");
            txvPartialStatusbarServiceCurrentTime.setText(localSimpleDateFormat1.format(date));
        } else {
            Date date = new Date();
            SimpleDateFormat localSimpleDateFormat1 = new SimpleDateFormat("h:mm");
            SimpleDateFormat localSimpleDateFormat2 = new SimpleDateFormat("a");
            txvPartialStatusbarServiceCurrentTime.setText(localSimpleDateFormat1.format(date) + " " + localSimpleDateFormat2.format(date));
        }
    }

    public void updateShowCarrierName() {
        if (SharedPreferencesUtil.isShowCarrierName(NotifyService.this)) {
            txvPartialStatusbarServiceCarrierName.setVisibility(View.VISIBLE);

            txvPartialStatusbarServiceCarrierName.startAnimation(animation);
            imvPartialStatusbarServiceWifi.startAnimation(animationTranslate);

        } else {
            txvPartialStatusbarServiceCarrierName.setVisibility(View.GONE);
            imvPartialStatusbarServiceWifi.startAnimation(animationTranslateBack);

        }
    }

    public void updateCustomCarrierName() {
        txvPartialStatusbarServiceCarrierName.setText(SharedPreferencesUtil.getCustomCarrierName(mContext));
        txvPartialStatusbarServiceCarrierName.startAnimation(animation);
    }

    private void updateBluetoothState(int idDrawable, boolean isEnable) {
//        if (isEnable) {
//            imvPartialStatusbarServiceBluetooth.setVisibility(View.VISIBLE);
//            imvPartialStatusbarServiceBluetooth.setImageResource(idDrawable);
//            btnPartialStatusbarServiceBluetooth.setSelected(true);
//        } else {
//            imvPartialStatusbarServiceBluetooth.setVisibility(View.INVISIBLE);
//            btnPartialStatusbarServiceBluetooth.setSelected(false);
//        }
    }

    @Override
    public void onPanelSlide(View toolbar, View panelView, float slideOffset, boolean isTouch, long currentTime, float distance, float upX) {
        try {
            mWindowManager.updateViewLayout(mStatusBarView, getSlideLayoutParams());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (touch == false && isTouch) {
            touch = true;
            showToolBar(true);
        }
        if (slideOffset > 0 && slideOffset < 1) {
            img_partial_notify__background.setAlpha(slideOffset);
            imvPartialStatusbarServiceToolbar.setImageResource(R.drawable.ic_bar);
        } else if (slideOffset == 1) {
            imvPartialStatusbarServiceToolbar.setImageResource(R.drawable.ic_bar_up);
            if (!isTouch) {
                Log.e(TAG, currentTime + " _ " + distance + " _ " + ScreenUtil.getScreenHeight(mContext) + " _ " + upX);
                Log.e("test_position", "upX =  " + upX + "  distance : " + distance);
//                startAnimation(panelView, currentTime, distance, upX);
//                startAnimation(toolbar, currentTime, distance, upX);
            }
        } else if (slideOffset == 0 && !isTouch) {
            touch = false;
            imvPartialStatusbarServiceToolbar.setImageResource(R.drawable.ic_bar);
            showToolBar(false);
            mWindowManager.updateViewLayout(mStatusBarView, getCoverLayoutParams());
        }
    }

    @Override
    public void onPanelClose(View toolbar, View panelView, float slideOffset, boolean isTouch, long currentTime, float currentY) {
    }

    @Override
    public void onPanelOpened(View toolbar, View panelView) {
        Log.d(TAG, "test=open");
    }

    @Override
    public void onPanelClosed(View toolbar, View panelView) {
        Log.d(TAG, "test=close");
    }

    public boolean dettachView() {
        try {
            if (null != mWindowManager) {
                mWindowManager.removeView(mStatusBarView);
                mStatusBarView = null;
                mWindowManager = null;
                stopSelf();
                UpdateReceiver.sSTOP_REQUEST = true;
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mInforBatteryReceiver);
        unregisterReceiver(mBluetoothchangedReceiver);
        unregisterReceiver(mNetworkChangingReceiver);
        unregisterReceiver(mSimChangedReceiver);
        unregisterReceiver(mUpdateCurrentTimeReceiver);
        mInforBatteryReceiver = null;
        mBluetoothchangedReceiver = null;
        mNetworkChangingReceiver = null;
        mUpdateCurrentTimeReceiver = null;
        mSimChangedReceiver = null;
        mNotifyService = null;
        UpdateReceiver.sSTOP_REQUEST = true;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
//            if (buttonView == rdbPartialStatusbarServiceToday) {
//                vpgPartialStatusbarService.setCurrentItem(0);
//            } else if (buttonView == rdbPartialStatusbarServiceNoty) {
//                vpgPartialStatusbarService.setCurrentItem(1);
//            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == clearSearchBox) {
            clearSearchBox.setVisibility(View.GONE);
            searchEditText.setText("");
        }
        if (v == cancel) {
            showHideCancelControl(false);
            searchEditText.setText("");
            rlSearchResult.setVisibility(View.GONE);
            vpgPartialStatusbarService.setVisibility(View.VISIBLE);
            ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        }
        if (v == btnPartialStatusbarServiceWifi) {

            if (btnPartialStatusbarServiceWifi.isSelected()) {
                btnPartialStatusbarServiceWifi.setSelected(false);
                SystemUtil.setWifiEnable(this, false);
            } else {
                onclickWifif = true;
                btnPartialStatusbarServiceWifi.setSelected(true);
                SystemUtil.setWifiEnable(this, true);
            }
        } else if (v == btnPartialStatusbarServiceMobileData) {
            if (btnPartialStatusbarServiceMobileData.isSelected()) {
                btnPartialStatusbarServiceMobileData.setSelected(false);
//				mobiledataenable(false);
                try {
                    final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    final Class<?> conmanClass = Class.forName(conman.getClass().getName());
                    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                    iConnectivityManagerField.setAccessible(true);
                    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                    final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                    setMobileDataEnabledMethod.setAccessible(true);
                    setMobileDataEnabledMethod.invoke(iConnectivityManager, false);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        SystemUtil.setMobileDataState(this, false);
                    } catch (Exception ex) {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                        startActivity(intent);
                        toolbarPanelLayout.closePanel();
                        btnPartialStatusbarServiceMobileData.setSelected(true);
                    }
                }

            } else {
                btnPartialStatusbarServiceMobileData.setSelected(true);

                try {
                    final ConnectivityManager conman = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    final Class<?> conmanClass = Class.forName(conman.getClass().getName());
                    final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                    iConnectivityManagerField.setAccessible(true);
                    final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                    final Class<?> iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                    final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
                    setMobileDataEnabledMethod.setAccessible(true);
                    setMobileDataEnabledMethod.invoke(iConnectivityManager, true);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        SystemUtil.setMobileDataState(this, false);
                    } catch (Exception ex) {
                        Intent intent = new Intent();
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
                        startActivity(intent);
                        toolbarPanelLayout.closePanel();
                        btnPartialStatusbarServiceMobileData.setSelected(false);
                    }
                }
            }
        } else if (v == btnPartialStatusbarServiceBluetooth) {
            if (btnPartialStatusbarServiceBluetooth.isSelected()) {
                btnPartialStatusbarServiceBluetooth.setSelected(false);
                SystemUtil.setBluetooth(false);
            } else {
                btnPartialStatusbarServiceBluetooth.setSelected(true);
                SystemUtil.setBluetooth(true);
            }
        } else if (v == btnPartialStatusbarServiceMute) {
            if (btnPartialStatusbarServiceMute.isSelected()) {
                btnPartialStatusbarServiceMute.setSelected(false);
                SystemUtil.setSilentEnable(this, false);
            } else {
                btnPartialStatusbarServiceMute.setSelected(true);
                SystemUtil.setSilentEnable(this, true);
            }
        } else if (v == btnPartialStatusbarServiceFlashtLight) {
            if (btnPartialStatusbarServiceFlashtLight.isSelected()) {
                btnPartialStatusbarServiceFlashtLight.setSelected(false);
                SystemUtil.setEnableFlashLight(false);
            } else {
                btnPartialStatusbarServiceFlashtLight.setSelected(true);
                SystemUtil.setEnableFlashLight(true);
            }
        } else if (v == btnPartialStatusbarServiceAutoRotate) {
            if (btnPartialStatusbarServiceAutoRotate.isSelected()) {
                btnPartialStatusbarServiceAutoRotate.setSelected(false);
                SystemUtil.setAutoOrientationEnabled(this, false);
            } else {
                btnPartialStatusbarServiceAutoRotate.setSelected(true);
                SystemUtil.setAutoOrientationEnabled(this, true);
            }
        } else if (v == btnPartialStatusbarServiceSettings) {
            Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            toolbarPanelLayout.closePanel();
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v == btnPartialStatusbarServiceWifi) {
            Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            toolbarPanelLayout.closePanel();
        } else if (v == btnPartialStatusbarServiceMobileData) {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
            startActivity(intent);
            toolbarPanelLayout.closePanel();
        } else if (v == btnPartialStatusbarServiceBluetooth) {
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.bluetooth.BluetoothSettings");
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            toolbarPanelLayout.closePanel();
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (searchEditText.getText().toString().equals("")) {
            adapter.clear();
            clearSearchBox.setVisibility(View.GONE);
        } else {
            vpgPartialStatusbarService.setVisibility(View.INVISIBLE);
            adapter.clear();
            rlSearchResult.setVisibility(View.VISIBLE);
            clearSearchBox.setVisibility(View.VISIBLE);
            showHideCancelControl(true);
            List<AppInfo> list = util.result(searchEditText.getText().toString());
            for (AppInfo appInfo : list) {
                Log.e("test1", "add : " + appInfo.getAppname());
                adapter.add(appInfo);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
