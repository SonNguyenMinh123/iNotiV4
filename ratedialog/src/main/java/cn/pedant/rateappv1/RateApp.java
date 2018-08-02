package cn.pedant.rateappv1;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import cn.pedant.sweetalert.R;
import cn.pedant.sweetalert.SweetAlertDialog;


public class RateApp {
    private CallBackRateApp mCallBack;
    private Context context;

    public RateApp(Context context) {
        this.context = context;

    }

    public static void startRate(final Context context, final Activity activity){
        RateApp rateApp = new RateApp(context);
        rateApp.listener(new CallBackRateApp() {
            @Override
            public void finishApp() {
                activity.finish();
            }
        });
        rateApp.showDialog("Rate our app 5 stars! Thank you so much!","Please!", R.drawable.rate2);


    }

    public static void startRate(Context context){
        RateApp rateApp = new RateApp(context);
        final Activity activity = (Activity) context;
        rateApp.listener(new CallBackRateApp() {
            @Override
            public void finishApp() {
                activity.finish();
            }
        });
        rateApp.showDialog("Rate our app 5 stars! Thank you so much!","Please!", R.drawable.rate2);
    }
    
     public static void startRate(Context context, int idDrawable){
        RateApp rateApp = new RateApp(context);
        final Activity activity = (Activity) context;
        rateApp.listener(new CallBackRateApp() {
            @Override
            public void finishApp() {
                activity.finish();
            }
        });
        rateApp.showDialog("Rate our app 5 stars! Thank you so much!","Please!", idDrawable);
    }

    public static void startRate(Context context, String contentText, String title, int idDrawable){
        RateApp rateApp = new RateApp(context);
        final Activity activity = (Activity) context;
        rateApp.listener(new CallBackRateApp() {
            @Override
            public void finishApp() {
                activity.finish();
            }
        });
        rateApp.showDialog(contentText,title, idDrawable);
    }
    public static void startRate(Context context, String contentText, String title){
        RateApp rateApp = new RateApp(context);
        final Activity activity = (Activity) context;
        rateApp.listener(new CallBackRateApp() {
            @Override
            public void finishApp() {
                activity.finish();
            }
        });
        rateApp.showDialog(contentText,title, R.drawable.rate2);
    }


    public void showDialog(String contentText,String title, int idDrawable) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("FirtInstall", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        int checkFirst = context.getSharedPreferences("FirtInstall", Context.MODE_PRIVATE).getInt("check", 0);
        if (checkFirst == 0) {
            final SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
            dialog.setTitleText(title);
            dialog.setContentText(contentText);
            dialog.setCustomImage(idDrawable);
            dialog.setCancelText(context.getResources().getString(R.string.later));
            dialog.setConfirmText(context.getResources().getString(R.string.yes));
            dialog.showCancelButton(true);
            dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    dialog.dismiss();
                    mCallBack.finishApp();
                }
            });
            dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sDialog) {
                    launchMarket();
                    editor.putInt("check", 1);
                    editor.commit();
                    dialog.dismiss();
                    mCallBack.finishApp();
                }
            })
                    .show();
        }else
            mCallBack.finishApp();
    }

    public void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                return;
            } catch (Exception localException) {

                Toast toast = Toast.makeText(context, "unable to find market app", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    public void listener(CallBackRateApp mCallBack){
        this.mCallBack = mCallBack;
    }

    public interface CallBackRateApp{
        void finishApp();
    }


}
