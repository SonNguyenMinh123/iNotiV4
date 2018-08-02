package com.notification.inotythemehhhhh.customviews.others;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.notification.inotythemehhhhh.R;
import com.notification.inotythemehhhhh.customviews.widgets.TextViewOSNormal;


public class ItemNotyTodayView extends LinearLayout {
    private ImageView imvItemNotyTodayViewIcon;
    private TextViewOSNormal txvItemNotyTodayViewTitle;
    private LinearLayout lnlItemNotyTodayViewAddItem;
    private String mTitle;

    public ItemNotyTodayView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public ItemNotyTodayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.item_noty_today_view, this);
            imvItemNotyTodayViewIcon = (ImageView) findViewById(R.id.imv_item_noty_today_view__icon);
            txvItemNotyTodayViewTitle = (TextViewOSNormal) findViewById(R.id.txv_item_noty_today_view__title);
            lnlItemNotyTodayViewAddItem = (LinearLayout) findViewById(R.id.lnl_item_noty_today_view__add_item);
        }
    }

    public void setIconDrawable(Drawable drawable) {
        imvItemNotyTodayViewIcon.setImageDrawable(drawable);
    }

    public void setTitle(String title) {
        mTitle = title;
        txvItemNotyTodayViewTitle.setText(title);
    }

    public String getTitle() {
        return mTitle;
    }

    public void addView(View view) {
        lnlItemNotyTodayViewAddItem.addView(view);
    }

    public void addLine(View view) {
        lnlItemNotyTodayViewAddItem.addView(view);
    }
}
