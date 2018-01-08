package com.example.joanna_zhang.test.animations;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;


/**
 * This animation first add the target textview inside the container for showing the animation. (so you don't have to do
 * that manually) Then the target textview will be fade up with its number assigned, and ends up with removing the
 * target textview from the container so it will not exist anymore.
 */
public class FadingTextEffectAnimation extends Animation implements Animation.AnimationListener {
    private static final String TAG = "HPAnimation";
    private int xoffset = -10;
    private int yoffset = -140;
    private ViewGroup container;
    private View targetView;
    private float fromX;
    private float fromY;
    private float targetX;
    private float targetY;
    private boolean fadding = true;

    public FadingTextEffectAnimation(ViewGroup container, View targetView, int xoffset, int yoffset) {
        this.container = container;
        this.targetView = targetView;
        this.fromX = targetView.getX();
        this.fromY = targetView.getY();
        this.targetX = targetView.getX() + xoffset;
        this.targetY = targetView.getY() + yoffset;
        container.addView(targetView);
        setAnimationListener(this);
        setDuration(2000);
    }

    public FadingTextEffectAnimation(ViewGroup container, TextView targetView) {
        this(container, targetView, -10, -140);
    }

    public void setFading(boolean fading) {
        this.fadding = fading;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float alpha = 1 - interpolatedTime;
        float x = fromX + (targetX - fromX) * interpolatedTime;
        float y = fromY + (targetY - fromY) * interpolatedTime;
        if (fadding)
            targetView.setAlpha(alpha);
        targetView.setX(x);
        targetView.setY(y);
    }

    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
        container.removeView(targetView);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}
}
