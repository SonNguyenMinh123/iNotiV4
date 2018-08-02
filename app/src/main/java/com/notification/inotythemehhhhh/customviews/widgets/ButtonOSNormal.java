package com.notification.inotythemehhhhh.customviews.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonOSNormal extends Button {
    public ButtonOSNormal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setType(context);
    }

    public ButtonOSNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public ButtonOSNormal(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/osnormal.ttf"));
    }
}
