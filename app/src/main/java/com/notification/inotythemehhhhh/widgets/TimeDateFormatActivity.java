package com.notification.inotythemehhhhh.widgets;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.notification.inotythemehhhhh.R;
import com.notification.inotythemehhhhh.myservices.NotifyService;
import com.notification.inotythemehhhhh.mycenterutils.SharedPreferencesUtil;
import com.notification.inotythemehhhhh.customviews.widgets.MyCheckBoxView;


public class TimeDateFormatActivity extends AppCompatActivity implements View.OnClickListener {
    private MyCheckBoxView ckbActivityLockFormat12h;
    private MyCheckBoxView ckbActivityLockFormat24h;
    private ImageView imvActivityClockFormatBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_format);
        initData();

    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ckbActivityLockFormat12h = (MyCheckBoxView) findViewById(R.id.ckb_activity_lock_format__12h);
        ckbActivityLockFormat24h = (MyCheckBoxView) findViewById(R.id.ckb_activity_lock_format__24h);
        imvActivityClockFormatBack = (ImageView) findViewById(R.id.imv_activity_clock_format__back);
        ckbActivityLockFormat12h.setOnClickListener(this);
        ckbActivityLockFormat24h.setOnClickListener(this);
        imvActivityClockFormatBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ckbActivityLockFormat12h) {
            SharedPreferencesUtil.set24hFormat(this, false);
            ckbActivityLockFormat12h.setChecked(true);
            ckbActivityLockFormat24h.setChecked(false);
            NotifyService notifyService = NotifyService.getInstance();
            if (notifyService != null) {
                notifyService.updateFormatTime();
            }
        } else if (v == ckbActivityLockFormat24h) {
            SharedPreferencesUtil.set24hFormat(this, true);
            ckbActivityLockFormat12h.setChecked(false);
            ckbActivityLockFormat24h.setChecked(true);
            NotifyService notifyService = NotifyService.getInstance();
            if (notifyService != null) {
                notifyService.updateFormatTime();
            }
        } else if (v == imvActivityClockFormatBack) {
            finish();
            overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
        }

    }

    private void initData() {
        if (SharedPreferencesUtil.is24hFormat(this)) {
            ckbActivityLockFormat12h.setChecked(false);
            ckbActivityLockFormat24h.setChecked(true);
        } else {
            ckbActivityLockFormat12h.setChecked(true);
            ckbActivityLockFormat24h.setChecked(false);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
    }
}
