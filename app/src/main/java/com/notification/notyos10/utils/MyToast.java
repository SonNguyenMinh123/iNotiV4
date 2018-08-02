package com.notification.notyos10.utils;

import android.content.Context;
import android.widget.Toast;


public class MyToast {
    private static Toast myToast;

    public static void showToast(Context mContext, String text) {
        if (myToast != null) {
            myToast.cancel();
        } else {
            myToast = new Toast(mContext);
        }
        myToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        myToast.show();
    }

}
