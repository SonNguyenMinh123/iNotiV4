package com.notification.notyos10.objects;

import android.graphics.drawable.Drawable;

public class AppInfo {

    String appname = "";
    String pname = "";
    String versionName = "";
    int versionCode = 0;
    Drawable icon;

    public AppInfo(String appname, String pname, String versionName, int versionCode, Drawable icon) {
        this.appname = appname;
        this.pname = pname;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.icon = icon;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

}
