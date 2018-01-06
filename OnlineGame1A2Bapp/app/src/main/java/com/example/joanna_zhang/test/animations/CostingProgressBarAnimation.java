package com.example.joanna_zhang.test.animations;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;


public class CostingProgressBarAnimation extends Animation {
    private ProgressBar progressBar;
    private float from;
    private float to;

    public CostingProgressBarAnimation(ProgressBar progressBar, float from, float to) {
        super();
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;

        setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN);}
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        long damage = (long) (from - to);
        long duration = damage < 40 ? 800 : damage < 130 ? 1200 : damage < 250 ? 1800 : 2200;
        setDuration(duration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        progressBar.setProgress((int) value);
    }

}
