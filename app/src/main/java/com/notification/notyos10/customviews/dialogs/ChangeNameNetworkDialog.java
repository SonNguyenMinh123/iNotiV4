package com.notification.notyos10.customviews.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.notification.notyos10.R;
import com.notification.notyos10.constants.Const;
import com.notification.notyos10.customviews.widgets.ButtonOSNormal;
import com.notification.notyos10.services.NotifyService;
import com.notification.notyos10.utils.SharedPreferencesUtil;
import com.notification.notyos10.utils.SystemUtil;

/**
 * Created by Administrator on 1/11/2017.
 */

public class ChangeNameNetworkDialog extends Dialog {
    private Context context;
    private EditText edtDialogCustomCarrierNameInput;
    private ButtonOSNormal btnDialogCustomCarrierNameOk;
    private ButtonOSNormal btnDialogCustomCarrierNameCancel;
    private ButtonOSNormal btnDialogCustomCarrierNameDefault;

    public ChangeNameNetworkDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_carrier_name);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        initDialog();
    }

    private void initDialog() {
        edtDialogCustomCarrierNameInput = (EditText) findViewById(R.id.edt_dialog_custom_carrier_name__input);
        btnDialogCustomCarrierNameOk = (ButtonOSNormal) findViewById(R.id.btn_dialog_custom_carrier_name__ok);
        btnDialogCustomCarrierNameCancel = (ButtonOSNormal) findViewById(R.id.btn_dialog_custom_carrier_name__cancel);
        btnDialogCustomCarrierNameDefault = (ButtonOSNormal) findViewById(R.id.btn_dialog_custom_carrier_name__default);

        if (SharedPreferencesUtil.getpreferences(context, Const.TEXT_EDITTEXT_DIALOG_NETWORK).equals("0")) {
            edtDialogCustomCarrierNameInput.setText("");
        } else {
            edtDialogCustomCarrierNameInput.setText(SharedPreferencesUtil.getpreferences(context, Const.TEXT_EDITTEXT_DIALOG_NETWORK));
        }
        initEvent();
    }

    private void initEvent() {
        btnDialogCustomCarrierNameOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyService notifyService = NotifyService.getInstance();
                String carrierName = edtDialogCustomCarrierNameInput.getText().toString();
                SharedPreferencesUtil.savePreferences(context, Const.TEXT_EDITTEXT_DIALOG_NETWORK, carrierName);

                if (carrierName.equals("")) {
                    carrierName = " ";
                }
                SharedPreferencesUtil.setCustomCarrierName(context, carrierName);
                if (notifyService != null) {
                    notifyService.updateCustomCarrierName();
                }
                dismiss();
            }
        });
        //
        btnDialogCustomCarrierNameCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //
        btnDialogCustomCarrierNameDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String carrierName = SystemUtil.getCarrierName(context).toUpperCase();
                edtDialogCustomCarrierNameInput.setText(carrierName);
            }
        });
    }
}
