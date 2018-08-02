package com.notification.notyos10.animations.creative;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.notification.notyos10.animations.base.BaseCreative;
import com.notification.notyos10.animations.base.BaseObjectAnimator;


public class ZoomAnimator {
    public static void playZoomOut(View target, Animator.AnimatorListener animatorListener){
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.SCALE_X, 0.3f, 1));
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.SCALE_Y, 0.3f, 1));
        baseCreative.addListener(animatorListener);
        baseCreative.startAnimationTogether();
    }
    public static void playZoomIn(View target, Animator.AnimatorListener animatorListener){
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.SCALE_X, 1, 0));
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.SCALE_Y, 1, 0));
        baseCreative.addListener(animatorListener);
        baseCreative.startAnimationTogether();
    }
    public static void playZoomIn(View target ){
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.SCALE_X, 1, 0));
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, BaseObjectAnimator.SCALE_Y, 1, 0));
        baseCreative.startAnimationTogether();
    }
}
