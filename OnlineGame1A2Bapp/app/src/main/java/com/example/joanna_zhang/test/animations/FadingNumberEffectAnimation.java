package com.example.joanna_zhang.test.animations;

import android.graphics.Color;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;


/**
 * This animation first add the target textview inside the container for showing the animation. (so you don't have to do
 * that manually) Then the target textview will be fade up with its number assigned, and ends up with removing the
 * target textview from the container so it will not exist anymore.
 */
public class FadingNumberEffectAnimation extends Animation implements Animation.AnimationListener {
    private static final String TAG = "HPAnimation";
    private static final int XOFFSET = -10;
    private static final int YOFFSET = -140;
    private ViewGroup container;
    private TextView targetTxt;
    private float fromX;
    private float fromY;
    private float targetX;
    private float targetY;
    private int textSize = 45;

    public FadingNumberEffectAnimation(ViewGroup container, TextView targetTxt) {
        this.container = container;
        this.targetTxt = targetTxt;
        this.fromX = targetTxt.getX();
        this.fromY = targetTxt.getY();
        this.targetX = targetTxt.getX() + XOFFSET;
        this.targetY = targetTxt.getY() + YOFFSET;
        container.addView(targetTxt);
        targetTxt.setTextColor(Color.RED);
        targetTxt.setTextSize(textSize);
        setAnimationListener(this);
        setDuration(2000);
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float alpha = 1 - interpolatedTime;
        float x = fromX + (targetX - fromX) * interpolatedTime;
        float y = fromY + (targetY - fromY) * interpolatedTime;
        targetTxt.setAlpha(alpha);
        targetTxt.setX(x);
        targetTxt.setY(y);
    }

    @Override
    public void onAnimationStart(Animation animation) {
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        container.removeView(targetTxt);
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
    }
}
