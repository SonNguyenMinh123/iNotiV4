package com.notification.inotythemehhhhh.widgets;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.mobiad.sdk.Start;
import com.notification.inotythemehhhhh.R;
import com.notification.inotythemehhhhh.customviews.widgets.ButtonOSNormal;
import com.notification.inotythemehhhhh.customviews.widgets.TextViewOSBold;
import com.notification.inotythemehhhhh.mycenterutils.SharedPreferencesUtil;
import com.notification.inotythemehhhhh.mycenterutils.SystemUtil;
import com.notification.inotythemehhhhh.myservices.NotifyService;
import com.sevenheaven.iosswitch.ShSwitchView;

import cn.pedant.sweetalert.SweetAlertDialog;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
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
	private TextView txvActivityMainClockFormatType;
	private ShSwitchView swvActivityMainCarierName;
	private RelativeLayout rllActivityMainCustomCarierName;
	private ShSwitchView swvActivityMainMenuSetting;
	private ShSwitchView swvActivitySettingStatusbar;
	private RelativeLayout view_activity_setting__color_statusbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

//		Start.run(this, "inoty_10_v1");
	}

	@Override
	protected void onPostResume() {
		super.onPostResume();
		initData();
	}

	@Override
	public void onContentChanged() {
		super.onContentChanged();
		show_activity_setting__color_statusbar = (ImageView) findViewById(R.id.show_activity_setting__color_statusbar);
		if (!SharedPreferencesUtil.getColorStatusBar(SettingActivity.this).equals("")) {
			show_activity_setting__color_statusbar.setBackgroundColor(Color.parseColor(SharedPreferencesUtil.getColorStatusBar(SettingActivity.this)));
		} else {
			show_activity_setting__color_statusbar.setBackgroundColor(Color.BLACK);
		}
		view_activity_setting__color_statusbar = (RelativeLayout) findViewById(R.id.view_activity_setting__color_statusbar);
		txvActivitySettingSettings = (TextViewOSBold) findViewById(R.id.txv_activity_setting_settings);
		rllActivityMainEnableNotify = (RelativeLayout) findViewById(R.id.rll_activity_setting__enable_notify);
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
		swvActivityMainMenuSetting = (ShSwitchView) findViewById(R.id.swv_activity_setting__menu_setting);
		view_activity_setting__color_statusbar.setOnClickListener(this);
		rllActivityMainEnableNotify.setOnClickListener(this);
		rllActivityMainClockFormat.setOnClickListener(this);
		rllActivityMainCustomCarierName.setOnClickListener(this);
		rllActivityMainRate.setOnClickListener(this);
		view_activity_setting__background = (RelativeLayout) findViewById(R.id.view_activity_setting__background);
		view_activity_setting__background.setOnClickListener(this);
		swvActivityMainMenuSetting.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
			@Override
			public void onSwitchStateChange(boolean isOn) {
				SharedPreferencesUtil.setShowMenuSetting(SettingActivity.this, isOn);
				NotifyService notifyService = NotifyService.getInstance();
				if (notifyService != null) {
					notifyService.updateShowMenuSetting();
				}
			}
		});
		swvActivityMainSignalStrength.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
			@Override
			public void onSwitchStateChange(boolean isOn) {
				SharedPreferencesUtil.setSignalStrength(SettingActivity.this, isOn);
				NotifyService notifyService = NotifyService.getInstance();
				if (notifyService != null) {
					notifyService.updateShowSignalStrength();
				}
			}
		});
		swvActivityMainBattery.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
			@Override
			public void onSwitchStateChange(boolean isOn) {
				SharedPreferencesUtil.setShowBattery(SettingActivity.this, isOn);
				NotifyService notifyService = NotifyService.getInstance();
				if (notifyService != null) {
					notifyService.updateShowBattery();
				}
			}
		});
		swvActivityMainCarierName.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
			@Override
			public void onSwitchStateChange(boolean isOn) {
				SharedPreferencesUtil.setShowCarrierName(SettingActivity.this, isOn);
				NotifyService notifyService = NotifyService.getInstance();
				if (notifyService != null) {
					notifyService.updateShowCarrierName();
				}
			}
		});
		swvActivitySettingStatusbar.setOnSwitchStateChangeListener(new ShSwitchView.OnSwitchStateChangeListener() {
			@Override
			public void onSwitchStateChange(boolean isOn) {
				SharedPreferencesUtil.setShowStatusbar(SettingActivity.this, isOn);
				NotifyService notifyService = NotifyService.getInstance();
				if (notifyService != null) {
					notifyService.updateStatusBar();
				}
			}
		});
		swvActivityMainSignalStrength.setOn(SharedPreferencesUtil.isSignalStrength(SettingActivity.this));
		swvActivityMainBattery.setOn(SharedPreferencesUtil.isShowBattery(SettingActivity.this));
		swvActivityMainCarierName.setOn(SharedPreferencesUtil.isShowCarrierName(SettingActivity.this));
		swvActivityMainMenuSetting.setOn(SharedPreferencesUtil.isShoMenuSetting(SettingActivity.this));
		swvActivitySettingStatusbar.setOn(SharedPreferencesUtil.isShowStatusbar(SettingActivity.this));
		//-----------------------


	}

	@Override
	public void onClick(final View v) {
		if (v == rllActivityMainEnableNotify) {
			try {
				startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
			} catch (ActivityNotFoundException e) {
				try {
					startActivity(new Intent("android.settings.NOTIFICATION_LISTENER_SETTINGS"));
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		} else if (v == view_activity_setting__color_statusbar) {

			ColorPickerDialogBuilder
					.with(v.getContext())
					.setTitle("Choose color")
					.initialColor(Color.RED)
					.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
					.density(12)
					.setOnColorSelectedListener(new OnColorSelectedListener() {
						@Override
						public void onColorSelected(int selectedColor) {
//							Toast.makeText(v.getContext(), "onColorSelected: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
						}
					})
					.setPositiveButton("ok", new ColorPickerClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
//							changeBackgroundColor(selectedColor);
//							Toast.makeText(v.getContext(), "" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
							NotifyService notifyService = NotifyService.getInstance();
							if (notifyService != null) {
								SharedPreferencesUtil.setColorStatusBar(SettingActivity.this, "#" + Integer.toHexString(selectedColor));
								notifyService.updateBackgroudStatusBar();
//								show_activity_setting__color_statusbar.getBackground().setColorFilter(Color.parseColor(SharedPreferencesUtil.getColorStatusBar(SettingActivity.this)),null);
								show_activity_setting__color_statusbar.setBackgroundColor(Color.parseColor(SharedPreferencesUtil.getColorStatusBar(SettingActivity.this)));
							} else {
								Toast.makeText(v.getContext(), "You need turn ON iNoty", Toast.LENGTH_SHORT).show();
							}
						}
					})
					.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					})
					.build()
					.show();

		} else if (v == rllActivityMainClockFormat) {
			Intent intent = new Intent(this, TimeDateFormatActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
		} else if (v == rllActivityMainCustomCarierName) {
			final Dialog dialog = new Dialog(this);
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			int screenWidth = (int) (metrics.widthPixels * 0.80);
			lp.width = screenWidth;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			dialog.setContentView(R.layout.dialog_custom_carrier_name);
			dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
			final EditText edtDialogCustomCarrierNameInput = (EditText) dialog.findViewById(R.id.edt_dialog_custom_carrier_name__input);
			ButtonOSNormal btnDialogCustomCarrierNameOk = (ButtonOSNormal) dialog.findViewById(R.id.btn_dialog_custom_carrier_name__ok);
			btnDialogCustomCarrierNameOk.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					NotifyService notifyService = NotifyService.getInstance();
					String carrierName = edtDialogCustomCarrierNameInput.getText().toString();
					if (carrierName.equals("")) {
						carrierName = " ";
					}
					SharedPreferencesUtil.setCustomCarrierName(SettingActivity.this, carrierName);
					if (notifyService != null) {
						notifyService.updateCustomCarrierName();
					}
					dialog.dismiss();
				}
			});
			ButtonOSNormal btnDialogCustomCarrierNameCancel = (ButtonOSNormal) dialog.findViewById(R.id.btn_dialog_custom_carrier_name__cancel);
			btnDialogCustomCarrierNameCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			ButtonOSNormal btnDialogCustomCarrierNameDefault = (ButtonOSNormal) dialog.findViewById(R.id.btn_dialog_custom_carrier_name__default);
			btnDialogCustomCarrierNameDefault.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String carrierName = SystemUtil.getCarrierName(SettingActivity.this).toUpperCase();
					edtDialogCustomCarrierNameInput.setText(carrierName);
				}
			});
			dialog.show();
//            dialog.getWindow().setAttributes(lp);
		} else if (v == rllActivityMainRate) {
			launchMarket();
		} else if (v == view_activity_setting__background) {
			Intent intent = new Intent(SettingActivity.this, BackGroundActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.anim_fast_right_to_center, R.anim.anim_low_center_to_left);
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
			txvActivityMainClockFormatType.setText(R.string.format_24h);

		} else {
			txvActivityMainClockFormatType.setText(R.string.format_12h);
		}

	}

	@Override
	public void onBackPressed() {

		SharedPreferences sharedPreferences = getSharedPreferences("FirtInstall", Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = sharedPreferences.edit();
		int checkFirst = getSharedPreferences("FirtInstall", MODE_PRIVATE).getInt("check", 0);
		if (checkFirst == 0) {
			final SweetAlertDialog dialog = new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
			dialog.setTitleText("Please!");
			dialog.setContentText("Rate our app 5 stars! Thank you much!");
			dialog.setCustomImage(R.drawable.ic_stars);
			dialog.setCancelText("Later");
			dialog.setConfirmText("Yes");
			dialog.showCancelButton(true);
			dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
				@Override
				public void onClick(SweetAlertDialog sDialog) {
					finish();
					dialog.dismiss();
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
			finish();
		}
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

}
