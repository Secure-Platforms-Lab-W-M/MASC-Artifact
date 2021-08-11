/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Handler
 *  android.util.AttributeSet
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationUtils
 *  android.widget.TextView
 */
package com.codetroopers.betterpickers.numberpicker;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class NumberPickerErrorTextView
extends TextView {
    private static final long LENGTH_SHORT = 3000L;
    private Handler fadeInEndHandler;
    private Runnable hideRunnable;

    public NumberPickerErrorTextView(Context context) {
        super(context);
        this.hideRunnable = new Runnable(){

            @Override
            public void run() {
                NumberPickerErrorTextView.this.hide();
            }
        };
        this.fadeInEndHandler = new Handler();
    }

    public NumberPickerErrorTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.hideRunnable = new ;
        this.fadeInEndHandler = new Handler();
    }

    public NumberPickerErrorTextView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.hideRunnable = new ;
        this.fadeInEndHandler = new Handler();
    }

    public void hide() {
        this.fadeInEndHandler.removeCallbacks(this.hideRunnable);
        Animation animation = AnimationUtils.loadAnimation((Context)this.getContext(), (int)17432577);
        animation.setAnimationListener(new Animation.AnimationListener(){

            public void onAnimationEnd(Animation animation) {
                NumberPickerErrorTextView.this.setVisibility(4);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.startAnimation(animation);
    }

    public void hideImmediately() {
        this.fadeInEndHandler.removeCallbacks(this.hideRunnable);
        this.setVisibility(4);
    }

    public void show() {
        this.fadeInEndHandler.removeCallbacks(this.hideRunnable);
        Animation animation = AnimationUtils.loadAnimation((Context)this.getContext(), (int)17432576);
        animation.setAnimationListener(new Animation.AnimationListener(){

            public void onAnimationEnd(Animation animation) {
                NumberPickerErrorTextView.this.fadeInEndHandler.postDelayed(NumberPickerErrorTextView.this.hideRunnable, 3000L);
                NumberPickerErrorTextView.this.setVisibility(0);
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        this.startAnimation(animation);
    }

}

