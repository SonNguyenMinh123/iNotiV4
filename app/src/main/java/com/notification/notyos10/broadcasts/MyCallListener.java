package com.notification.notyos10.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.notification.notyos10.R;
import com.notification.notyos10.constants.Const;
import com.notification.notyos10.objects.NotifyEntity;

public class MyCallListener extends BroadcastReceiver {
    static boolean isRinging = false;
    static boolean isReceived = false;
    static String callerPhoneNumber;
    static boolean isFirts;
    //    @Override
    public void onReceive(Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        if (state == null)
            return;
        //phone is ringing
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING))
        {
            isRinging = true;
            //get caller's number
            Bundle bundle = intent.getExtras();
            callerPhoneNumber = bundle.getString("incoming_number");
            isFirts = true;
            isReceived = false;
        }

        //phone is received
        if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
        {
            isReceived = true;
        }
        // phone is idle
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE))
        {
            // detect missed call
            if (isRinging == true && isReceived == false && isFirts) {
                NotifyEntity notifyEntityMissCall = new NotifyEntity();
                notifyEntityMissCall.setAppName("Phone");
                notifyEntityMissCall.setContent(context.getResources().getString(R.string.str_missed_call_from) + " : " + callerPhoneNumber);
                notifyEntityMissCall.setNumberPhone(callerPhoneNumber);
                notifyEntityMissCall.setTitle("Missed call");
                notifyEntityMissCall.setPackageName("Miss_Call_Pkm");
                Const.arrNotyMissedCall.add(notifyEntityMissCall);
                isFirts = false;
                Log.e("test_phone", "size : " + Const.arrNotyMissedCall.size());
            }else{
                if (isFirts == false){
                    isRinging = false;
                    isReceived = false;
                }
            }
        }
    }
}
