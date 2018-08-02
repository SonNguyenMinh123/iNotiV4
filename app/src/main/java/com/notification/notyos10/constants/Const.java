package com.notification.notyos10.constants;

import android.net.Uri;

import com.notification.notyos10.objects.NotifyEntity;
import com.notification.notyos10.weather.objects.CurrentWeather;

import java.util.ArrayList;

public class Const {
    public static boolean isOpenedMenuNoty = false;
    public static String encodeBitmap;
    public static Uri uriPhoto;
    public static int levelBlur;
    // Picasa album photos url
    public static final String URL_ALBUM_PHOTOS =
            "https://picasaweb.google.com/data/feed/api/user/minhtien1406/albumid/_ALBUM_ID_?alt=json";
    public static final String PICASA_USER = "";
    public static ArrayList<NotifyEntity> arrNotyMissedCall = new ArrayList<>();
    public static ArrayList<NotifyEntity> arrNotySms = new ArrayList<>();
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String CITYLOCATION = "CITYLOCATION";
    public static final String CURRENTlOCATION = "CURRENTlOCATION";
    public static final String TEMP = "TEMP";
    public static CurrentWeather mFIOCurrentWeather;
    public static boolean weatherSuccess = false;
    public static String TEXT_EDITTEXT_DIALOG_NETWORK = "TEXT_EDITTEXT_DIALOG_NETWORK";
}
