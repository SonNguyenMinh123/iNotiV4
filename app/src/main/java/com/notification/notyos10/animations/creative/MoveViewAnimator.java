package com.notification.notyos10.animations.creative;


import android.animation.ObjectAnimator;
import android.view.View;

import com.notification.notyos10.animations.base.BaseCreative;


public class MoveViewAnimator {
    static BaseCreative baseCreative;

    public static void initMoveRightIn(View target) {
        baseCreative = new BaseCreative();
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, "alpha", 1, 0, 0));
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, "scaleX", 1, 0.3f, 0));
        baseCreative.addAnimator(ObjectAnimator.ofFloat(target, "scaleY", 1, 0.3f, 0));
    }

    public static void play() {

    }
}
