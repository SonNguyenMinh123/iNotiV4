package com.notification.inotythemehhhhh.mycenterutils;

import android.content.Context;
import android.os.Build;
import android.os.Vibrator;

/**
 * Created by DucNguyen on 6/23/2015.
 */
public class DeviceUtil {

    public static void runVibrate(Context context) {
        // make view vibrate
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500L);
    }

    public static void runVibrateDot(Context context) {
        // make view vibrate
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(50L);
    }

    /**
     * @param versionEquals
     * @return true // if current version > @param
     * @return false // if current version <= param
     */
    public static boolean checkVersionBigger(int versionEquals) {
        if (Build.VERSION.SDK_INT > versionEquals) {
            return true;
        }
        return false;
    }

}
