package com.notification.notyos10.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import com.notification.notyos10.R;
import com.notification.notyos10.constants.Const;
import com.notification.notyos10.objects.NotifyEntity;

public class IncomingSms extends BroadcastReceiver {
    final SmsManager sms = SmsManager.getDefault();

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    Log.e("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
                    NotifyEntity notifyEntity = new NotifyEntity();
                    notifyEntity.setAppName(context.getResources().getString(R.string.str_name_message));
                    notifyEntity.setTitle(context.getResources().getString(R.string.str_message_received));
                    notifyEntity.setContent(context.getResources().getString(R.string.stc_content_message) + message);
                    notifyEntity.setNumberPhone(senderNum);
                    notifyEntity.setPackageName("SMS");
                    Const.arrNotySms.add(notifyEntity);
                    Log.e("SmsReceiver"," : " + Const.arrNotySms.size());
                }
            }
        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }
}
