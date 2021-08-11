/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorSet
 */
package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import java.util.List;

interface MotionStrategy {
    public void addAnimationListener(Animator.AnimatorListener var1);

    public AnimatorSet createAnimator();

    public MotionSpec getCurrentMotionSpec();

    public int getDefaultMotionSpecResource();

    public List<Animator.AnimatorListener> getListeners();

    public MotionSpec getMotionSpec();

    public void onAnimationCancel();

    public void onAnimationEnd();

    public void onAnimationStart(Animator var1);

    public void onChange(ExtendedFloatingActionButton.OnChangedCallback var1);

    public void performNow();

    public void removeAnimationListener(Animator.AnimatorListener var1);

    public void setMotionSpec(MotionSpec var1);

    public boolean shouldCancel();
}

