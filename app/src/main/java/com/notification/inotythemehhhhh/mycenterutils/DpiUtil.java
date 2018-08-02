package com.notification.inotythemehhhhh.mycenterutils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by NguyenDuc on 11/01/2015.
 */
public class DpiUtil {
    public static int pxToDip(Context context, int px) {
        DisplayMetrics localDisplayMetrics = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(1, px, localDisplayMetrics);
    }

    public static float dipToPx(Context context, float dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    public static int pxToSp(Context context, int px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scaledDensity);
    }


}


