package com.notification.notyos10.controllers;
import android.content.Context;
import android.content.Intent;

import com.notification.notyos10.services.NotifyService;
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
        return mLockscreenInstance;
    }
    private ServiceController() {
        mContext = null;
    }

    private ServiceController(Context context) {
        mContext = context;
    }

    public void startStatusService() {
        startLockscreenIntent = new Intent(mContext, NotifyService.class);
        mContext.startService(startLockscreenIntent);
    }

    public void stopStatusService() {
        if(NotifyService.getInstance()!=null) {
            NotifyService.getInstance().dettachView();
        }
        startLockscreenIntent = new Intent(mContext, NotifyService.class);
        mContext.stopService(startLockscreenIntent);
    }
}
