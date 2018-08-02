package com.notification.notyos10.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import com.notification.notyos10.utils.SystemUtil;


public class SimChangedReceiver extends BroadcastReceiver {
    private SimChangedReceiverCallback mSimChangedReceiverCallback;

    public SimChangedReceiver(SimChangedReceiverCallback simChangedReceiverCallback) {
        mSimChangedReceiverCallback = simChangedReceiverCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        TelephonyManager telephoneMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telephoneMgr.getSimState();
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
//                Log.i("SimStateListener", "Sim State absent");
                mSimChangedReceiverCallback.onSimChangedChanged(0, null);
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                mSimChangedReceiverCallback.onSimChangedChanged(0, null);
//                Log.i("SimStateListener", "Sim State network locked");
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
//                Log.i("SimStateListener", "Sim State pin required");
                mSimChangedReceiverCallback.onSimChangedChanged(1, SystemUtil.getCarrierName(context));
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
//                Log.i("SimStateListener", "Sim State puk required");
                mSimChangedReceiverCallback.onSimChangedChanged(1, SystemUtil.getCarrierName(context));
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
//                Log.i("SimStateListener", "Sim State unknown");
                mSimChangedReceiverCallback.onSimChangedChanged(0, null);
                break;
            case TelephonyManager.SIM_STATE_READY:
                mSimChangedReceiverCallback.onSimChangedChanged(1, SystemUtil.getCarrierName(context));
//                Log.i("SimStateListener", "Sim State ready");
        }
//        mSimChangedReceiverCallback.onSimChangedChanged();
    }

    public interface SimChangedReceiverCallback {
        public void onSimChangedChanged(int type, String carrierName);
    }
}
