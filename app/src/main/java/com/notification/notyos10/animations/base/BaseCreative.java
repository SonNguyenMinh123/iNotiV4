package com.notification.notyos10.animations.base;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;

import java.util.ArrayList;

public class BaseCreative{
    protected AnimatorSet animatorSet = new AnimatorSet();
    protected ArrayList<Animator> objs = new ArrayList<>();

    public BaseCreative() {
       animatorSet = new AnimatorSet();
        objs = new ArrayList<>();
    }

    public void startAnimationTogether(){
        animatorSet.playTogether(objs);
        animatorSet.start();
    }

    public void startAnimationSequentially(){
        animatorSet.playSequentially(objs);
        animatorSet.start();
    }

    public void endAnimaton(){
        animatorSet.end();
    }
    public void addListener(Animator.AnimatorListener animatorListener){
        animatorSet.addListener(animatorListener);
    }
    public void setDuration(long time){
        animatorSet.setDuration(time);
    }
    public AnimatorSet getAnimatorSet(){
        return animatorSet;
    }
    public ArrayList<Animator> getObjs(){
        return objs;
    }
    public void addAnimator(ObjectAnimator animator){
        objs.add(animator);
    }
    public void addListAnimator(ArrayList<ObjectAnimator> animators){
        objs.addAll(animators);
    }

    public void setRepeatModeAll( int value){
        for (int i=0;i<objs.size();i++){
            ObjectAnimator objectAnimator = (ObjectAnimator) objs.get(i);
            objectAnimator.setRepeatMode(value);
        }
    }
    public void setRepeatCountAll(int value){
        for (int i=0;i<objs.size();i++){
            ObjectAnimator objectAnimator = (ObjectAnimator) objs.get(i);
            objectAnimator.setRepeatCount(value);
        }
    }
    public void setRepeatModeChild(ObjectAnimator objectAnimator, int value){
        objectAnimator.setRepeatMode(value);
    }
    public void setRepeatCountChild(ObjectAnimator objectAnimator, int value){
        objectAnimator.setRepeatCount(value);
    }
}
