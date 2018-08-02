package com.notification.notyos10.customviews.others;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.notification.notyos10.R;
import com.notification.notyos10.customviews.widgets.TextViewOSNormal;


public class ItemNotyTodayView extends LinearLayout {
    private ImageView imvItemNotyTodayViewIcon;
    private TextViewOSNormal txvItemNotyTodayViewTitle;
    private LinearLayout lnlItemNotyTodayViewAddItem;
    private String mTitle;
    private ImageButton btnClose;
    private CommucationNotyTodayView mCommucationNotyTodayView;

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
            btnClose = (ImageButton) findViewById(R.id.btn_item_noty_today_view_close);
            btnClose.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommucationNotyTodayView.clickClose();
                }
            });
        }
    }

    public void setIconDrawable(Drawable drawable) {
        imvItemNotyTodayViewIcon.setImageDrawable(drawable);
    }

    public void setTitle(String title) {
        mTitle = title;
        txvItemNotyTodayViewTitle.setText(title);
    }

    public void setTitleColor(){
        try {
            txvItemNotyTodayViewTitle.setTextColor(Color.WHITE);
        }catch (NullPointerException e){

        }
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

    public void listenerNotyTodayView(CommucationNotyTodayView commucationNotyTodayView){
        mCommucationNotyTodayView = commucationNotyTodayView;
    }

    public interface CommucationNotyTodayView{
        void clickClose();
    }
}
