package com.notification.inotythemehhhhh.receiverapp;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.notification.inotythemehhhhh.R;
import com.notification.inotythemehhhhh.mycenterutils.logs.LogUtil;


public class BluetoothchangedReceiver extends BroadcastReceiver {
    private static final String TAG = "BluetoothchangedReceiver";
    private int idDrawable = 0;
    private BluetoothReceiverCallback mBluetoothReceiverCallback;

    public BluetoothchangedReceiver(BluetoothReceiverCallback bluetoothReceiverCallback) {
        mBluetoothReceiverCallback = bluetoothReceiverCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();

        if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
            final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

            LogUtil.getLogger().d(TAG, state + "");
            switch (state) {
                case BluetoothAdapter.STATE_OFF:
                    idDrawable = 0;
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    idDrawable = R.drawable.ic_bluetooth_off;
                    break;
               /*     case BluetoothAdapter.STATE_ON:
                        idDrawable = R.drawable.ic_bluetooth_off;
                        break;*/
                case BluetoothAdapter.STATE_TURNING_ON:
                    idDrawable = R.drawable.ic_bluetooth_on;
                    break;
            }

            if (idDrawable == 0) {
                mBluetoothReceiverCallback.onBluetoothchanged(0, false);
            } else {
                mBluetoothReceiverCallback.onBluetoothchanged(idDrawable, true);

            }
        }
    }

    public interface BluetoothReceiverCallback {
        public void onBluetoothchanged(int idDrawable, boolean isEnable);
    }
}
