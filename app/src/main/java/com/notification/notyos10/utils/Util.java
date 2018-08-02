package com.notification.notyos10.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import com.notification.notyos10.objects.AppInfo;

import java.util.ArrayList;
import java.util.List;


public class Util {

    private Context mContext;
    private List<AppInfo> mList;

    public Util(Context context) {
        mContext = context;
        getApps();
    }

    public void getApps() {
        new AsyncTask<Void,Void,List<AppInfo>>() {

            @Override
            protected List<AppInfo> doInBackground(Void... params) {
                List<PackageInfo> apps = mContext.getPackageManager().getInstalledPackages(0);
                List<AppInfo> list = new ArrayList<>();
                for (int i = 0; i < apps.size(); i++) {
                    PackageInfo p = apps.get(i);
                    String appname = p.applicationInfo.loadLabel(mContext.getPackageManager()).toString();
                    String pname = p.packageName;
                    String versionName = p.versionName;
                    int versionCode = p.versionCode;
                    Drawable icon = null;
                    try {
                        icon = p.applicationInfo.loadIcon(mContext.getPackageManager());
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                    AppInfo newInfo = new AppInfo(appname, pname, versionName, versionCode, icon);
                    list.add(newInfo);
                }
                return list;
            }

            @Override
            protected void onPostExecute(List<AppInfo> appList) {
                mList=appList;
                super.onPostExecute(appList);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    public List<AppInfo> result(String text) {
        List<AppInfo> list = new ArrayList<>();
        for (AppInfo appInfo : mList) {
            if (appInfo.getAppname().toLowerCase().contains(text.toLowerCase())) {
                PackageManager packageManager = mContext.getPackageManager();
                Intent launchApp = packageManager.getLaunchIntentForPackage(appInfo.getPname());
                if (launchApp != null) {
                    list.add(appInfo);
                }
            }
        }
        return list;
    }


}
