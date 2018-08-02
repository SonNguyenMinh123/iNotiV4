package com.notification.notyos10.customviews.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RadioButton;


public class RadioButtonIos extends RadioButton {
    public RadioButtonIos(Context context) {
        super(context);
        setType(context);
    }

    public RadioButtonIos(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public RadioButtonIos(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/bold-font.ttf"));
//        this.setShadowLayer(1.5f, 5, 5, Color.parseColor("#80000000"));
    }
}
