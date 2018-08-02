package com.notification.inotythemehhhhh.customviews.others;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.notification.inotythemehhhhh.R;


public class PartialSignalView extends LinearLayout {

    private ImageView imvPartialSignalStrengthDot1;
    private ImageView imvPartialSignalStrengthDot2;
    private ImageView imvPartialSignalStrengthDot3;
    private ImageView imvPartialSignalStrengthDot4;
    private ImageView imvPartialSignalStrengthDot5;

    public PartialSignalView(Context context) {
        super(context);
        initView(context, null, 0);
    }


    public PartialSignalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.partial_signal_strength, this);
            if (attrs != null) {
                imvPartialSignalStrengthDot1 = (ImageView) findViewById(R.id.imv_partial_signal_strength__dot1);
                imvPartialSignalStrengthDot2 = (ImageView) findViewById(R.id.imv_partial_signal_strength__dot2);
                imvPartialSignalStrengthDot3 = (ImageView) findViewById(R.id.imv_partial_signal_strength__dot3);
                imvPartialSignalStrengthDot4 = (ImageView) findViewById(R.id.imv_partial_signal_strength__dot4);
                imvPartialSignalStrengthDot5 = (ImageView) findViewById(R.id.imv_partial_signal_strength__dot5);
            }
        }
    }

    public void setType(int type) {
        if (type == 0) {
            imvPartialSignalStrengthDot1.setImageResource(R.drawable.ic_signal_empty);
            imvPartialSignalStrengthDot2.setImageResource(R.drawable.ic_signal_empty);
            imvPartialSignalStrengthDot3.setImageResource(R.drawable.ic_signal_empty);
            imvPartialSignalStrengthDot4.setImageResource(R.drawable.ic_signal_empty);
            imvPartialSignalStrengthDot5.setImageResource(R.drawable.ic_signal_empty);
        } else {
            imvPartialSignalStrengthDot1.setImageResource(R.drawable.ic_signal_fill);
            imvPartialSignalStrengthDot2.setImageResource(R.drawable.ic_signal_fill);
            imvPartialSignalStrengthDot3.setImageResource(R.drawable.ic_signal_fill);
            imvPartialSignalStrengthDot4.setImageResource(R.drawable.ic_signal_fill);
            imvPartialSignalStrengthDot5.setImageResource(R.drawable.ic_signal_fill);
        }
    }

}
