package com.notification.inotythemehhhhh.objects;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;


public class NotifyEntity {
    private int mId;
    private int mPosition;
    private int mSmallIcon;
    private int mNormalIcon;
    private long mFlag;
    private String mKey;
    private String mTitle;
    private String mContent;
    private String mTimeAgo;
    private String mAppName;
    private PendingIntent mPendingIntent;
    private RemoteViews mContentView;
    private Bitmap mBigIcon;
    private Drawable mDrawableIcon;
    private long mPostTime;
    private String mTag;
    private String mPackageName;
    private boolean mAction;
    private long mHeaderId;

    public long getHeaderId() {
        return mHeaderId;
    }

    public void setHeaderId(long headerId) {
        mHeaderId = headerId;
    }

    public boolean isAction() {
        return mAction;
    }

    public void setAction(boolean action) {
        mAction = action;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    public String getTag() {
        return mTag;
    }

    public void setTag(String tag) {
        mTag = tag;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public long getPostTime() {
        return mPostTime;
    }

    public void setPostTime(long postTime) {
        mPostTime = postTime;
    }

    public Drawable getDrawableIcon() {
        return mDrawableIcon;
    }

    public void setDrawableIcon(Drawable drawableIcon) {
        mDrawableIcon = drawableIcon;
    }

    public Bitmap getBigIcon() {
        return mBigIcon;
    }

    public void setBigIcon(Bitmap bigIcon) {
        mBigIcon = bigIcon;
    }

    public RemoteViews getContentView() {
        return mContentView;
    }

    public void setContentView(RemoteViews contentView) {
        mContentView = contentView;
    }

    public PendingIntent getPendingIntent() {
        return mPendingIntent;
    }

    public void setPendingIntent(PendingIntent pendingIntent) {
        mPendingIntent = pendingIntent;
    }

    public int getSmallIcon() {
        return mSmallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        mSmallIcon = smallIcon;
    }

    public int getNormalIcon() {
        return mNormalIcon;
    }

    public void setNormalIcon(int normalIcon) {
        mNormalIcon = normalIcon;
    }

    public String getTitle() {

        return mTitle;
    }

    public long getFlag() {
        return mFlag;
    }

    public void setFlag(long flag) {
        mFlag = flag;
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getTimeAgo() {
        return mTimeAgo;
    }

    public void setTimeAgo(String timeAgo) {
        mTimeAgo = timeAgo;
    }


    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String appName) {
        mAppName = appName;
    }
}
