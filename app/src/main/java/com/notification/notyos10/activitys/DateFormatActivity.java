package com.notification.notyos10.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.notification.notyos10.R;
import com.notification.notyos10.customviews.widgets.MyCheckBoxView;
import com.notification.notyos10.services.NotifyService;
import com.notification.notyos10.utils.SharedPreferencesUtil;


public class DateFormatActivity extends AppCompatActivity implements View.OnClickListener {
    private MyCheckBoxView ckbActivityFormat12h;
    private MyCheckBoxView ckbActivityFormat24h;
//    private ImageView imvBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_clock_format);
        initData();
    }
    private void initData() {
        if (SharedPreferencesUtil.is24hFormat(this)) {
            ckbActivityFormat12h.setChecked(false);
            ckbActivityFormat24h.setChecked(true);
        } else {
            ckbActivityFormat12h.setChecked(true);
            ckbActivityFormat24h.setChecked(false);
        }

    }
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ckbActivityFormat12h = (MyCheckBoxView) findViewById(R.id.ckb_activity_lock_format__12h);
        ckbActivityFormat24h = (MyCheckBoxView) findViewById(R.id.ckb_activity_lock_format__24h);
//        imvBack = (ImageView) findViewById(R.id.imv_activity_clock_format__back);
        ckbActivityFormat12h.setOnClickListener(this);
        ckbActivityFormat24h.setOnClickListener(this);
//        imvBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == ckbActivityFormat12h) {
            SharedPreferencesUtil.set24hFormat(this, false);
            ckbActivityFormat12h.setChecked(true);
            ckbActivityFormat24h.setChecked(false);
            NotifyService notifyService = NotifyService.getInstance();
            if (notifyService != null) {
                notifyService.updateFormatTime();
            }
        } else if (v == ckbActivityFormat24h) {
            SharedPreferencesUtil.set24hFormat(this, true);
            ckbActivityFormat12h.setChecked(false);
            ckbActivityFormat24h.setChecked(true);
            NotifyService notifyService = NotifyService.getInstance();
            if (notifyService != null) {
                notifyService.updateFormatTime();
            }
        }
//        else if (v == imvBack) {
//            finish();
//            overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
//        }

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_low_left_to_center, R.anim.anim_fast_center_to_right);
    }
}
