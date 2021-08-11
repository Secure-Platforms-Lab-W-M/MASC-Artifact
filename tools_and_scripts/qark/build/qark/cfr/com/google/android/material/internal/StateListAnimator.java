/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.ValueAnimator
 *  android.util.StateSet
 */
package com.google.android.material.internal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.StateSet;
import java.util.ArrayList;

public final class StateListAnimator {
    private final Animator.AnimatorListener animationListener;
    private Tuple lastMatch = null;
    ValueAnimator runningAnimator = null;
    private final ArrayList<Tuple> tuples = new ArrayList();

    public StateListAnimator() {
        this.animationListener = new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator animator2) {
                if (StateListAnimator.this.runningAnimator == animator2) {
                    StateListAnimator.this.runningAnimator = null;
                }
            }
        };
    }

    private void cancel() {
        ValueAnimator valueAnimator = this.runningAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.runningAnimator = null;
        }
    }

    private void start(Tuple tuple) {
        tuple = tuple.animator;
        this.runningAnimator = tuple;
        tuple.start();
    }

    public void addState(int[] object, ValueAnimator valueAnimator) {
        object = new Tuple((int[])object, valueAnimator);
        valueAnimator.addListener(this.animationListener);
        this.tuples.add((Tuple)object);
    }

    public void jumpToCurrentState() {
        ValueAnimator valueAnimator = this.runningAnimator;
        if (valueAnimator != null) {
            valueAnimator.end();
            this.runningAnimator = null;
        }
    }

    public void setState(int[] object) {
        Tuple tuple;
        Tuple tuple2 = null;
        int n = this.tuples.size();
        int n2 = 0;
        do {
            tuple = tuple2;
            if (n2 >= n) break;
            tuple = this.tuples.get(n2);
            if (StateSet.stateSetMatches((int[])tuple.specs, (int[])object)) break;
            ++n2;
        } while (true);
        if (tuple == (object = this.lastMatch)) {
            return;
        }
        if (object != null) {
            this.cancel();
        }
        this.lastMatch = tuple;
        if (tuple != null) {
            this.start(tuple);
        }
    }

    static class Tuple {
        final ValueAnimator animator;
        final int[] specs;

        Tuple(int[] arrn, ValueAnimator valueAnimator) {
            this.specs = arrn;
            this.animator = valueAnimator;
        }
    }

}

