package com.notification.notyos10.activitys;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.notification.notyos10.R;
import com.notification.notyos10.adapters.ListBackgroundAdapter;
import com.notification.notyos10.animations.base.BaseCreative;
import com.notification.notyos10.animations.base.BaseObjectAnimator;
import com.notification.notyos10.connectionpicasa.PrefManager;
import com.notification.notyos10.constants.Const;
import com.notification.notyos10.customviews.widgets.RippleOSButton;
import com.notification.notyos10.myaplication.MyAplication;
import com.notification.notyos10.objects.WallpaperOnline;
import com.notification.notyos10.services.NotifyService;
import com.notification.notyos10.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SetBackgroundActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    private TextView textDefault;
    private RecyclerView rcvImage;
    private SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    private TextView redToolTip, greenToolTip, blueToolTip;
    private int red, green, blue, alpha;
    private Rect thumbRect;
    private View colorView;
    private NotifyService mService;
    private LinearLayout llChangColor;
    private TextView txtTitleColor;
    private ImageView imgArrow;
    private RippleOSButton btnGetPhotoFromGallery;
    boolean thefirst;
    private PrefManager pref;
    private List<WallpaperOnline> photosList;
    private static final String TAG_FEED = "feed", TAG_ENTRY = "entry",
            TAG_MEDIA_GROUP = "media$group",
            TAG_MEDIA_CONTENT = "media$content", TAG_IMG_URL = "url",
            TAG_IMG_WIDTH = "width", TAG_IMG_HEIGHT = "height", TAG_ID = "id",
            TAG_T = "$t";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_background);
        try {
            mService = NotifyService.getInstance();
            mSetBackgroundActivity = this;
            initView();
            initEventView();
            String kindBackground = "default";
            try {
                kindBackground = SharedPreferencesUtil.getKindBackground(this);
            } catch (NullPointerException e) {
                kindBackground = "default";
            }
            if (kindBackground.equals("background_app_photo")){
                int levelBllur = SharedPreferencesUtil.getLevelBlurred(this);
                String pathFile = SharedPreferencesUtil.getPathBAckground(this);
                mService.updateBackgroundNotifyImage(pathFile, levelBllur);
            }else
            if (kindBackground.equals("background_your_photo")){
                try {
                    Uri imageUri = Uri.parse(SharedPreferencesUtil.getUriBackground(SetBackgroundActivity.this));
                    int levelBllur = SharedPreferencesUtil.getLevelBlurred(SetBackgroundActivity.this);
                    mService.updateBackgroundNotifyImage(imageUri,levelBllur);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }else
            if (kindBackground.equals("default")){
                mService.updateBackgroundNotifyDefault();
            }else
            if (kindBackground.equals("color")){
                redSeekBar.setProgress(red);
                greenSeekBar.setProgress(green);
                blueSeekBar.setProgress(blue);
                mService.updateBackgroundNotify(Color.rgb(red,green,blue));
                colorView.setBackgroundColor(Color.argb(alpha, red, green, blue));
            }

            initDataBackground();
        } catch (Exception e) {
            e.printStackTrace();
            finish();
            Toast.makeText(this,"can't show this image, try again!",Toast.LENGTH_SHORT).show();
        }
    }

    private void initView(){

        rcvImage = (RecyclerView) findViewById(R.id.rcv_image);
        rcvImage.setHasFixedSize(true);
        rcvImage.setLayoutManager(new GridLayoutManager(this, 2));
        textDefault = (TextView) findViewById(R.id.bgr_default_header);
        redSeekBar = (SeekBar) findViewById(R.id.redSeekBar);
        greenSeekBar = (SeekBar) findViewById(R.id.greenSeekBar);
        blueSeekBar = (SeekBar) findViewById(R.id.blueSeekBar);
        redToolTip = (TextView) findViewById(R.id.redToolTip);
        greenToolTip = (TextView) findViewById(R.id.greenToolTip);
        blueToolTip = (TextView) findViewById(R.id.blueToolTip);
        colorView = findViewById(R.id.colorView);
        llChangColor = (LinearLayout) findViewById(R.id.ll_change_color);
        txtTitleColor = (TextView) findViewById(R.id.txt_title_color);
        imgArrow = (ImageView) findViewById(R.id.img_activity_set_background__arrow_color);
        imgArrow.setImageResource(R.drawable.arrow_up);
        btnGetPhotoFromGallery = (RippleOSButton) findViewById(R.id.btn_get_background_from_gallery);
        btnGetPhotoFromGallery.setVisibility(View.INVISIBLE);
        if (isNetworkConnected(this) == false){
            btnGetPhotoFromGallery.setVisibility(View.VISIBLE);
        }

        redSeekBar.setOnSeekBarChangeListener(this);
        greenSeekBar.setOnSeekBarChangeListener(this);
        blueSeekBar.setOnSeekBarChangeListener(this);
        red = SharedPreferencesUtil.getRedColor(this);
        green = SharedPreferencesUtil.getGreenColor(this);
        blue = SharedPreferencesUtil.getBlueColor(this);
        alpha = SharedPreferencesUtil.getAlphaColor(this);
    }

    private void initEventView(){
        btnGetPhotoFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 101);
            }
        });

        textDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                red = 0;
                green = 0;
                blue = 0;
                alpha = 0;
                redSeekBar.setProgress(0);
                greenSeekBar.setProgress(0);
                blueSeekBar.setProgress(0);
                SharedPreferencesUtil.setRedColor(SetBackgroundActivity.this,0);
                SharedPreferencesUtil.setGreenColor(SetBackgroundActivity.this,0);
                SharedPreferencesUtil.setBlueColor(SetBackgroundActivity.this,0);
                SharedPreferencesUtil.setAlphaColor(SetBackgroundActivity.this,0);
                mService.updateBackgroundNotifyDefault();
                colorView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                SharedPreferencesUtil.setKindBackground(SetBackgroundActivity.this,"default");
                Toast.makeText(SetBackgroundActivity.this,getResources().getString(R.string.done_default),Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private boolean isScrollRcv;
    private void initDataBackground(){
        pref = new PrefManager(this);
        photosList = new ArrayList<>();
        String url = null;
        url = Const.URL_ALBUM_PHOTOS.replace("_PICASA_USER_",
                pref.getGoogleUserName()).replace("_ALBUM_ID_",
                "6266184875865003345");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        // Parsing the json response
                        try {
                            JSONArray entry = response.getJSONObject(TAG_FEED)
                                    .getJSONArray(TAG_ENTRY);
                            for (int i = 0; i < entry.length(); i++) {
                                JSONObject photoObj = (JSONObject) entry.get(i);
                                JSONArray mediacontentArry = photoObj
                                        .getJSONObject(TAG_MEDIA_GROUP)
                                        .getJSONArray(TAG_MEDIA_CONTENT);
                                if (mediacontentArry.length() > 0) {
                                    JSONObject mediaObj = (JSONObject) mediacontentArry
                                            .get(0);

                                    String url = mediaObj
                                            .getString(TAG_IMG_URL);

                                    String photoJson = photoObj.getJSONObject(
                                            TAG_ID).getString(TAG_T)
                                            + "&imgmax=d";

                                    int width = mediaObj.getInt(TAG_IMG_WIDTH);
                                    int height = mediaObj
                                            .getInt(TAG_IMG_HEIGHT);

                                    WallpaperOnline p = new WallpaperOnline(photoJson, url, width,
                                            height);
                                    photosList.add(p);
                                    Log.d("test10", "Photo: " + url + ", w: "
                                            + width + ", h: " + height);
                                }
                            }
                            ListBackgroundAdapter mAdapterListBackground = new ListBackgroundAdapter(SetBackgroundActivity.this, photosList);
                            mAdapterListBackground.listener(new ListBackgroundAdapter.CommucationListBackgroundAdapter() {
                                @Override
                                public void clickItem(int positition) {
                                    Intent intent = new Intent(SetBackgroundActivity.this, CustomBackgroundActicvity.class);
                                    intent.putExtra("IDBACKGROUND", photosList.get(positition).getUrl());
                                    startActivity(intent);
                                }
                            });
                            thefirst = false;
                            rcvImage.setAdapter(mAdapterListBackground);
                            rcvImage.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                    isScrollRcv = false;
                                }

                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    if (dy > 0 && !thefirst) {
                                        isRunning = true;
                                        isScrollRcv = true;
                                        btnGetPhotoFromGallery.setVisibility(View.VISIBLE);
                                        BaseCreative baseCreative = new BaseCreative();
                                        ObjectAnimator objectAnimatorx = ObjectAnimator.ofFloat(btnGetPhotoFromGallery, BaseObjectAnimator.SCALE_X, 0, 1);
                                        ObjectAnimator objectAnimatory = ObjectAnimator.ofFloat(btnGetPhotoFromGallery, BaseObjectAnimator.SCALE_Y, 0, 1);
                                        ObjectAnimator objectAnimatorarrow = ObjectAnimator.ofFloat(imgArrow,BaseObjectAnimator.ROTATION,0,180);
                                        baseCreative.addAnimator(objectAnimatorx);
                                        baseCreative.addAnimator(objectAnimatory);
                                        baseCreative.addAnimator(objectAnimatorarrow);
                                        baseCreative.setDuration(200);
                                        baseCreative.startAnimationTogether();
                                        baseCreative.addListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {
                                                thefirst = true;
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        });
                                        llChangColor.startAnimation(new MyScaler(1.0f, 1.0f, 1.0f, 0.0f, 200, llChangColor, true));

                                    }
                                }
                            });
                            txtTitleColor.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (isScrollRcv) return;
                                    if (isRunning) {
                                        Log.e("test_animation", " isRunning true");
                                        return;
                                    }
                                    Log.e("test_animation", " isRunning false");
                                    if (thefirst ){
                                        llChangColor.startAnimation(new MyExpand(1.0f, 1.0f, 0.0f, 1.0f, 200, llChangColor, true));
                                        BaseCreative baseCreative = new BaseCreative();
                                        ObjectAnimator objectAnimatorx = ObjectAnimator.ofFloat(btnGetPhotoFromGallery, BaseObjectAnimator.SCALE_X, 1, 0);
                                        ObjectAnimator objectAnimatory = ObjectAnimator.ofFloat(btnGetPhotoFromGallery, BaseObjectAnimator.SCALE_Y, 1, 0);
                                        ObjectAnimator objectAnimatorarrow = ObjectAnimator.ofFloat(imgArrow,BaseObjectAnimator.ROTATION,-180,0);
                                        baseCreative.addAnimator(objectAnimatorx);
                                        baseCreative.addAnimator(objectAnimatory);
                                        baseCreative.addAnimator(objectAnimatorarrow);
                                        baseCreative.setDuration(200);
                                        baseCreative.startAnimationTogether();
                                        thefirst = false;
                                    }else{
                                        llChangColor.startAnimation(new MyScaler(1.0f, 1.0f, 1.0f, 0.0f, 200, llChangColor, true));
                                        thefirst = true;
                                        btnGetPhotoFromGallery.setVisibility(View.VISIBLE);
                                        BaseCreative baseCreative = new BaseCreative();
                                        ObjectAnimator objectAnimatorx = ObjectAnimator.ofFloat(btnGetPhotoFromGallery, BaseObjectAnimator.SCALE_X, 0, 1);
                                        ObjectAnimator objectAnimatory = ObjectAnimator.ofFloat(btnGetPhotoFromGallery, BaseObjectAnimator.SCALE_Y, 0, 1);
                                        ObjectAnimator objectAnimatorarrow = ObjectAnimator.ofFloat(imgArrow,BaseObjectAnimator.ROTATION,0,180);
                                        baseCreative.addAnimator(objectAnimatorx);
                                        baseCreative.addAnimator(objectAnimatory);
                                        baseCreative.addAnimator(objectAnimatorarrow);
                                        baseCreative.setDuration(200);
                                        baseCreative.startAnimationTogether();
                                    }

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );
        MyAplication.getInstance().getRequestQueue().getCache().remove(url);
        jsonObjectRequest.setShouldCache(false);
        MyAplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }
    private static SetBackgroundActivity mSetBackgroundActivity;
    public static SetBackgroundActivity getInstance(){
        return mSetBackgroundActivity;
    }
    public void saveKindBackground(String kind){
        SharedPreferencesUtil.setKindBackground(SetBackgroundActivity.this, kind);
        if (kind.equals("background_app_photo")){
            SharedPreferencesUtil.setBlurLevel(SetBackgroundActivity.this, Const.levelBlur);
            SharedPreferencesUtil.setPathBackground(SetBackgroundActivity.this, Const.encodeBitmap);
        }else
            if (kind.equals("background_your_photo")){
//                SharedPreferencesUtil.setUriBackGround(SetBackgroundActivity.this, Const.uriPhoto.toString());
//                SharedPreferencesUtil.setBlurLevel(SetBackgroundActivity.this,Const.levelBlur);
                SharedPreferencesUtil.setBlurLevel(SetBackgroundActivity.this, Const.levelBlur);
                SharedPreferencesUtil.setPathBackground(SetBackgroundActivity.this, Const.encodeBitmap);
            }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (data!= null){
                Intent intent = new Intent(SetBackgroundActivity.this, CustomBackgroundActicvity.class);
                Const.uriPhoto = data.getData();
                intent.putExtra("URI", true);
                startActivity(intent);
            }
        }
    }

    public class MyScaler extends ScaleAnimation {
        private View mView;
        private LinearLayout.LayoutParams mLayoutParams;
        private int mMarginBottomFromY, mMarginBottomToY;
        private boolean mVanishAfter = false;
        public MyScaler(float fromX, float toX, float fromY, float toY, int duration, View view,
                        boolean vanishAfter) {
            super(fromX, toX, fromY, toY);
            setDuration(duration);
            mView = view;
            mVanishAfter = vanishAfter;
            mLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            int height = mView.getHeight();
            mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin - height;
            mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) - height;
            saveBottomFromY = mMarginBottomFromY;
            saveBottomToY = mMarginBottomToY;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f) {
                isRunning = true;
                int newMarginBottom = mMarginBottomFromY
                        + (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime);
                mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,
                        mLayoutParams.rightMargin, newMarginBottom);
                mView.getParent().requestLayout();
            } else if (mVanishAfter) {
                mView.setVisibility(View.GONE);
                isRunning = false;
            }
        }

    }

    private int saveBottomFromY;
    private int saveBottomToY;
    private boolean isRunning;

    public class MyExpand extends ScaleAnimation {

        private View mView;

        private LinearLayout.LayoutParams mLayoutParams;

        private int mMarginBottomFromY, mMarginBottomToY;

        private boolean mVanishAfter = false;

        public MyExpand(float fromX, float toX, float fromY, float toY, int duration, View view,
                        boolean vanishAfter) {
            super(fromX, toX, fromY, toY);
            setDuration(duration);
            mView = view;
            mVanishAfter = vanishAfter;
            mLayoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            int height = mView.getHeight();
            mMarginBottomFromY = (int) (height * fromY) + mLayoutParams.bottomMargin - height;
            mMarginBottomToY = (int) (0 - ((height * toY) + mLayoutParams.bottomMargin)) - height;
            mMarginBottomFromY = saveBottomToY;
            mMarginBottomToY = saveBottomFromY;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mView.setVisibility(View.VISIBLE);
            if (interpolatedTime < 1.0f) {
                isRunning = true;
                int newMarginBottom = mMarginBottomFromY + (int) ((mMarginBottomToY - mMarginBottomFromY) * interpolatedTime);
                Log.e("testanim","expand : " + newMarginBottom);
                mLayoutParams.setMargins(mLayoutParams.leftMargin, mLayoutParams.topMargin,
                        mLayoutParams.rightMargin, newMarginBottom);
                mView.getParent().requestLayout();
            } else if (mVanishAfter) {
                isRunning = false;
            }
        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.redSeekBar) {
            red = progress;
            Log.e("test2", "progress red : " + progress);
            thumbRect = seekBar.getThumb().getBounds();
            if (progress < 10)
                redToolTip.setText("  " + red);
            else if (progress < 100)
                redToolTip.setText(" " + red);
            else
                redToolTip.setText(red + "");

        } else if (seekBar.getId() == R.id.greenSeekBar) {

            green = progress;
            thumbRect = seekBar.getThumb().getBounds();
            if (progress < 10)
                greenToolTip.setText("  " + green);
            else if (progress < 100)
                greenToolTip.setText(" " + green);
            else
                greenToolTip.setText(green + "");

        } else if (seekBar.getId() == R.id.blueSeekBar) {

            blue = progress;
            thumbRect = seekBar.getThumb().getBounds();
            if (progress < 10)
                blueToolTip.setText("  " + blue);
            else if (progress < 100)
                blueToolTip.setText(" " + blue);
            else
                blueToolTip.setText(blue + "");
        }

        if (red != 0 || green != 0 || blue != 0)
            alpha = 255;

        colorView.setBackgroundColor(Color.argb(alpha, red, green, blue));
        mService.updateBackgroundNotify(Color.argb(alpha, red, green, blue));


        SharedPreferencesUtil.setKindBackground(SetBackgroundActivity.this,"color");
        String kindBackground = SharedPreferencesUtil.getKindBackground(this);
        if (kindBackground.equals("background_app_photo")){
         //   int idDrawable = SharedPreferencesUtil.getIdBAckground(SetBackgroundActivity.this, R.drawable.bground);
            int levelBlur = SharedPreferencesUtil.getLevelBlurred(SetBackgroundActivity.this);
      //      mService.updateBackgroundNotifyImage(idDrawable,levelBlur);
        }else
        if (kindBackground.equals("background_your_photo")){
            try {
                Uri imageUri = Uri.parse(SharedPreferencesUtil.getUriBackground(SetBackgroundActivity.this));
                int levelBllur = SharedPreferencesUtil.getLevelBlurred(SetBackgroundActivity.this);
                mService.updateBackgroundNotifyImage(imageUri,levelBllur);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }else
        if (kindBackground.equals("default")){
            mService.updateBackgroundNotifyDefault();
        }else
        if (kindBackground.equals("color"))
            mService.updateBackgroundNotify(Color.rgb(red,green,blue));
        if (red==0 && green==0 && blue==0 && alpha==0){
            mService.updateBackgroundNotifyDefault();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        SharedPreferencesUtil.setRedColor(SetBackgroundActivity.this,redSeekBar.getProgress());
        SharedPreferencesUtil.setGreenColor(SetBackgroundActivity.this,greenSeekBar.getProgress());
        SharedPreferencesUtil.setBlueColor(SetBackgroundActivity.this,blueSeekBar.getProgress());
        SharedPreferencesUtil.setKindBackground(this,"color");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferencesUtil.setRedColor(SetBackgroundActivity.this,redSeekBar.getProgress());
        SharedPreferencesUtil.setGreenColor(SetBackgroundActivity.this,greenSeekBar.getProgress());
        SharedPreferencesUtil.setBlueColor(SetBackgroundActivity.this,blueSeekBar.getProgress());
    }
}
