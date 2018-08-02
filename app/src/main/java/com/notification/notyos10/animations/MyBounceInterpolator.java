package com.notification.notyos10.animations;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

public class MyBounceInterpolator implements Interpolator {

    private float timeDivider;
    private AccelerateInterpolator a;

    public MyBounceInterpolator(float timeDivider) {
        a = new AccelerateInterpolator();
        this.timeDivider = timeDivider;
    }

    public float getInterpolation(float t) {
            return a.getInterpolation(t);
    }

}
