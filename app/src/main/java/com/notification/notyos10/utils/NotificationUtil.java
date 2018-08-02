package com.notification.notyos10.utils;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.text.TextUtils;

public class NotificationUtil {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";

    public static boolean isEnabled(Context context) {
        String pkgName = context.getPackageName();
        final String flat = Settings.Secure.getString(context.getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
        * Returns estimated value of the lightness of the status bar background
        * @return
                */
    public static int getStatusBarBackgroundLightValue(Context context) {
        // better this than nothing
        Drawable bg = context.getResources().getDrawable(android.R.drawable.status_bar_item_background);
        int height = Math.max(1, bg.getIntrinsicHeight());
        int width = Math.max(1, bg.getIntrinsicWidth());
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        bg.setBounds(0, 0, width, height);
        bg.draw(canvas);

        long sum = 0;

        int pixel = bitmap.getPixel(10,10);
        bitmap.recycle();
        bitmap = null;
        canvas = null;
        bg = null;
        sum = sum / (width * height);
        // should be [0..255]
        return pixel;
    }

}
