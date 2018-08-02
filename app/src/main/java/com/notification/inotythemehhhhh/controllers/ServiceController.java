package com.notification.inotythemehhhhh.controllers;

import android.content.Context;
import android.content.Intent;

import com.notification.inotythemehhhhh.myservices.NotifyService;


public class ServiceController {

    private Context mContext = null;
    private static ServiceController mLockscreenInstance;
    private static Intent startLockscreenIntent;

    public static ServiceController getInstance(Context context) {
        if (mLockscreenInstance == null) {
            if (null != context) {
                mLockscreenInstance = new ServiceController(context);
            } else {
                mLockscreenInstance = new ServiceController();
            }
        }
        if (startLockscreenIntent == null) {
            startLockscreenIntent = new Intent(context, NotifyService.class);
        }
        return mLockscreenInstance;
    }

    private ServiceController() {
        mContext = null;
    }

    private ServiceController(Context context) {
        mContext = context;
    }

    public void startStatusService() {
        mContext.startService(startLockscreenIntent);
    }

    public void stopStatusService() {
        if(NotifyService.getInstance()!=null) {
            NotifyService.getInstance().dettachView();
        }
        mContext.stopService(startLockscreenIntent);
    }
}
