package com.example.joanna_zhang.test.animations;

import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;


public class CostingProgressBarAnimation extends Animation {
    private static final String TAG = "HPAnimation";
    private ProgressBar progressBar;
    private float from;
    private float to;

    public CostingProgressBarAnimation(ProgressBar progressBar, float from, float to) {
        super();
        this.progressBar = progressBar;
        this.from = from;
        this.to = to;
        //progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
        long damage = (long) (from - to);
        long duration = damage < 40 ? 800 : damage < 130 ? 1200 : damage < 250 ? 1800 : 2200;
        setDuration(duration);
        Log.d(TAG, "Progress animation: from " + from + " to " + to + " , duration " + duration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        value = value < 0 ? 0 : value;
        /*if (value <= to)
            progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.DARKEN);*/
        progressBar.setProgress((int) value);
        Log.d(TAG, "Transformation: now process: " + progressBar.getProgress() +", set value: " + value);
    }

}
