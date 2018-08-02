package com.notification.notyos10.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import android.provider.Telephony;

public class DefaultAppUtils {
    public static String getPackageDefaultSmsApp(Context mContext){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Telephony.Sms.getDefaultSmsPackage(mContext);
        }else{
            String defApp = Settings.Secure.getString(mContext.getContentResolver(), "sms_default_application");
            PackageManager pm = mContext.getApplicationContext().getPackageManager();
            Intent iIntent = pm.getLaunchIntentForPackage(defApp);
            ResolveInfo mInfo = pm.resolveActivity(iIntent,0);
            return mInfo.activityInfo.packageName;
        }
    }

    public static String isDefaultPhoneApp(Context ctx) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        PackageManager pm = ctx.getApplicationContext().getPackageManager();
        ResolveInfo mInfo = pm.resolveActivity(intent, 0);
        return mInfo.activityInfo.packageName;
    }

    public static String isDefaultPhoneApp2(Context ctx) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        PackageManager pm = ctx.getApplicationContext().getPackageManager();
        ResolveInfo mInfo = pm.resolveActivity(intent, 0);
        return mInfo.activityInfo.packageName;
    }


}
