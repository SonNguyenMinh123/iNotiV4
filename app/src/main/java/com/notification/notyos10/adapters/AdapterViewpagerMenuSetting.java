package com.notification.notyos10.adapters;

import android.animation.Animator;
import android.content.Context;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.notification.notyos10.R;
import com.notification.notyos10.animations.creative.FadeAnimator;
import com.notification.notyos10.utils.SystemUtil;

public class AdapterViewpagerMenuSetting extends PagerAdapter {
    private Context context;
    private CommucationMenuSetting mCommucationMenuSetting;

    public AdapterViewpagerMenuSetting(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View myPager = null;
        if (position == 0) {
            myPager = inflater.inflate(R.layout.pager_first_menu_setting, container, false);
            initFirtMenu(myPager);
        } else if (position == 1) {
            myPager = inflater.inflate(R.layout.pager_second_menu_setting, container, false);
            initSecondMenu(myPager);
        }
        container.addView(myPager);
        return myPager;
    }

    private ImageButton btnSettingMore;
    private ImageButton btnSwitchWifi;
    private ImageButton btnSwitchDataMobile;
    private ImageButton btnSwitchRingtone;
    private ImageButton btnSwitchFlash;

    private boolean flashIsOn;

    private void initFirtMenu(View view) {
        btnSettingMore = (ImageButton) view.findViewById(R.id.btn_pager_firsr_menu_setting__setting_more);
        btnSwitchWifi = (ImageButton) view.findViewById(R.id.btn_pager_firsr_menu_setting__setting_wifi);
        btnSwitchDataMobile = (ImageButton) view.findViewById(R.id.btn_pager_firsr_menu_setting__data_mobile);
        btnSwitchRingtone = (ImageButton) view.findViewById(R.id.btn_pager_firsr_menu_setting__ringtone);
        btnSwitchFlash = (ImageButton) view.findViewById(R.id.btn_pager_firsr_menu_setting__flash);

        if (SystemUtil.isWifiEnble(context)) {
            btnSwitchWifi.setImageResource(R.drawable.ic_wifi_pre);
        } else
            btnSwitchWifi.setImageResource(R.drawable.ic_wifi);

        if (SystemUtil.isMobileDataEnable(context)) {
            btnSwitchDataMobile.setImageResource(R.drawable.ic_data_pre);
        } else {
            btnSwitchDataMobile.setImageResource(R.drawable.ic_data);
        }

        if (SystemUtil.isSilentEnable(context)) {
            btnSwitchRingtone.setImageResource(R.drawable.ic_ringtone);
        } else
            btnSwitchRingtone.setImageResource(R.drawable.ic_ringtone_pre);
        flashIsOn = false;
        btnSettingMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommucationMenuSetting.clickSettingMore();
            }
        });

        btnSwitchWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemUtil.isWifiEnble(context)) {
                    SystemUtil.setWifiEnable(context, false);
                    btnSwitchWifi.setImageResource(R.drawable.ic_wifi);
                } else {
                    SystemUtil.setWifiEnable(context, true);
                    btnSwitchWifi.setImageResource(R.drawable.ic_wifi_pre);
                }
            }
        });

        btnSwitchDataMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommucationMenuSetting.clickDataMobile((ImageButton) v);
            }
        });

        btnSwitchRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemUtil.isSilentEnable(context)) {
                    SystemUtil.setSilentEnable(context, false);
                    btnSwitchRingtone.setImageResource(R.drawable.ic_ringtone);
                } else {
                    SystemUtil.setSilentEnable(context, true);
                    btnSwitchRingtone.setImageResource(R.drawable.ic_ringtone_pre);
                }
            }
        });

        btnSwitchFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SystemUtil.isFlashSupported(context.getPackageManager())){
                    Toast.makeText(context,R.string.str_not_support_flash,Toast.LENGTH_LONG).show();
                    return;
                }
                if (!flashIsOn) {
                    btnSwitchFlash.setImageResource(R.drawable.ic_flashlight_pre);
                    SystemUtil.setEnableFlashLight(true);
                    flashIsOn = true;
                } else {
                    btnSwitchFlash.setImageResource(R.drawable.ic_flashlight);
                    SystemUtil.setEnableFlashLight(false);
                    flashIsOn = false;
                }
            }
        });

    }
    private RelativeLayout frControlBrightness;
    private SeekBar skbBrightness;
    private ImageButton btnOkBrightness;

    private LinearLayout lnlControlMenuSetting;
    private ImageButton btnSwitchRotate;
    private ImageButton btnSwitchLocation;
    private ImageButton btnBatterySaver;
    private ImageButton btnManagerApp;
    private ImageButton btnModeAirplane;

    public void initSecondMenu(View view) {
        lnlControlMenuSetting = (LinearLayout) view.findViewById(R.id.lnl_pager_second_menu_setting__control);
        btnSwitchRotate = (ImageButton) view.findViewById(R.id.ic_switch_pager_second_menu_setting_rotation_disabled);
        btnSwitchLocation = (ImageButton) view.findViewById(R.id.ic_switch_pager_second_menu_setting_rotation_airplane);
        btnBatterySaver = (ImageButton) view.findViewById(R.id.ic_switch_pager_second_menu_setting_batery_saver);
        btnManagerApp = (ImageButton) view.findViewById(R.id.ic_switch_pager_second_menu_setting_manager_app);
        btnModeAirplane = (ImageButton) view.findViewById(R.id.ic_switch_pager_second_menu_setting_mode_airplane);
        skbBrightness = (SeekBar) view.findViewById(R.id.seekBar_pager_second_menu_setting__brightness);
        btnOkBrightness = (ImageButton) view.findViewById(R.id.btn_pager_second_menu_setting__ok_brighness);
        frControlBrightness = (RelativeLayout) view.findViewById(R.id.rll_pager_second_menu_setting_control_brightness);
        if (SystemUtil.isAutoOrientaitonEnable(context)) {
            btnSwitchRotate.setImageResource(R.drawable.ic_rotation_pre);
        } else
            btnSwitchRotate.setImageResource(R.drawable.ic_rotation);

        btnSwitchRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemUtil.isAutoOrientaitonEnable(context)) {
                    SystemUtil.setAutoOrientationEnabled(context, false);
                    btnSwitchRotate.setImageResource(R.drawable.ic_rotation);
                } else {
                    SystemUtil.setAutoOrientationEnabled(context, true);
                    btnSwitchRotate.setImageResource(R.drawable.ic_rotation_pre);
                }
            }
        });

        btnSwitchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommucationMenuSetting.clickLoccation();
            }
        });

        btnBatterySaver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommucationMenuSetting.clickBatterySaver();
            }
        });

        btnManagerApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FadeAnimator.playFadeOut(lnlControlMenuSetting, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        frControlBrightness.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                       lnlControlMenuSetting.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                setSeekbar();
            }
        });

        btnModeAirplane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommucationMenuSetting.clickModeAirPlane();
            }
        });
    }

    private void setSeekbar()
    {
        skbBrightness.setMax(255);
        float curBrightnessValue = 0;
        try {
            curBrightnessValue = android.provider.Settings.System.getInt(
                   context.getContentResolver(),
                    android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        int screen_brightness = (int) curBrightnessValue;
        skbBrightness.setProgress(screen_brightness);
        skbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue,
                                          boolean fromUser) {
                progress = progresValue;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                android.provider.Settings.System.putInt(context.getContentResolver(),
                        android.provider.Settings.System.SCREEN_BRIGHTNESS,
                        progress);
            }
        });

        btnOkBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FadeAnimator.playinitFadeIn(lnlControlMenuSetting, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        lnlControlMenuSetting.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        frControlBrightness.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });
    }


    public void listener(CommucationMenuSetting mCommucationMenuSetting) {
        this.mCommucationMenuSetting = mCommucationMenuSetting;
    }

    public interface CommucationMenuSetting {
        void clickSettingMore();

        void clickDataMobile(ImageButton btnDataMobile);

        void clickLoccation();

        void clickBatterySaver();

        void clickManagerApp();

        void clickModeAirPlane();
    }


}
