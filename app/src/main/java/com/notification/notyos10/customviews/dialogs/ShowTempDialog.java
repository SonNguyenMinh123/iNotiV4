package com.notification.notyos10.customviews.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.notification.notyos10.R;
import com.notification.notyos10.customviews.widgets.MyCheckBoxView;
import com.notification.notyos10.services.NotifyService;
import com.notification.notyos10.utils.SharedPreferencesUtil;

/**
 * Created by Administrator on 1/11/2017.
 */

public class ShowTempDialog extends Dialog {
    private Context context;
    private MyCheckBoxView ckbActivityFormat12h;
    private MyCheckBoxView ckbActivityFormat24h;

    public ShowTempDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_clock_format);

        initDialog();
    }

    private void initDialog() {
        // init
        ckbActivityFormat12h = (MyCheckBoxView) findViewById(R.id.ckb_activity_lock_format__12h);
        ckbActivityFormat24h = (MyCheckBoxView) findViewById(R.id.ckb_activity_lock_format__24h);

        if (SharedPreferencesUtil.is24hFormat(context)) {
            ckbActivityFormat12h.setChecked(false);
            ckbActivityFormat24h.setChecked(true);
        } else {
            ckbActivityFormat12h.setChecked(true);
            ckbActivityFormat24h.setChecked(false);
        }
        initEvent();
    }

    private void initEvent() {
        ckbActivityFormat12h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.set24hFormat(context, false);
                ckbActivityFormat12h.setChecked(true);
                ckbActivityFormat24h.setChecked(false);
                NotifyService notifyService = NotifyService.getInstance();
                if (notifyService != null) {
                    notifyService.updateFormatTime();
                }
            }
        });
        ckbActivityFormat24h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.set24hFormat(context, true);
                ckbActivityFormat12h.setChecked(false);
                ckbActivityFormat24h.setChecked(true);
                NotifyService notifyService = NotifyService.getInstance();
                if (notifyService != null) {
                    notifyService.updateFormatTime();
                }
            }
        });
    }
}
