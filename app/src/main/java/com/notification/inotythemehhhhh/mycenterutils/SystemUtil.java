package com.notification.inotythemehhhhh.mycenterutils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class SystemUtil {
    private static final String AIRPLANE_MODE = "airplanemodeupdate";
    private static Camera camera;

    public static void setAirPlaneMode(Context context, boolean mode) {
        Intent broadcastIntent = new Intent(AIRPLANE_MODE);
        broadcastIntent.putExtra("enablePlane", mode);
        context.sendBroadcast(broadcastIntent);
    }

    public static boolean isPlaneEnable(Context context) {
        boolean isEnabled = Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        return isEnabled;
    }

    public static void setWifiEnable(Context context, boolean status) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (status == true && !wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        } else if (status == false && wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }
    }

    public static boolean isWifiEnble(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    public static int getLevelWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        int numberOfLevels = 100;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        return level;
    }

    public static String getCarrierName(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String carrierName = manager.getNetworkOperatorName();
        return carrierName;
    }


    public static void setMobileDataState(Context context, boolean mobileDataEnabled) throws Exception {
        try {
            final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final Class conmanClass = Class.forName(conman.getClass().getName());

            final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
            iConnectivityManagerField.setAccessible(true);
            final Object iConnectivityManager = iConnectivityManagerField.get(conman);
            final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
            final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
            setMobileDataEnabledMethod.setAccessible(true);
            setMobileDataEnabledMethod.invoke(iConnectivityManager, mobileDataEnabled);

        } catch (Exception e) {
            try {
                final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                Class conmanClass;
                Class[] cArg = new Class[2];
                cArg[0] = String.class;
                cArg[1] = Boolean.TYPE;
                Method setMobileDataEnabledMethod;
                conmanClass = Class.forName(conman.getClass().getName());
                final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
                iConnectivityManagerField.setAccessible(true);
                final Object iConnectivityManager = iConnectivityManagerField.get(conman);
                final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
                setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", cArg);

                Object[] pArg = new Object[2];
                pArg[0] = context.getPackageName();
                pArg[1] = mobileDataEnabled;
                setMobileDataEnabledMethod.setAccessible(true);
                setMobileDataEnabledMethod.invoke(iConnectivityManager, pArg);
            } catch (Exception e2) {
                Method dataConnSwitchmethod;
                Object ITelephonyStub;

                TelephonyManager tmanager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

                try {
                    Method getITelephonyMethod = tmanager.getClass().getDeclaredMethod("getITelephony");

                    getITelephonyMethod.setAccessible(true);
                    ITelephonyStub = getITelephonyMethod.invoke(tmanager);

                    if (mobileDataEnabled) {
                        dataConnSwitchmethod = ITelephonyStub.getClass().getDeclaredMethod("enableDataConnectivity");
                    } else {
                        dataConnSwitchmethod = ITelephonyStub.getClass().getDeclaredMethod("disableDataConnectivity");
                    }
                    dataConnSwitchmethod.setAccessible(true);
                    dataConnSwitchmethod.invoke(ITelephonyStub);

                } catch (Exception e3) {
                    ConnectivityManager dataManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    Method dataClass = null;
                    try {
                        dataClass = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
                        dataClass.setAccessible(true);
                        dataClass.invoke(dataManager, mobileDataEnabled);
                    } catch (Exception e4) {
                        throw new Exception();
                    }
                }
            }


        }
    }

    public static boolean isMobileDataEnable(Context context) {
        boolean mobileYN = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mobileYN = Settings.Global.getInt(context.getContentResolver(), "mobile_data", 1) == 1;
        } else {
            mobileYN = Settings.Secure.getInt(context.getContentResolver(), "mobile_data", 1) == 1;
        }
        return mobileYN;
    }


    public static String getMobileNetworkName(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null || !info.isConnected())
            return "-"; //not connected
        if (info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
    }

    public static boolean isBluetoothEnble() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.isEnabled();
    }

    public static void setAutoOrientationEnabled(Context context, boolean enabled) {
        Settings.System.putInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
    }

    public static void setEnableFlashLight(boolean show) {
        try {
            if (camera == null) {
                camera = Camera.open();
            }
            Camera.Parameters p = camera.getParameters();
            boolean on = show;
            if (on) {
                Log.i("info", "torch is turn on!");
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                SurfaceTexture mPreviewTexture = new SurfaceTexture(0);
                try {
                    camera.setPreviewTexture(mPreviewTexture);
                } catch (IOException ex) {
                    // Ignore
                }
                camera.startPreview();
            } else {
                Log.i("info", "torch is turn off!");
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(p);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAutoOrientaitonEnable(Context context) {
        if (Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            return true;
        }
        return false;
    }

    public static void setSilentEnable(Context context, boolean enabled) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        if (enabled) {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        } else {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }

    public static boolean isSilentEnable(Context context) {
        AudioManager mAudioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        int ringermode = mAudioManager.getRingerMode();
        if (ringermode == mAudioManager.RINGER_MODE_VIBRATE) {
            return true;
        }
        return false;
    }

    public static int getBrightness(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return 255;
        }
    }

    static Handler handler = new Handler();

    public static void setBrightness(final Context context, final int value) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);
                        // here I set current screen brightness programmatically
                        Window window = ((Activity) context).getWindow();
                        LayoutParams layoutpars = window.getAttributes();
                        layoutpars.screenBrightness = value / (float) 255;
                        window.setAttributes(layoutpars);
                    }
                }).run();
            }
        });


    }


    /**
     * @param packageManager
     * @return true <b>if the device support camera flash</b><br/>
     * false <b>if the device doesn't support camera flash</b>
     */
    public static boolean isFlashSupported(PackageManager packageManager) {
        // if device support camera flash?
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            return true;
        }
        return false;
    }

}
