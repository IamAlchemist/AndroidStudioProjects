package com.tiny.wizard.uiexample;
// Created by wizard on 12/26/14.

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;

public class CircleBarActivity extends Activity{
    private ObjectAnimator animator;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_circle_bar);

        CircularBar circularBar = (CircularBar)findViewById(R.id.circle_bar_circle);


        animate(circularBar, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        }, 0.75f, 3000);

    }

    private void animate(final CircularBar circularBar, final Animator.AnimatorListener listener,
                         final float progress, final int duration){

        animator = ObjectAnimator.ofFloat(circularBar, "progress", 0, progress);
        animator.setDuration(duration);

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                circularBar.setProgress(progress);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });

        if(listener != null){
            animator.addListener(listener);
        }

        animator.reverse();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                circularBar.setProgress((Float)valueAnimator.getAnimatedValue());
            }
        });

        animator.start();
    }
}
