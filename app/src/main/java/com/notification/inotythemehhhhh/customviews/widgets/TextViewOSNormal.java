package com.notification.inotythemehhhhh.customviews.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewOSNormal extends TextView {

    public TextViewOSNormal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setType(context);
    }

    public TextViewOSNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public TextViewOSNormal(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/osnormal.ttf"));
    }
}
