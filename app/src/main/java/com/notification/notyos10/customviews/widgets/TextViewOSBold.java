package com.notification.notyos10.customviews.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewOSBold extends TextView {

    public TextViewOSBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setType(context);
    }

    public TextViewOSBold(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public TextViewOSBold(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/bold-font.ttf"));
    }
}
