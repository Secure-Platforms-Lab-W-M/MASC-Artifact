/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.AnimatorSet
 *  android.animation.ValueAnimator
 */
package com.google.android.material.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import java.util.Collection;
import java.util.List;

public class AnimatorSetCompat {
    public static void playTogether(AnimatorSet animatorSet, List<Animator> list) {
        Animator animator;
        long l = 0L;
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            animator = list.get(i);
            l = Math.max(l, animator.getStartDelay() + animator.getDuration());
        }
        animator = ValueAnimator.ofInt((int[])new int[]{0, 0});
        animator.setDuration(l);
        list.add(0, animator);
        animatorSet.playTogether(list);
    }
}

