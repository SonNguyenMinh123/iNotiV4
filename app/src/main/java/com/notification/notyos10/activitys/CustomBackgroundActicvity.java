package com.notification.notyos10.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.notification.notyos10.R;
import com.notification.notyos10.constants.Const;
import com.notification.notyos10.customviews.widgets.RippleOSButton;
import com.notification.notyos10.downloader.ImageDownloader;
import com.notification.notyos10.objects.MyBackground;
import com.notification.notyos10.services.NotifyService;
import com.notification.notyos10.utils.ConvertUtils;
import com.notification.notyos10.utils.SharedPreferencesUtil;
import com.qiushui.blurredview.BlurredView;

import java.io.ByteArrayOutputStream;

import a.a.AdConfig;
import a.a.AdsListener;
import io.realm.Realm;

public class CustomBackgroundActicvity extends Activity {
    private BlurredView mBlurredView;
    private SeekBar mSbBlurLevel;
    private RippleOSButton btnCancle;
    private RippleOSButton btnOK;
    private NotifyService mService;
    private String idDrawable;
    private boolean isLocal;
    private int levelBlur;
    private Bitmap mBitmapDownloaded;
    private ImageDownloader altexImageDownloader;
    private boolean isShowAds = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_background);
        initView();
        AdConfig.setAdListener(new AdsListener() {
            @Override
            public void onDismissed(String s) {
                if (s.equals("background_to_main")) {
                    isShowAds=false;
                    onBackPressed();
                }
            }

            @Override
            public void onError(String s, String s1) {
                if (isShowAds && s.equals("background_to_main")) {
                    isShowAds=false;
                    onBackPressed();
                }
            }

            @Override
            public void onLoaded(String s) {
                if (s.equals("background_to_main")) {
                    isShowAds = true;
                }
            }
        });
        AdConfig.loadAds("background_to_main", this);
        SharedPreferences settings = getSharedPreferences("BLUR_BACKGROUND", 0);
        levelBlur = settings.getInt("BLUR_LEVEL", 100);
        mSbBlurLevel.setProgress(levelBlur);
        mBlurredView.setBlurredLevel(levelBlur);
        Intent intent = getIntent();
        idDrawable = "";
        idDrawable = intent.getStringExtra("IDBACKGROUND");
        isLocal = intent.getBooleanExtra("URI", false);
        altexImageDownloader = new
                ImageDownloader(new ImageDownloader.OnImageLoaderListener() {
            @Override
            public void onError(ImageDownloader.ImageError error) {
            }

            @Override
            public void onProgressChange(int percent) {
            }

            @Override
            public void onComplete(final Bitmap result) {
                Log.e("test_custom", "download thành công");
                mBitmapDownloaded = result;
                if (!isLocal) {
                    try {
                        mBlurredView.setBlurredImg(mBitmapDownloaded);
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        initEventView();

        if (!isLocal) {
            altexImageDownloader.download(idDrawable, true);
        } else {
            Bitmap bitmap = ConvertUtils.loadBitmapFromUri(CustomBackgroundActicvity.this, Const.uriPhoto, null);
            if (bitmap != null)
                try {
                    mBlurredView.setBlurredImg(bitmap);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mService.updateBackgroundNotifyImage(Const.uriPhoto, mSbBlurLevel.getProgress());
                    SetBackgroundActivity setBackgroundActivity = SetBackgroundActivity.getInstance();
                    setBackgroundActivity.saveKindBackground("background_your_photo");
                    Toast.makeText(CustomBackgroundActicvity.this, getResources().getString(R.string.change_background), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });
        }

        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSbBlurLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBlurredView.setBlurredLevel(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences settings = getSharedPreferences("BLUR_BACKGROUND", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("BLUR_LEVEL", seekBar.getProgress());
                editor.apply();
                editor.commit();
            }
        });
    }

    private void initView() {
        mBlurredView = (BlurredView) findViewById(R.id.blur_view_preview);
        btnOK = (RippleOSButton) findViewById(R.id.btn_activity_custom_background_ok);
        btnCancle = (RippleOSButton) findViewById(R.id.btn_activity_custom_background_cancle);
        mSbBlurLevel = (SeekBar) findViewById(R.id.blur_background_level);
        mBlurredView.setVisibility(View.VISIBLE);
        mService = NotifyService.getInstance();
        isLocal = false;
        levelBlur = 100;
    }

    @Override
    public void onBackPressed() {
        if (isShowAds) {
            AdConfig.showAds("background_to_main", CustomBackgroundActicvity.this);
        } else {
            super.onBackPressed();
        }
    }

    private void initEventView() {
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLocal) {
                    /*trường hợp ảnh background là ảnh online*/
                    boolean existed = false;
                    Uri imageUri = Uri.parse(idDrawable);
                    String fileName = imageUri.getLastPathSegment();
                    String strList = SharedPreferencesUtil.getListNameImageDownloaded(CustomBackgroundActicvity.this);
                    String[] names = strList.split("__");
                    if (names.length == 0) existed = false;
                    for (int i = 0; i < names.length; i++) {
                        String strcheck = names[i];
                        if (strcheck.equals(fileName)) {
                            existed = true;
                            break;
                        }
                    }
                    if (!existed) {
                        altexImageDownloader.writeToDisk(CustomBackgroundActicvity.this, idDrawable, "iNoty_");

                        StringBuilder stringBuilder = new StringBuilder(strList);
                        stringBuilder.append("__" + fileName);
                        strList = stringBuilder.toString();
                        SharedPreferencesUtil.setListNameImageDownloaded(CustomBackgroundActicvity.this, strList);
                        Const.levelBlur = mSbBlurLevel.getProgress();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        try {
                            mBitmapDownloaded.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            byte[] b = baos.toByteArray();
                            String encoded = Base64.encodeToString(b, Base64.DEFAULT);
                            Const.encodeBitmap = encoded;
                        /*
                        * lưu lại ảnh và tên ảnh đi kèm
                        * */
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            MyBackground testObjects = realm.createObject(MyBackground.class);
                            testObjects.setName(fileName);
                            testObjects.setContent(encoded);
                            realm.commitTransaction();

                            mService.updateBackgroundNotifyImage(Const.encodeBitmap, Const.levelBlur);
                            SetBackgroundActivity setBackgroundActivity = SetBackgroundActivity.getInstance();
                            setBackgroundActivity.saveKindBackground("background_app_photo");
                            Toast.makeText(CustomBackgroundActicvity.this, getResources().getString(R.string.change_background), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            Const.levelBlur = mSbBlurLevel.getProgress();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            mBitmapDownloaded.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] b = baos.toByteArray();
                            String encoded = Base64.encodeToString(b, Base64.DEFAULT);
                            Const.encodeBitmap = encoded;

                            mService.updateBackgroundNotifyImage(Const.encodeBitmap, Const.levelBlur);
                            SetBackgroundActivity setBackgroundActivity = SetBackgroundActivity.getInstance();
                            setBackgroundActivity.saveKindBackground("background_app_photo");
                            Toast.makeText(CustomBackgroundActicvity.this, getResources().getString(R.string.change_background), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    /*
                    * trường hợp ảnh background là ảnh từ gallery
                    * */
                    Bitmap bitmap = ConvertUtils.loadBitmapFromUri(CustomBackgroundActicvity.this, Const.uriPhoto, null);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] b = baos.toByteArray();
                    String encoded = Base64.encodeToString(b, Base64.DEFAULT);
                    Const.encodeBitmap = encoded;
                    if (bitmap != null)
                        mBlurredView.setBlurredImg(bitmap);
                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mService.updateBackgroundNotifyImage(Const.uriPhoto, mSbBlurLevel.getProgress());
                            SetBackgroundActivity setBackgroundActivity = SetBackgroundActivity.getInstance();
                            setBackgroundActivity.saveKindBackground("background_your_photo");
                            Toast.makeText(CustomBackgroundActicvity.this, getResources().getString(R.string.change_background), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    });
                }
            }
        });
    }

}
