package com.notification.inotythemehhhhh.receiverapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class UpdateCurrentTimeReceiver extends BroadcastReceiver {
    private UpdateCurrentTimeReceiverCallback mUpdateCurrentTimeReceiverCallback;

    public UpdateCurrentTimeReceiver(UpdateCurrentTimeReceiverCallback updateCurrentTimeReceiverCallback) {
        mUpdateCurrentTimeReceiverCallback = updateCurrentTimeReceiverCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
            mUpdateCurrentTimeReceiverCallback.onUpdateCurrentTimeChanged();
        }
    }

    public interface UpdateCurrentTimeReceiverCallback {
        public void onUpdateCurrentTimeChanged();
    }
}
