package com.notification.inotythemehhhhh.mycenterutils;


import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtil {

    public static void setColorStatusBar(Context context, String idColor) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString("COLOR_STATUS_BAR", idColor);
        localEditor.commit();
    }
    public static void setBackgroundNotify(Context context, String path) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString("setBackgroundNotify", path);
        localEditor.commit();
    }

    public static String getBackgroundNotify(Context context) {
        return context.getSharedPreferences("pref", 0).getString("setBackgroundNotify","");
    }
    public static String getColorStatusBar(Context context) {
        return context.getSharedPreferences("pref", 0).getString("COLOR_STATUS_BAR","");
    }


    public static void setShowMenuSetting(Context context, boolean param) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putBoolean("MENU_SETTINGS", param);
        localEditor.commit();
    }

    public static boolean isShoMenuSetting(Context context) {
        return context.getSharedPreferences("pref", 0).getBoolean("MENU_SETTINGS", true);
    }

    public static void setSignalStrength(Context context, boolean param) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putBoolean("SIGNAL_STRENGTH", param);
        localEditor.commit();
    }

    public static boolean isSignalStrength(Context context) {
        return context.getSharedPreferences("pref", 0).getBoolean("SIGNAL_STRENGTH", true);
    }

    public static void setShowBattery(Context context, boolean param) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putBoolean("BATTERY", param);
        localEditor.commit();
    }


    public static boolean isShowBattery(Context context) {
        return context.getSharedPreferences("pref", 0).getBoolean("BATTERY", true);
    }

    public static void setShowStatusbar(Context context, boolean param) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putBoolean("STATUSBAR", param);
        localEditor.commit();
    }
    public static boolean isShowStatusbar(Context context) {
        return context.getSharedPreferences("pref", 0).getBoolean("STATUSBAR", true);
    }

    public static void set24hFormat(Context context, boolean param) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putBoolean("FORMAT", param);
        localEditor.commit();
    }

    public static boolean is24hFormat(Context context) {
        return context.getSharedPreferences("pref", 0).getBoolean("FORMAT", false);
    }

    public static void setShowCarrierName(Context context, boolean param) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putBoolean("CARRIERNAME", param);
        localEditor.commit();
    }

    public static boolean isShowCarrierName(Context context) {
        return context.getSharedPreferences("pref", 0).getBoolean("CARRIERNAME", true);
    }

    public static void setCustomCarrierName(Context context, String name) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString("CUSTOMCARRIERNAME", name);
        localEditor.commit();
    }

    public static String getCustomCarrierName(Context context) {
        return context.getSharedPreferences("pref", 0).getString("CUSTOMCARRIERNAME", "");
    }


}
