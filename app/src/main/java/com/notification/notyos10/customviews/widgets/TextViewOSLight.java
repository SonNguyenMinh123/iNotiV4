package com.notification.notyos10.customviews.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewOSLight extends TextView {

    public TextViewOSLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setType(context);
    }

    public TextViewOSLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public TextViewOSLight(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/oslight.ttf"));
    }
}
