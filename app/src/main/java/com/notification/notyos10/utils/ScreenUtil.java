package com.notification.notyos10.utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class ScreenUtil {
    public static int CURENT_ITEM = 0;
    public static int[] getCameraSize(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return new int[]{dm.widthPixels, dm.heightPixels};
    }
    public static int getScreenHeight(Context context) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getHeight();
    }

    public static int getScreenWidth(Context context) {

        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        return display.getWidth();

    }


    public static int getHasSoftKeys(Context context, WindowManager paramWindowManager) {
//        boolean hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey();
//        return hasMenuKey;
        int i;
        int j;
        int k;
        int m;
        Display localDisplay = paramWindowManager.getDefaultDisplay();
        DisplayMetrics localDisplayMetrics1 = new DisplayMetrics();
        localDisplay.getRealMetrics(localDisplayMetrics1);
        i = localDisplayMetrics1.heightPixels;
        j = localDisplayMetrics1.widthPixels;

        DisplayMetrics localDisplayMetrics2 = new DisplayMetrics();
        localDisplay.getMetrics(localDisplayMetrics2);
        k = localDisplayMetrics2.heightPixels;
        m = localDisplayMetrics2.widthPixels;
        int sSoftKeyHeight = i - k;
        if ((j - m > 0) || (i - k > 0)) {
            return sSoftKeyHeight;
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            result = context.getResources().getDimensionPixelSize(resourceId);

        return result;
    }

    public static int getHeightStatusBar(Window window) {
        Rect rectangle= new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight= rectangle.top;
        int contentViewTop=
                window.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight= contentViewTop - statusBarHeight;
        return titleBarHeight;
    }

}
