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
package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.StateSet;
import java.util.ArrayList;

final class StateListAnimator {
    private final Animator.AnimatorListener mAnimationListener;
    private Tuple mLastMatch = null;
    ValueAnimator mRunningAnimator = null;
    private final ArrayList<Tuple> mTuples = new ArrayList();

    StateListAnimator() {
        this.mAnimationListener = new AnimatorListenerAdapter(){

            public void onAnimationEnd(Animator animator2) {
                if (StateListAnimator.this.mRunningAnimator == animator2) {
                    StateListAnimator.this.mRunningAnimator = null;
                    return;
                }
            }
        };
    }

    private void cancel() {
        ValueAnimator valueAnimator = this.mRunningAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.mRunningAnimator = null;
            return;
        }
    }

    private void start(Tuple tuple) {
        this.mRunningAnimator = tuple.mAnimator;
        this.mRunningAnimator.start();
    }

    public void addState(int[] object, ValueAnimator valueAnimator) {
        object = new Tuple((int[])object, valueAnimator);
        valueAnimator.addListener(this.mAnimationListener);
        this.mTuples.add((Tuple)object);
    }

    public void jumpToCurrentState() {
        ValueAnimator valueAnimator = this.mRunningAnimator;
        if (valueAnimator != null) {
            valueAnimator.end();
            this.mRunningAnimator = null;
            return;
        }
    }

    void setState(int[] object) {
        Tuple tuple;
        Tuple tuple2 = null;
        int n = this.mTuples.size();
        int n2 = 0;
        do {
            tuple = tuple2;
            if (n2 >= n) break;
            tuple = this.mTuples.get(n2);
            if (StateSet.stateSetMatches((int[])tuple.mSpecs, (int[])object)) break;
            ++n2;
        } while (true);
        if (tuple == (object = this.mLastMatch)) {
            return;
        }
        if (object != null) {
            this.cancel();
        }
        this.mLastMatch = tuple;
        if (tuple != null) {
            this.start(tuple);
            return;
        }
    }

    static class Tuple {
        final ValueAnimator mAnimator;
        final int[] mSpecs;

        Tuple(int[] arrn, ValueAnimator valueAnimator) {
            this.mSpecs = arrn;
            this.mAnimator = valueAnimator;
        }
    }

}

