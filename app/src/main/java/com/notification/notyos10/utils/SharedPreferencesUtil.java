package com.notification.notyos10.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.notification.notyos10.constants.Const;


public class SharedPreferencesUtil {

    public static void savePreferences2(Context context, String paramString1, String paramString2) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString(paramString1, paramString2);
        localEditor.commit();
    }

    public static String getpreferences(Context context, String paramString) {
        return context.getSharedPreferences("pref", 0).getString(paramString, "0");
    }

    public static String getKindTem(Context context){
        return context.getSharedPreferences("pref",0).getString("KIND_TEMP","F");
    }
    public static void setKindTem(Context context, String kind){
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString("KIND_TEMP", kind);
        localEditor.commit();
    }

    public static void setDotPosition(Context context, int postion){
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putInt("DOT_POSITION", postion);
        localEditor.commit();
    }

    public static int getDotPosition(Context context){
        return context.getSharedPreferences("pref",0).getInt("DOT_POSITION",0);
    }

    public static boolean isShowWeather(Context context){
        return context.getSharedPreferences("pref", 0).getBoolean("SHOW_WHEATHER",false);
    }
    public static void setShowWeather(Context context, boolean value){
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putBoolean("SHOW_WHEATHER", value);
        localEditor.commit();
    }
    public static void setCurrentLocation(Context context, boolean isCurent) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putBoolean(Const.CURRENTlOCATION, isCurent);
        localEditor.commit();
    }

    public static boolean isCurrentLocation(Context context) {
        return context.getSharedPreferences("pref", 0).getBoolean(Const.CURRENTlOCATION, false);
    }

    public static void savePreferences(Context context, String key, String value) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString(key, value);
        localEditor.commit();
    }
    public static String getCurrentCity(Context context){
        return context.getSharedPreferences("pref", 0).getString(Const.CITYLOCATION, "noname");
    }
    public static void saveLocation(Context context, double latitude, double longitude) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString(Const.LATITUDE, latitude + "");
        localEditor.putString(Const.LONGITUDE, longitude + "");
        localEditor.commit();
    }
    public static double[] getLocation(Context context) {
        double[] location = new double[2];
        try {
            String param1 = context.getSharedPreferences("pref", 0).getString(Const.LATITUDE, "0");
            location[0] = Double.parseDouble(param1);
            String param2 = context.getSharedPreferences("pref", 0).getString(Const.LONGITUDE, "0");
            location[1] = Double.parseDouble(param2);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return location;
    }

    public static String getCityLocation(Context context) {
        return context.getSharedPreferences("pref", 0).getString(Const.CITYLOCATION, "n/a");
    }

    public static void setListNameImageDownloaded(Context context, String strList){
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString("LIST_DOWNLOADED", strList);
        localEditor.commit();
    }

    public static String getListNameImageDownloaded(Context context) {
        return context.getSharedPreferences("pref", 0).getString("LIST_DOWNLOADED", "noname");
    }

    public static void setColorStatusBar(Context context, String idColor) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString("COLOR_STATUS_BAR", idColor);
        localEditor.commit();
    }

    public static void setReceived(Context context, boolean callReceived) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putBoolean("CALL_RECEIVED", callReceived);
        localEditor.commit();
    }

    public static boolean getReceived(Context context) {
        return context.getSharedPreferences("pref", 0).getBoolean("CALL_RECEIVED", false);
    }

    public static void setColorStatusBar2(Context context, int idColor) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putInt("COLOR_STATUS_BAR2", idColor);
        localEditor.commit();
    }

    public static int getColorStatusBar2(Context context) {
        return context.getSharedPreferences("pref", 0).getInt("COLOR_STATUS_BAR2", -1);
    }

    public static void setKindBackground(Context context, String kind) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString("KIND_BACKGROUND", kind);
        localEditor.commit();
    }

    public static String getKindBackground(Context context) {
        return context.getSharedPreferences("pref", 0).getString("KIND_BACKGROUND", "null");
    }

    public static void setIdBackground(Context context, int idBackground) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putInt("ID_BACKGROUND", idBackground);
        localEditor.commit();
    }

    public static int getIdBAckground(Context context, int idDrawable) {
        return context.getSharedPreferences("pref", 0).getInt("ID_BACKGROUND", -1);
    }

    public static void setPathBackground(Context context, String idBackground) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString("PATH_BACKGROUND", idBackground);
        localEditor.commit();
    }

    public static String getPathBAckground(Context context) {
        return context.getSharedPreferences("pref", 0).getString("PATH_BACKGROUND", "");
    }

    public static void setBlurLevel(Context context, int level) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putInt("LEVEL_BLUR", level);
        localEditor.commit();
    }

    public static int getLevelBlurred(Context context) {
        return context.getSharedPreferences("pref", 0).getInt("LEVEL_BLUR", 100);
    }

    public static void setUriBackGround(Context context, String uri) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString("URI_BACKGROUND", uri);
        localEditor.commit();
    }

    public static String getUriBackground(Context context) {
        return context.getSharedPreferences("pref", 0).getString("URI_BACKGROUND", "defaultString");
    }

    public static void setBlueColor(Context context, int blur) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("Color", 0).edit();
        localEditor.putInt("BLUE", blur);
        localEditor.commit();
    }

    public static void setRedColor(Context context, int red) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("Color", 0).edit();
        localEditor.putInt("RED", red);
        localEditor.commit();
    }

    public static void setGreenColor(Context context, int green) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("Color", 0).edit();
        localEditor.putInt("GREEN", green);
        localEditor.commit();
    }

    public static void setAlphaColor(Context context, int green) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("Color", 0).edit();
        localEditor.putInt("ALPHA", green);
        localEditor.commit();
    }

    public static int getAlphaColor(Context context) {
        return context.getSharedPreferences("Color", 0).getInt("ALPHA", 0);
    }

    public static int getRedColor(Context context) {
        return context.getSharedPreferences("Color", 0).getInt("RED", 0);
    }

    public static int getBlueColor(Context context) {
        return context.getSharedPreferences("Color", 0).getInt("BLUE", 0);
    }

    public static int getGreenColor(Context context) {
        return context.getSharedPreferences("Color", 0).getInt("GREEN", 0);
    }


    public static void setBackgroundNotify(Context context, String path) {
        SharedPreferences.Editor localEditor = context.getSharedPreferences("pref", 0).edit();
        localEditor.putString("setBackgroundNotify", path);
        localEditor.commit();
    }

    public static String getBackgroundNotify(Context context) {
        return context.getSharedPreferences("pref", 0).getString("setBackgroundNotify", "");
    }

    public static String getColorStatusBar(Context context) {
        return context.getSharedPreferences("pref", 0).getString("COLOR_STATUS_BAR", "");
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
