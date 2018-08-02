package com.notification.notyos10.animations.creative;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

import com.notification.notyos10.animations.base.BaseCreative;


public class FadeAnimator {
    public static void playFadeOut(View target){
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, "alpha", 1, 0));
        baseCreative.setDuration(200);
        baseCreative.startAnimationTogether();
    }
    public static void playFadeOut(View target, Animator.AnimatorListener animatorListener){
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, "alpha", 1, 0));
        baseCreative.addListener(animatorListener);
        baseCreative.startAnimationTogether();
    }

    public static void playinitFadeIn(View target){
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, "alpha", 0, 1));
        baseCreative.startAnimationTogether();
    }

    public static void playinitFadeIn(View target, Animator.AnimatorListener animatorListener){
        BaseCreative baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, "alpha", 0, 1));
        baseCreative.addListener(animatorListener);
        baseCreative.setDuration(200);
        baseCreative.startAnimationTogether();
    }


}
