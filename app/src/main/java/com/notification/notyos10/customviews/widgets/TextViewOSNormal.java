package com.notification.notyos10.customviews.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

public class TextViewOSNormal extends TextView {

    public TextViewOSNormal(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TextViewOSNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextViewOSNormal(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context){
        setType(context);
    }

    private void setType(Context context) {
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "fonts/bold-font.ttf"));
        setGravity(Gravity.CENTER);
    }
}
