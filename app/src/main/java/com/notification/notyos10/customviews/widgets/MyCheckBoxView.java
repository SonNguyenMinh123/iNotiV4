package com.notification.notyos10.customviews.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.notification.notyos10.R;


public class MyCheckBoxView extends RelativeLayout {
    private TextViewOSNormal txvPartialCheckboxTitle;
    private ImageView imvPartialCheckboxChecked;

    public MyCheckBoxView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public MyCheckBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.partial_checkbox, this);
            if (attrs != null) {
                TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ItemcheckBox);
                String strText = typedArray.getString(R.styleable.ItemcheckBox_textFormat);
                txvPartialCheckboxTitle = (TextViewOSNormal) findViewById(R.id.txv_partial_checkbox__title);
                imvPartialCheckboxChecked = (ImageView) findViewById(R.id.imv_partial_checkbox__checked);
                txvPartialCheckboxTitle.setText(strText);
            }
        }
    }

    public void setChecked(boolean isChecked) {
        if (isChecked) {
            imvPartialCheckboxChecked.setVisibility(View.VISIBLE);
        } else {
            imvPartialCheckboxChecked.setVisibility(View.INVISIBLE);
        }
    }
}
