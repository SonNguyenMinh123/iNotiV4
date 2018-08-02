package com.notification.inotythemehhhhh.myservices;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.notification.inotythemehhhhh.R;
import com.notification.inotythemehhhhh.animations.MyBounceInterpolator;
import com.notification.inotythemehhhhh.customviews.others.PartialSignalView;
import com.notification.inotythemehhhhh.customviews.swipedown.ToolbarPanelLayout;
import com.notification.inotythemehhhhh.customviews.swipedown.ToolbarPanelListener;
import com.notification.inotythemehhhhh.myadapters.AdapterAppInfo;
import com.notification.inotythemehhhhh.myadapters.AdapterViewpagerNoty;
import com.notification.inotythemehhhhh.mycenterutils.ScreenUtil;
import com.notification.inotythemehhhhh.mycenterutils.SharedPreferencesUtil;
import com.notification.inotythemehhhhh.mycenterutils.SystemUtil;
import com.notification.inotythemehhhhh.mycenterutils.Util;
import com.notification.inotythemehhhhh.mycenterutils.logs.LogUtil;
import com.notification.inotythemehhhhh.objects.AppInfo;
import com.notification.inotythemehhhhh.objects.NotifyEntity;
import com.notification.inotythemehhhhh.receiverapp.BluetoothchangedReceiver;
import com.notification.inotythemehhhhh.receiverapp.DelayRequestUpdateReceiver;
import com.notification.inotythemehhhhh.receiverapp.InforBatteryReceiver;
import com.notification.inotythemehhhhh.receiverapp.NetworkChangingReceiver;
import com.notification.inotythemehhhhh.receiverapp.SimChangedReceiver;
import com.notification.inotythemehhhhh.receiverapp.UpdateCurrentTimeReceiver;
import com.notification.inotythemehhhhh.threadsapp.LoadNotificationTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NotifyService extends Service implements ToolbarPanelListener, CompoundButton.OnCheckedChangeListener, View.OnClickListener, View.OnLongClickListener, TextWatcher {

	private final static String TAG = "NotifyService";
	public WindowManager mWindowManager;
	private LayoutInflater mInflater = null;
	private View mStatusBarView;
	private static NotifyService mNotifyService;
	private Context mContext;
	private RelativeLayout rllPartialStatusbarServiceStatusbar;
	private PartialSignalView sgvPartialStatusbarServiceSignalStrength;
	private TextView txvPartialStatusbarServiceCarrierName;
	private ImageView imvPartialStatusbarServiceWifi;
	private ImageView imvPartialStatusbarServiceBluetooth;
	private ImageView imvPartialStatusbarServiceBattery;
	private ImageView imvPartialStatusbarServiceStorm;
	private TextView txvPartialStatusbarServiceCurrentTime;
	public ToolbarPanelLayout toolbarPanelLayout;
	private ViewPager vpgPartialStatusbarService;
	private TextView txvPartialStatusbarServicePercentBattery;
	private TextView txvPartialStatusbarServiceNetworkName;
	private AdapterViewpagerNoty mViewpagerAdapter;
	private ImageView imvPartialStatusbarServiceToolbar;
	private InforBatteryReceiver mInforBatteryReceiver;
	private BluetoothchangedReceiver mBluetoothchangedReceiver;
	private UpdateCurrentTimeReceiver mUpdateCurrentTimeReceiver;
	private NetworkChangingReceiver mNetworkChangingReceiver;
	private SimChangedReceiver mSimChangedReceiver;
	private RelativeLayout rllPartialStatusbarServiceToolbar;
	private RelativeLayout search;
	private RecyclerView recyclerView;
	private AdapterAppInfo adapter;
	private List<AppInfo> mList;
	//    private RadioButton rdbPartialStatusbarServiceToday;
//    private RadioButton rdbPartialStatusbarServiceNoty;
	EditText searchEditText;
	ImageView clearSearchBox;
	Button cancel;
	int realWidth = -1;
	Util util;

	private ImageButton btnPartialStatusbarServiceWifi;
	private ImageButton btnPartialStatusbarServiceMobileData;
	private ImageButton btnPartialStatusbarServiceBluetooth;
	private ImageButton btnPartialStatusbarServiceMute;
	private ImageButton btnPartialStatusbarServiceFlashtLight;
	private ImageButton btnPartialStatusbarServiceAutoRotate;
	private ImageView img_partial_notify__background;
	private View viewPartialTodayMenuSetting;
	private LinearLayout lnlPartialTodayMenuSetting;
	private View viewPartialTodayLineToolbar;
	private ArrayList<NotifyEntity> mRightNotyList = new ArrayList<>();
	private ArrayList<NotifyEntity> mLeftNotyList = new ArrayList<>();
	private boolean onclickWifif = false;

	private int getDisplayHeight() {
		return this.getResources().getDisplayMetrics().heightPixels;
	}

	public static NotifyService getInstance() {
		return mNotifyService;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mNotifyService = this;
		mContext = this;
		util = new Util(mContext);

		if (mWindowManager == null) {
			initState();
			initView();
			attachView();
			initData();
			updateMenubar();
		}
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

			vpgPartialStatusbarService = (ViewPager) mStatusBarView.findViewById(R.id.vpg_partial_statusbar__service);
			txvPartialStatusbarServiceCurrentTime = (TextView) mStatusBarView.findViewById(R.id.txv_partial_statusbar_service__current_time);
			imvPartialStatusbarServiceToolbar = (ImageView) mStatusBarView.findViewById(R.id.imv_partial_statusbar_service__toolbar);
//            rdbPartialStatusbarServiceToday = (RadioButton) mStatusBarView.findViewById(R.id.rdb_partial_statusbar_service__today);
//            rdbPartialStatusbarServiceNoty = (RadioButton) mStatusBarView.findViewById(R.id.rdb_partial_statusbar_service__noty);
			search = (RelativeLayout) mStatusBarView.findViewById(R.id.search);
			search.setVisibility(View.GONE);
			mList = util.result("goo");
			adapter = new AdapterAppInfo(mContext, mList, toolbarPanelLayout);
			recyclerView = (RecyclerView) mStatusBarView.findViewById(R.id.recyclerView);
			recyclerView.setLayoutManager(new GridLayoutManager(mContext, 6));
			recyclerView.setAdapter(adapter);

			cancel = (Button) mStatusBarView.findViewById(R.id.cancel);
			searchEditText = (EditText) mStatusBarView.findViewById(R.id.search_box);
			clearSearchBox = (ImageView) mStatusBarView.findViewById(R.id.clear_search_box);

			viewPartialTodayLineToolbar = mStatusBarView.findViewById(R.id.view_partial_today__line_toolbar);
			viewPartialTodayMenuSetting = (View) mStatusBarView.findViewById(R.id.view_partial_today__menu_setting);
			lnlPartialTodayMenuSetting = (LinearLayout) mStatusBarView.findViewById(R.id.lnl_partial_today__menu_setting);
			btnPartialStatusbarServiceWifi = (ImageButton) mStatusBarView.findViewById(R.id.btn_partial_statusbar_service__wifi);
			btnPartialStatusbarServiceMobileData = (ImageButton) mStatusBarView.findViewById(R.id.btn_partial_statusbar_service__mobile_data);
			btnPartialStatusbarServiceBluetooth = (ImageButton) mStatusBarView.findViewById(R.id.btn_partial_statusbar_service__bluetooth);
			btnPartialStatusbarServiceMute = (ImageButton) mStatusBarView.findViewById(R.id.btn_partial_statusbar_service__mute);
			btnPartialStatusbarServiceFlashtLight = (ImageButton) mStatusBarView.findViewById(R.id.btn_partial_statusbar_service__flasht_light);
			btnPartialStatusbarServiceAutoRotate = (ImageButton) mStatusBarView.findViewById(R.id.btn_partial_statusbar_service__auto_rotate);
			rllPartialStatusbarServiceStatusbar = (RelativeLayout) mStatusBarView.findViewById(R.id.rll_partial_statusbar_service__statusbar);

			viewPartialTodayLineToolbar.setMinimumHeight(viewPartialTodayLineToolbar.getHeight() + 1);
			viewPartialTodayLineToolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					viewPartialTodayLineToolbar.setLayoutParams(new LinearLayout.LayoutParams(viewPartialTodayLineToolbar.getWidth(), viewPartialTodayLineToolbar.getHeight() + 1));
					viewPartialTodayLineToolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			});
			toolbarPanelLayout.setToolbarPanelListener(this);
//            rdbPartialStatusbarServiceToday.setOnCheckedChangeListener(this);
//            rdbPartialStatusbarServiceNoty.setOnCheckedChangeListener(this);
			searchEditText.addTextChangedListener(this);
			searchEditText.setOnClickListener(this);
			clearSearchBox.setOnClickListener(this);

			cancel.setOnClickListener(this);

			cancel.getViewTreeObserver().addOnGlobalLayoutListener(
					new ViewTreeObserver.OnGlobalLayoutListener() {
						@Override
						public void onGlobalLayout() {
							cancel.setPivotX(cancel.getMeasuredWidth());
							if (realWidth == -1) {
								realWidth = cancel.getMeasuredWidth();
								setWidthToCancelButton(0);
							}
						}
					});

			btnPartialStatusbarServiceWifi.setOnClickListener(this);
			btnPartialStatusbarServiceMobileData.setOnClickListener(this);
			btnPartialStatusbarServiceBluetooth.setOnClickListener(this);
			btnPartialStatusbarServiceMute.setOnClickListener(this);
			btnPartialStatusbarServiceFlashtLight.setOnClickListener(this);
			btnPartialStatusbarServiceAutoRotate.setOnClickListener(this);

			btnPartialStatusbarServiceWifi.setOnLongClickListener(this);
			btnPartialStatusbarServiceMobileData.setOnLongClickListener(this);
			btnPartialStatusbarServiceBluetooth.setOnLongClickListener(this);

			vpgPartialStatusbarService.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
				@Override
				public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

				}

				@Override
				public void onPageSelected(int position) {
					if (position == 0) {
//                        rdbPartialStatusbarServiceToday.setChecked(true);
					} else if (position == 1) {
//                        rdbPartialStatusbarServiceNoty.setChecked(true);
					}
				}

				@Override
				public void onPageScrollStateChanged(int state) {

				}
			});
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
		if (show) {
			imvPartialStatusbarServiceToolbar.setVisibility(View.VISIBLE);
			rllPartialStatusbarServiceToolbar.setBackgroundResource(R.drawable.ibg_toolbar);
		} else {
			imvPartialStatusbarServiceToolbar.setVisibility(View.INVISIBLE);
			rllPartialStatusbarServiceToolbar.setBackgroundResource(android.R.color.transparent);
		}
	}

	private void attachView() {
		if (null != mWindowManager && null != mStatusBarView) {
			try {
				mWindowManager.addView(mStatusBarView, getCoverLayoutParams());
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			mStatusBarView.setFocusableInTouchMode(true);
		}
		hideNavigationBar();
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
					if (ScreenUtil.getScreenHeight(mContext) == helperWnd.getHeight()) {
						mStatusBarView.setVisibility(View.GONE);
						return;
					}
					mStatusBarView.setVisibility(View.VISIBLE);
				}
			});
			localWindowManager.removeView(helperWnd);
		} catch (NoSuchMethodError e) {
			e.printStackTrace();
		}
	}


//	public void updateStatusBarColor(int color) {
//		rllPartialStatusbarServiceStatusbar.setBackgroundColor(color);
//	}

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
//        int softKeyHeight = 0;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            softKeyHeight = ScreenUtil.hasSoftKeys(this, mWindowManager);
//        }
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

	public void deleteNotificaiton(NotifyEntity notyModel) {

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
						imvPartialStatusbarServiceStorm.startAnimation(blinkAnim);
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
			mNetworkChangingReceiver = new NetworkChangingReceiver(new NetworkChangingReceiver.NetworkChangeReceiverCallback() {
				@Override
				public void onNetworkChangechanged(int sType_Network, int levelWifi) {
					if (sType_Network == 0 && !onclickWifif) {
						txvPartialStatusbarServiceNetworkName.setVisibility(View.INVISIBLE);
						imvPartialStatusbarServiceWifi.setVisibility(View.INVISIBLE);
						btnPartialStatusbarServiceWifi.setSelected(false);
						btnPartialStatusbarServiceMobileData.setSelected(false);
						return;
					} else if (onclickWifif) {
						onclickWifif = false;
					}

					imvPartialStatusbarServiceWifi.setVisibility(View.VISIBLE);
					txvPartialStatusbarServiceNetworkName.setVisibility(View.INVISIBLE);
					btnPartialStatusbarServiceWifi.setSelected(true);
					btnPartialStatusbarServiceMobileData.setSelected(false);
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
					btnPartialStatusbarServiceMobileData.setSelected(true);

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
		updateFormatTime();
		updateShowSignalStrength();
		updateShowBattery();
		updateStatusBar();
		updateBackgroudStatusBar();
		updateBluetoothState(R.drawable.ic_bluetooth_on, SystemUtil.isBluetoothEnble());
		if (!SharedPreferencesUtil.getCustomCarrierName(mContext).equals("")) {
			txvPartialStatusbarServiceCarrierName.setText(SharedPreferencesUtil.getCustomCarrierName(mContext));
		} else
			txvPartialStatusbarServiceCarrierName.setText(SystemUtil.getCarrierName(this).toUpperCase());
		new LoadNotificationTask(this, new LoadNotificationTask.OnNotyLoadListener() {
			@Override
			public void onLoadNoty(ArrayList<NotifyEntity> listNotyLeft, ArrayList<NotifyEntity> listNotyRight) {
				mRightNotyList = listNotyRight;
				mLeftNotyList = listNotyLeft;
				mViewpagerAdapter = new AdapterViewpagerNoty(NotifyService.this, mLeftNotyList, mRightNotyList);
				vpgPartialStatusbarService.setAdapter(mViewpagerAdapter);

			}
		}).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        updateHeightStatusbar();
		DelayRequestUpdateReceiver.sSTOP_REQUEST = false;
		AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		Intent serviceIntent = new Intent(DelayRequestUpdateReceiver.IDREQUEST);
		PendingIntent pi = PendingIntent.getBroadcast(mContext, 1, serviceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 1000 * 10, pi);
	}

	public void updateBackgroudStatusBar() {

		String color = SharedPreferencesUtil.getColorStatusBar(NotifyService.this);
		if (color != "") {
			rllPartialStatusbarServiceStatusbar.setBackgroundColor(Color.parseColor(color));

		}
//		rllPartialStatusbarServiceStatusbar.setBackgroundColor((color));

//		img_partial_notify__background.setImageResource(color);
//		rllPartialStatusbarServiceStatusbar.setDrawingCacheBackgroundColor((color));
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void updateBackGroundNotify(String path, int check) {
		switch (check) {
			case 1:
				img_partial_notify__background.setAlpha((float) 0.9);
				img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
				img_partial_notify__background.setBackgroundColor(Color.parseColor("#00000000"));
//				img_partial_notify__background.setImageResource(getDrawable(R.drawable));
				Picasso.with(NotifyService.this).load(R.drawable.bg).into(img_partial_notify__background);


				break;
			case 2:
				switch (path) {
					case "black":
						img_partial_notify__background.setAlpha((float) 0.9);
						img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
						img_partial_notify__background.setBackgroundColor(Color.parseColor("#000000"));
						Picasso.with(NotifyService.this).load((R.drawable.bgr_trongsuot)).into(img_partial_notify__background);
						break;
					case "purple":
						img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
						Picasso.with(NotifyService.this).load(R.drawable.bgr_trongsuot).into(img_partial_notify__background);
						img_partial_notify__background.setBackgroundColor(Color.parseColor("#9C27B0"));
						img_partial_notify__background.setAlpha((float) 0.9);
						break;
					case "blue":
						img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
						Picasso.with(NotifyService.this).load((R.drawable.bgr_trongsuot)).into(img_partial_notify__background);
						img_partial_notify__background.setBackgroundColor(Color.parseColor("#2196F3"));
						img_partial_notify__background.setAlpha((float) 0.9);
						break;
					case "red":
						img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
						Picasso.with(NotifyService.this).load((R.drawable.bgr_trongsuot)).into(img_partial_notify__background);
						img_partial_notify__background.setBackgroundColor(Color.parseColor("#F44336"));
						img_partial_notify__background.setAlpha((float) 0.9);
						break;
					case "orange":
						img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
						Picasso.with(NotifyService.this).load((R.drawable.bgr_trongsuot)).into(img_partial_notify__background);
						img_partial_notify__background.setBackgroundColor(Color.parseColor("#EF6C00"));
						img_partial_notify__background.setAlpha((float) 0.9);
						break;
				}
				break;
			case 3:
				img_partial_notify__background.setAlpha((float) 0.9);
				img_partial_notify__background.setBackgroundColor(Color.parseColor("#00000000"));
				img_partial_notify__background.setScaleType(ImageView.ScaleType.CENTER_CROP);
				Picasso.with(NotifyService.this).load(new File(path)).into(img_partial_notify__background);
				break;
		}
	}

	public void updateStatusBar() {
		if (SharedPreferencesUtil.isShowStatusbar(NotifyService.this)) {
			rllPartialStatusbarServiceStatusbar.setVisibility(View.VISIBLE);
		} else {
			rllPartialStatusbarServiceStatusbar.setVisibility(View.GONE);
		}
	}

//    public void updateCalendar() {
//        if (mViewpagerAdapter != null)
//            mViewpagerAdapter.updateCalendar();
//    }

	public void updateNotification() {
		new LoadNotificationTask(this, new LoadNotificationTask.OnNotyLoadListener() {
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
		} else {
			sgvPartialStatusbarServiceSignalStrength.setVisibility(View.GONE);
		}
	}

	public void updateShowMenuSetting() {
		if (SharedPreferencesUtil.isShoMenuSetting(NotifyService.this)) {
			viewPartialTodayMenuSetting.setVisibility(View.VISIBLE);
			lnlPartialTodayMenuSetting.setVisibility(View.VISIBLE);
		} else {
			viewPartialTodayMenuSetting.setVisibility(View.GONE);
			lnlPartialTodayMenuSetting.setVisibility(View.GONE);
		}
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
		} else {
			txvPartialStatusbarServiceCarrierName.setVisibility(View.GONE);
		}
	}

	public void updateCustomCarrierName() {
		txvPartialStatusbarServiceCarrierName.setText(SharedPreferencesUtil.getCustomCarrierName(mContext));
	}


	private void updateBluetoothState(int idDrawable, boolean isEnable) {
		if (isEnable) {
			imvPartialStatusbarServiceBluetooth.setVisibility(View.VISIBLE);
			imvPartialStatusbarServiceBluetooth.setImageResource(idDrawable);
			btnPartialStatusbarServiceBluetooth.setSelected(true);
		} else {
			imvPartialStatusbarServiceBluetooth.setVisibility(View.INVISIBLE);
			btnPartialStatusbarServiceBluetooth.setSelected(false);
		}
	}


	private void startAnimation(View view, long currentTime, float currentY, float upX) {
		float vantoc = currentY / currentTime;
		Log.e(TAG, "VT = " + vantoc);
		if (vantoc <= 3) {
			LogUtil.getLogger().d(TAG, "ok-8");
			ObjectAnimator ballFallAnimator = ObjectAnimator.ofFloat(view, "translationY", -10, 0);

			// vi tri trên cùng nẩy lên
			ObjectAnimator ballFallAnimatorReverse = ObjectAnimator.ofFloat(view, "translationY", 0, -10);
			if (currentY<500){
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(90);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(60);
			} else if (currentY<1000){
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(110);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(80);
			} else {
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(130);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(100);
			}

			//
			ballFallAnimator.setInterpolator(new MyBounceInterpolator(100));

			ballFallAnimatorReverse.setInterpolator(new FastOutSlowInInterpolator());

			AnimatorSet ballAnimationSet = new AnimatorSet();
			ballAnimationSet.playSequentially(ballFallAnimatorReverse, ballFallAnimator);
			ballAnimationSet.start();
		} else
		if (vantoc <= 8 || vantoc>=50) {
			LogUtil.getLogger().d(TAG, "ok-4");
			ObjectAnimator ballFallAnimator = ObjectAnimator.ofFloat(view, "translationY", -5, 0);

			// vi tri trên cùng nẩy lên
			ObjectAnimator ballFallAnimatorReverse = ObjectAnimator.ofFloat(view, "translationY", 0, -5);
			if (currentY<500){
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(120);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(90);
			} else if (currentY<1000){
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(150);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(120);
			} else {
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(170);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(140);
			}

			//
			ballFallAnimator.setInterpolator(new MyBounceInterpolator(100));

			ballFallAnimatorReverse.setInterpolator(new FastOutSlowInInterpolator());

			AnimatorSet ballAnimationSet = new AnimatorSet();
			ballAnimationSet.playSequentially(ballFallAnimatorReverse, ballFallAnimator);
			ballAnimationSet.start();
		} else if (vantoc <= 13) {
			LogUtil.getLogger().d(TAG, "ok-3");
//			ObjectAnimator ballFallAnimator = ObjectAnimator.ofFloat(view, "translationY", -currentY / 3, 0);
//			ObjectAnimator ballFallAnimatorReverse = ObjectAnimator.ofFloat(view, "translationY", 0, -currentY / 3);
			ObjectAnimator ballFallAnimator = ObjectAnimator.ofFloat(view, "translationY", -currentY / 2.7f, 0);

			// vi tri trên cùng nẩy lên
			ObjectAnimator ballFallAnimatorReverse = ObjectAnimator.ofFloat(view, "translationY", 0, -currentY / 2.7f);
			if (currentY<500){
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(160);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(140);
			} else if (currentY<1000){
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(220);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(190);
			} else {
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(240);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(210);
			}

			//
			ballFallAnimator.setInterpolator(new MyBounceInterpolator(100));

			ballFallAnimatorReverse.setInterpolator(new FastOutSlowInInterpolator());

			AnimatorSet ballAnimationSet = new AnimatorSet();
			ballAnimationSet.playSequentially(ballFallAnimatorReverse, ballFallAnimator);
			ballAnimationSet.start();
		} else if (vantoc <= 18) {
			LogUtil.getLogger().d(TAG, "ok-2");
			ObjectAnimator ballFallAnimator = ObjectAnimator.ofFloat(view, "translationY", -currentY / 1.9f, 0);

			// vi tri trên cùng nẩy lên
			ObjectAnimator ballFallAnimatorReverse = ObjectAnimator.ofFloat(view, "translationY", 0, -currentY / 1.9f);
			if (currentY<500){
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(180);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(160);
			} else if (currentY<1000){
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(240);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(210);
			} else {
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(260);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(230);
			}

			//
			ballFallAnimator.setInterpolator(new MyBounceInterpolator(100));

			ballFallAnimatorReverse.setInterpolator(new FastOutSlowInInterpolator());

			AnimatorSet ballAnimationSet = new AnimatorSet();
			ballAnimationSet.playSequentially(ballFallAnimatorReverse, ballFallAnimator);
			ballAnimationSet.start();
		} else {
			LogUtil.getLogger().d(TAG, "ok-1");

//			ballFallAnimator.setInterpolator(new MyBounceInterpolator(230));
			// // vi tri bắt đầu rơi xuống
			ObjectAnimator ballFallAnimator = ObjectAnimator.ofFloat(view, "translationY", -currentY / 1.5f, 0);

			// vi tri trên cùng nẩy lên
			ObjectAnimator ballFallAnimatorReverse = ObjectAnimator.ofFloat(view, "translationY", 0, -currentY / 1.5f);
			if (currentY<500){
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(200);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(180);
			} else if (currentY<1000){
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(260);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(230);
			} else {
				// thoi gian rơi xuống
				ballFallAnimator.setDuration(280);
				// thoi gian di chuyen len
				ballFallAnimatorReverse.setDuration(250);
			}

			//
			ballFallAnimator.setInterpolator(new MyBounceInterpolator(100));

			ballFallAnimatorReverse.setInterpolator(new FastOutSlowInInterpolator());

			AnimatorSet ballAnimationSet = new AnimatorSet();
			ballAnimationSet.playSequentially(ballFallAnimatorReverse, ballFallAnimator);
			ballAnimationSet.start();
		}

//		if (currentTime < 90 && (currentY > ScreenUtil.getScreenHeight(mContext) / 4 || currentY > ScreenUtil.getScreenHeight(mContext) / 5 && upX > ScreenUtil.getScreenHeight(mContext) / 2)) {
//
//		} else if (currentTime < 120 && (currentY > ScreenUtil.getScreenHeight(mContext) / 4 || currentY > ScreenUtil.getScreenHeight(mContext) / 5 && upX > ScreenUtil.getScreenHeight(mContext) / 2)) {
//
//		} else if (currentTime < 150 && currentY > ScreenUtil.getScreenHeight(mContext) / 4 && upX > ScreenUtil.getScreenHeight(mContext) / 2) {
//
//		} else if (currentTime < 200 && currentY > ScreenUtil.getScreenHeight(mContext) / 4 && upX > ScreenUtil.getScreenHeight(mContext) / 3) {
//
//		} else if (currentTime < 250 && currentY > ScreenUtil.getScreenHeight(mContext) / 4 && upX > ScreenUtil.getScreenHeight(mContext) / 3) {
//			LogUtil.getLogger().d(TAG, "ok-5");
//			ObjectAnimator ballFallAnimator = ObjectAnimator.ofFloat(view, "translationY", -40, 0);
//			ballFallAnimator.setDuration(70);
//			ballFallAnimator.setInterpolator(new MyBounceInterpolator(70));
//
//			ObjectAnimator ballFallAnimatorReverse = ObjectAnimator.ofFloat(view, "translationY", 0, -40);
//			ballFallAnimatorReverse.setDuration(10);
//			if (currentY>=1700){
//				ballFallAnimatorReverse.setDuration(200);
//				ballFallAnimator.setInterpolator(new MyBounceInterpolator(250));
//			}else
//			if (currentY <= 300) {
//				ballFallAnimatorReverse.setDuration(50);
//			} else if (currentY <= 700) {
//				ballFallAnimatorReverse.setDuration(80);
//			} else if (currentY <= 1000) {
//				ballFallAnimatorReverse.setDuration(100);
//			} else
//				ballFallAnimatorReverse.setDuration(130);
//			ballFallAnimatorReverse.setInterpolator(new FastOutSlowInInterpolator());
//
//
//			AnimatorSet ballAnimationSet = new AnimatorSet();
//			ballAnimationSet.playSequentially(ballFallAnimatorReverse, ballFallAnimator);
//			ballAnimationSet.start();
//		} else
//		if (currentTime < 90 && currentY > ScreenUtil.getScreenHeight(mContext) / 5 && upX > ScreenUtil.getScreenHeight(mContext) / 3) {
//			LogUtil.getLogger().d(TAG, "ok-6");
//			ObjectAnimator ballFallAnimator = ObjectAnimator.ofFloat(view, "translationY", -currentY / 1.5f, 0);
//			ballFallAnimator.setDuration(250);
//			ballFallAnimator.setInterpolator(new MyBounceInterpolator(100));
//
//			ObjectAnimator ballFallAnimatorReverse = ObjectAnimator.ofFloat(view, "translationY", 0, -currentY / 1.5f);
//			ballFallAnimatorReverse.setDuration(150);
//			ballFallAnimatorReverse.setInterpolator(new FastOutSlowInInterpolator());
//
//			AnimatorSet ballAnimationSet = new AnimatorSet();
//			ballAnimationSet.playSequentially(ballFallAnimatorReverse, ballFallAnimator);
//			ballAnimationSet.start();
//		} else if (currentTime < 120 && currentY > ScreenUtil.getScreenHeight(mContext) / 5 && upX > ScreenUtil.getScreenHeight(mContext) / 3) {
//			LogUtil.getLogger().d(TAG, "ok-7");
//			ObjectAnimator ballFallAnimator = ObjectAnimator.ofFloat(view, "translationY", -(currentY) / 2, 0);
//			ballFallAnimator.setDuration(100);
//			ballFallAnimator.setInterpolator(new MyBounceInterpolator(100));
//
//			ObjectAnimator ballFallAnimatorReverse = ObjectAnimator.ofFloat(view, "translationY", 0, -(currentY) / 2);
//			ballFallAnimatorReverse.setDuration(100);
//			ballFallAnimatorReverse.setInterpolator(new FastOutSlowInInterpolator());
//
//			AnimatorSet ballAnimationSet = new AnimatorSet();
//			ballAnimationSet.playSequentially(ballFallAnimatorReverse, ballFallAnimator);
//			ballAnimationSet.start();
//		} else if (currentTime < 150 && currentY > ScreenUtil.getScreenHeight(mContext) / 5) {
//
//		}
//		else if (currentTime < 200 && currentY > ScreenUtil.getScreenHeight(mContext) / 5) {
		/*    LogUtil.getLogger().d(TAG, "ok-9");
			ObjectAnimator ballFallAnimator = ObjectAnimator.ofFloat(view, "translationY", -10, 0);
            ballFallAnimator.setDuration(20);
            ballFallAnimator.setInterpolator(new MyBounceInterpolator(100));

            ObjectAnimator ballFallAnimatorReverse = ObjectAnimator.ofFloat(view, "translationY", 0, -10);
            ballFallAnimatorReverse.setDuration(20);
            ballFallAnimatorReverse.setInterpolator(new FastOutSlowInInterpolator());


            AnimatorSet ballAnimationSet = new AnimatorSet();
            ballAnimationSet.playSequentially(ballFallAnimatorReverse, ballFallAnimator);
            ballAnimationSet.start();*/
//		} else if (currentTime < 250 && currentY > ScreenUtil.getScreenHeight(mContext) / 4) {
		   /* LogUtil.getLogger().d(TAG, "ok-10");
			ObjectAnimator ballFallAnimator = ObjectAnimator.ofFloat(view, "translationY", -5, 0);
            ballFallAnimator.setDuration(10);
            ballFallAnimator.setInterpolator(new MyBounceInterpolator(100));

            ObjectAnimator ballFallAnimatorReverse = ObjectAnimator.ofFloat(view, "translationY", 0, -5);
            ballFallAnimatorReverse.setDuration(10);
            ballFallAnimatorReverse.setInterpolator(new FastOutSlowInInterpolator());


            AnimatorSet ballAnimationSet = new AnimatorSet();
            ballAnimationSet.playSequentially(ballFallAnimatorReverse, ballFallAnimator);
            ballAnimationSet.start();*/
//		} else {

//		}
	}

	private boolean touch = false;

	@Override
	public void onPanelSlide(View toolbar, View panelView, float slideOffset, boolean isTouch, long currentTime, float distance, float upX) {
//		Log.d(TAG, "test=" + slideOffset + "-" + isTouch);
		mWindowManager.updateViewLayout(mStatusBarView, getSlideLayoutParams());
//		isTouch = true;
//		imvPartialStatusbarServiceToolbar.setVisibility(View.VISIBLE);
		if (touch == false && isTouch) {
//			mWindowManager.updateViewLayout(mStatusBarView, getSlideLayoutParams());
			touch = true;
			showToolBar(true);
		}
		if (slideOffset > 0 && slideOffset < 1) {
			imvPartialStatusbarServiceToolbar.setImageResource(R.drawable.ic_bar);
		} else
		if (slideOffset == 1) {
			imvPartialStatusbarServiceToolbar.setImageResource(R.drawable.ic_bar_up);
			if (!isTouch) {
				Log.e(TAG, currentTime + " _ " + distance + " _ " + ScreenUtil.getScreenHeight(mContext) + " _ " + upX);
				startAnimation(panelView, currentTime, distance, upX);
				startAnimation(toolbar, currentTime, distance, upX);
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
	   /* imvPartialStatusbarServiceToolbar.setImageResource(R.drawable.ic_bar_up);
		if (!isTouch) {
            LogUtil.getLogger().d(TAG, currentTime + " - " + currentY);
            startAnimation(panelView, currentTime, panelView.getHeight() - currentY);
            startAnimation(toolbar, currentTime, panelView.getHeight() - currentY);
        }*/
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
		if (null != mWindowManager) {
			mWindowManager.removeView(mStatusBarView);
			mStatusBarView = null;
			mWindowManager = null;
			stopSelf();
			DelayRequestUpdateReceiver.sSTOP_REQUEST = true;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogUtil.getLogger().d(TAG, "onDestroy");
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
		DelayRequestUpdateReceiver.sSTOP_REQUEST = true;
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
//
//    protected void hideSoftKeyboard(EditText input) {
//        input.setInputType(0);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
//    }

	@Override
	public void onClick(View v) {

//        if (v == searchEditText) {
//            ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(searchEditText, InputMethodManager.SHOW_FORCED);
//        }

		if (v == clearSearchBox) {
			clearSearchBox.setVisibility(View.GONE);
			searchEditText.setText("");
		}

		if (v == cancel) {
			search.setVisibility(View.GONE);
			showHideCancelControl(false);
			searchEditText.setText("");
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
				try {
					SystemUtil.setMobileDataState(this, false);
				} catch (Exception e) {
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
					startActivity(intent);
					toolbarPanelLayout.closePanel();
					btnPartialStatusbarServiceMobileData.setSelected(true);
				}

			} else {
				btnPartialStatusbarServiceMobileData.setSelected(true);
				try {
					SystemUtil.setMobileDataState(this, false);
				} catch (Exception e) {
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings$DataUsageSummaryActivity"));
					startActivity(intent);
					toolbarPanelLayout.closePanel();
					btnPartialStatusbarServiceMobileData.setSelected(false);
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
		}
	}

	private void updateMenubar() {
		if (SystemUtil.isWifiEnble(this)) {
			btnPartialStatusbarServiceWifi.setSelected(true);
		}
		if (SystemUtil.isMobileDataEnable(this)) {
			btnPartialStatusbarServiceMobileData.setSelected(true);
		}
		if (SystemUtil.isBluetoothEnble()) {
			btnPartialStatusbarServiceBluetooth.setSelected(true);
		}
		if (SystemUtil.isSilentEnable(this)) {
			btnPartialStatusbarServiceMute.setSelected(true);
		}
		if (SystemUtil.isAutoOrientaitonEnable(this)) {
			btnPartialStatusbarServiceAutoRotate.setSelected(true);
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
			adapter.clear();
			search.setVisibility(View.VISIBLE);
			clearSearchBox.setVisibility(View.VISIBLE);
			showHideCancelControl(true);

			List<AppInfo> list = util.result(searchEditText.getText().toString());

			for (AppInfo appInfo : list) {
				adapter.add(appInfo);
			}
		}
	}

	@Override
	public void afterTextChanged(Editable editable) {
	}
}
