/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 */
package com.google.android.material.floatingactionbutton;

import android.animation.Animator;

class AnimatorTracker {
    private Animator currentAnimator;

    AnimatorTracker() {
    }

    public void cancelCurrent() {
        Animator animator = this.currentAnimator;
        if (animator != null) {
            animator.cancel();
        }
    }

    public void clear() {
        this.currentAnimator = null;
    }

    public void onNextAnimationStart(Animator animator) {
        this.cancelCurrent();
        this.currentAnimator = animator;
    }
}

