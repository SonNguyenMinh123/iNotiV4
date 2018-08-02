package com.notification.inotythemehhhhh.threadsapp;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;

import com.notification.inotythemehhhhh.R;
import com.notification.inotythemehhhhh.objects.NotifyEntity;
import com.notification.inotythemehhhhh.myservices.NotificationMonitorService;
import com.notification.inotythemehhhhh.mycenterutils.NotificationUtil;

import java.util.ArrayList;


public class LoadNotificationTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;
    private OnNotyLoadListener mOnNotyLoadListener;
    private ArrayList<NotifyEntity> mNotyModelRightArrayList = new ArrayList<>();
    private ArrayList<NotifyEntity> mNotyModelLeftArrayList = new ArrayList<>();

    public LoadNotificationTask(Context context, OnNotyLoadListener onNotyLoadListener) {
        mContext = context;
        mOnNotyLoadListener = onNotyLoadListener;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(Void... params) {
        if (NotificationUtil.isEnabled(mContext)) {
            if (NotificationMonitorService.getCurrentNotifications() == null) {
                return null;
            }
            StatusBarNotification[] currentNos = NotificationMonitorService.getCurrentNotifications();
            if (currentNos != null) {
                PackageManager pm = mContext.getApplicationContext().getPackageManager();
                long headerId = 0;
                Var:
                for (int i = 0; i < currentNos.length; i++) {
                    NotifyEntity notyModel = new NotifyEntity();
                    Bundle bundle = null;
                    bundle = currentNos[i].getNotification().extras;
                    if (bundle != null) {
                        ApplicationInfo ai;
                        try {
                            ai = pm.getApplicationInfo(currentNos[i].getPackageName(), 0);
                        } catch (final PackageManager.NameNotFoundException e) {
                            ai = null;
                        }
                        String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
                        String title = bundle.getString(currentNos[i].getNotification().EXTRA_TITLE);
                        String content = bundle.getString(currentNos[i].getNotification().EXTRA_TEXT);
                        int resIdIdIcon = bundle.getInt(currentNos[i].getNotification().EXTRA_SMALL_ICON);
                        int resNormalIcon = bundle.getInt(currentNos[i].getNotification().EXTRA_LARGE_ICON);
                        Bitmap bmpLargeIcon = currentNos[i].getNotification().largeIcon;
                        PendingIntent pendingIntentent = currentNos[i].getNotification().contentIntent;
                        Drawable iconDrawable = ai.loadIcon(pm);
                        long posTime = currentNos[i].getPostTime();

                        if (title != null && applicationName != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                                notyModel.setKey(currentNos[i].getKey());
                            }
                            if (currentNos[i].getNotification().actions != null && currentNos[i].getNotification().actions.length > 0 || content == null) {
                                notyModel.setAction(true);
                            } else {
                                notyModel.setAction(false);
                            }
                            notyModel.setPosition(i);
                            notyModel.setId(currentNos[i].getId());
                            notyModel.setAppName(applicationName);
                            notyModel.setPackageName(currentNos[i].getPackageName());
                            notyModel.setTag(currentNos[i].getTag());
                            notyModel.setFlag(currentNos[i].getNotification().flags);
                            notyModel.setTitle(title);
                            notyModel.setContent(content);
                            notyModel.setSmallIcon(ai.icon);
                            notyModel.setDrawableIcon(iconDrawable);
                            if (currentNos[i].getPackageName().equalsIgnoreCase(mContext.getPackageName())) {
                                notyModel.setAppName("System");
                                notyModel.setDrawableIcon(mContext.getResources().getDrawable(R.drawable.ic_star));
                            }
                            notyModel.setNormalIcon(resNormalIcon);
                            notyModel.setBigIcon(bmpLargeIcon);
                            notyModel.setPostTime(posTime);
                            notyModel.setPendingIntent(pendingIntentent);
                            notyModel.setHeaderId(headerId++);
                            for (int j = 0; j < mNotyModelRightArrayList.size(); j++) {
                                if (i == j) {
                                    continue;
                                }
                                if (notyModel.getPackageName().equals(mNotyModelRightArrayList.get(j).getPackageName())) {
                                    notyModel.setHeaderId(mNotyModelRightArrayList.get(j).getHeaderId());
                                    int index = j;
                                    if (index < mNotyModelRightArrayList.size()) {
                                        index += 1;
                                    }
                                    mNotyModelRightArrayList.add(index, notyModel);
                                    continue Var;
                                }
                            }
                            mNotyModelRightArrayList.add(notyModel);
                        } else if (currentNos[i].getNotification().contentView != null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                                notyModel.setKey(currentNos[i].getKey());
                            }
                            if (currentNos[i].getNotification().actions != null && currentNos[i].getNotification().actions.length > 0) {
                                notyModel.setAction(true);
                            } else {
                                notyModel.setAction(false);
                            }
                            notyModel.setPosition(i);
                            notyModel.setId(currentNos[i].getId());
                            notyModel.setPackageName(currentNos[i].getPackageName());
                            notyModel.setTag(currentNos[i].getTag());
                            notyModel.setFlag(currentNos[i].getNotification().flags);
                            notyModel.setContentView(currentNos[i].getNotification().contentView);
                            notyModel.setAppName(applicationName);
                            notyModel.setSmallIcon(resIdIdIcon);
                            notyModel.setNormalIcon(resNormalIcon);
                            notyModel.setPendingIntent(pendingIntentent);
                            notyModel.setBigIcon(bmpLargeIcon);
                            notyModel.setDrawableIcon(iconDrawable);
                            notyModel.setPostTime(posTime);
                            mNotyModelLeftArrayList.add(notyModel);
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mOnNotyLoadListener.onLoadNoty(mNotyModelLeftArrayList, mNotyModelRightArrayList);
    }

    public interface OnNotyLoadListener {
        void onLoadNoty(ArrayList<NotifyEntity> listNotyLeft, ArrayList<NotifyEntity> listNotyRight);
    }
}
