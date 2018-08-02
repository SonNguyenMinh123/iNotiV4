package com.notification.inotythemehhhhh.mycenterutils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import com.notification.inotythemehhhhh.objects.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MyPC on 12/07/2016.
 */
public class Util {

    private Context context;
    private List<AppInfo> mList;

    public Util(Context context) {
        this.context = context;
        mList = getApps(context);
    }

    public List<AppInfo> getApps(Context context) {
        List<PackageInfo> apps = context.getPackageManager().getInstalledPackages(0);
        List<AppInfo> list = new ArrayList<>();
        for (int i = 0; i < apps.size(); i++) {
            PackageInfo p = apps.get(i);
            String appname = p.applicationInfo.loadLabel(context.getPackageManager()).toString();
            String pname = p.packageName;
            String versionName = p.versionName;
            int versionCode = p.versionCode;
            Drawable icon = p.applicationInfo.loadIcon(context.getPackageManager());
            AppInfo newInfo = new AppInfo(appname, pname, versionName, versionCode, icon);
            list.add(newInfo);
        }
        return list;
    }


    public List<AppInfo> result(String text) {

        List<AppInfo> list = new ArrayList<>();
        for (AppInfo appInfo : mList) {
            if (appInfo.getAppname().toLowerCase().contains(text.toLowerCase())) {
                list.add(appInfo);
            }
        }
        return list;

    }


}
