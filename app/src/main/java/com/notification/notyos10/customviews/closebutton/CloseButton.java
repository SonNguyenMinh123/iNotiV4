package com.notification.notyos10.customviews.closebutton;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.notification.notyos10.R;
import com.notification.notyos10.animations.base.BaseObjectAnimator;

import java.util.ArrayList;


public class CloseButton extends FrameLayout {
    private CloseButtonInterface mCloseButtonInterface;
    private Context context;
    private boolean opened;
    private boolean running;
    private String textOpened;
    private String textClosed;
    private int size = 100;

    public CloseButton(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CloseButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(context, attrs, 0);
        init();
    }

    public CloseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        textClosed = "X";
        textOpened = "Clear";
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.closeButton, defStyleAttr, 0);
        textClosed = array.getString(R.styleable.closeButton_text_close);
        textOpened = array.getString(R.styleable.closeButton_text_opend);
        size = array.getInteger(R.styleable.closeButton_size_button, 100);
    }

    private HalfCircleRight halfCircleRight;
    private HalfCircleLeft halfCircleLeft;
    private TextView textView;
    private TextView textView0;

    public void init() {
        opened = false;
        running = false;
        halfCircleRight = new HalfCircleRight(context);
        halfCircleLeft = new HalfCircleLeft(context);
        LayoutParams params = new LayoutParams(size, size);
        params.gravity = Gravity.RIGHT;
        LayoutParams params1 = new LayoutParams(size, size);
        params1.gravity = Gravity.RIGHT;
        params1.rightMargin = size / 2;

        textView = new TextView(context);
        textView.setWidth(size);
        textView.setHeight(size);
        textView.setTextColor(Color.BLACK);
        textView.setText(textOpened);
        textView.setTextSize(context.getResources().getDimensionPixelSize(R.dimen._4sdp));
        textView.setGravity(Gravity.CENTER);
        textView.setPivotX(size);
        textView.setVisibility(GONE);

        textView0 = new TextView(context);
        textView0.setWidth(size);
        textView0.setHeight(size);
        textView0.setTextColor(Color.BLACK);
        textView0.setGravity(Gravity.CENTER);
        textView0.setTextSize(context.getResources().getDimensionPixelSize(R.dimen._6sdp));
        textView0.setText(textClosed);

        addView(halfCircleRight, params);
        addView(halfCircleLeft, params);
        addView(textView, params1);
        addView(textView0, params);

        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCloseButtonInterface.onClickClosed();
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (animatorSet.isRunning() || animatorSetClose.isRunning())
                {
                    return;
                }
               if (!opened){
                   openAnim();
               }
               else
                   closeAnim();
            }
        });
    }
    AnimatorSet animatorSet = new AnimatorSet();
    AnimatorSet animatorSetClose = new AnimatorSet();
    public void openAnim() {
        textView.setVisibility(VISIBLE);
        textView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        animatorSet = new AnimatorSet();
        ArrayList<Animator> animators = new ArrayList<>();
        ObjectAnimator animator =  ObjectAnimator.ofFloat(textView0, BaseObjectAnimator.ALPHA, 1, 0);
        animator.setDuration(100);
        animators.add(animator);
        animators.add( BaseObjectAnimator.makeAnimator(textView0, BaseObjectAnimator.ROTATION, 0, -90));
        animators.add( BaseObjectAnimator.makeAnimator(halfCircleRight, BaseObjectAnimator.TRANSLATION_X, 0, -50));
        animators.add( BaseObjectAnimator.makeAnimator(textView, BaseObjectAnimator.SCALE_X, 0, 1f));
        animators.add( BaseObjectAnimator.makeAnimator(textView0, BaseObjectAnimator.TRANSLATION_X, 0, -50));
        animatorSet.playTogether(
                animators
        );
        animatorSet.setDuration(200);
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                running = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                running = false;
            }
        });
        opened = true;
    }
    public void closeAnim() {
        ArrayList<Animator> animators = new ArrayList<>();
        animators.add(BaseObjectAnimator.makeAnimator(halfCircleRight, BaseObjectAnimator.TRANSLATION_X, -50, 0));
        animators.add(BaseObjectAnimator.makeAnimator(textView, BaseObjectAnimator.SCALE_X, 1f, 0));
        animators.add(BaseObjectAnimator.makeAnimator(textView0, BaseObjectAnimator.ALPHA, 0, 1));
        animators.add(BaseObjectAnimator.makeAnimator(textView0, BaseObjectAnimator.ROTATION, 90, 0));
        animators.add(BaseObjectAnimator.makeAnimator(textView0, BaseObjectAnimator.TRANSLATION_X, -50, 0));
        animatorSetClose.setDuration(200);
        animatorSetClose.playTogether(animators);
        animatorSetClose.start();
        animatorSetClose.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                running = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                running = false;
                textView0.setVisibility(VISIBLE);
                textView.setVisibility(GONE);
                opened = false;
            }
        });
    }

    public void closeButton(){
        if (opened)
            closeAnim();
    }

    public void setOnClickListener(CloseButtonInterface closeButtonInterface) {
        mCloseButtonInterface = closeButtonInterface;
    }

    public interface CloseButtonInterface {
        public void onClickClosed();
    }

}
