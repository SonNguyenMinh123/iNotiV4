package com.notification.notyos10.customviews.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonIos extends Button {
    public ButtonIos(Context context) {
        super(context);
        setType(context);
    }

    public ButtonIos(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public ButtonIos(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/bold-font.ttf"));
//        this.setShadowLayer(1.5f, 5, 5, Color.parseColor("#80000000"));
    }
}
