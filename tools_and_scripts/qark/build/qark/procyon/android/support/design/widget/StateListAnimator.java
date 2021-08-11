// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.util.StateSet;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import java.util.ArrayList;
import android.animation.ValueAnimator;
import android.animation.Animator$AnimatorListener;

final class StateListAnimator
{
    private final Animator$AnimatorListener mAnimationListener;
    private Tuple mLastMatch;
    ValueAnimator mRunningAnimator;
    private final ArrayList<Tuple> mTuples;
    
    StateListAnimator() {
        this.mTuples = new ArrayList<Tuple>();
        this.mLastMatch = null;
        this.mRunningAnimator = null;
        this.mAnimationListener = (Animator$AnimatorListener)new AnimatorListenerAdapter() {
            public void onAnimationEnd(final Animator animator) {
                if (StateListAnimator.this.mRunningAnimator == animator) {
                    StateListAnimator.this.mRunningAnimator = null;
                }
            }
        };
    }
    
    private void cancel() {
        final ValueAnimator mRunningAnimator = this.mRunningAnimator;
        if (mRunningAnimator != null) {
            mRunningAnimator.cancel();
            this.mRunningAnimator = null;
        }
    }
    
    private void start(final Tuple tuple) {
        (this.mRunningAnimator = tuple.mAnimator).start();
    }
    
    public void addState(final int[] array, final ValueAnimator valueAnimator) {
        final Tuple tuple = new Tuple(array, valueAnimator);
        valueAnimator.addListener(this.mAnimationListener);
        this.mTuples.add(tuple);
    }
    
    public void jumpToCurrentState() {
        final ValueAnimator mRunningAnimator = this.mRunningAnimator;
        if (mRunningAnimator != null) {
            mRunningAnimator.end();
            this.mRunningAnimator = null;
        }
    }
    
    void setState(final int[] array) {
        final Tuple tuple = null;
        final int size = this.mTuples.size();
        int n = 0;
        Tuple mLastMatch;
        while (true) {
            mLastMatch = tuple;
            if (n >= size) {
                break;
            }
            mLastMatch = this.mTuples.get(n);
            if (StateSet.stateSetMatches(mLastMatch.mSpecs, array)) {
                break;
            }
            ++n;
        }
        final Tuple mLastMatch2 = this.mLastMatch;
        if (mLastMatch == mLastMatch2) {
            return;
        }
        if (mLastMatch2 != null) {
            this.cancel();
        }
        if ((this.mLastMatch = mLastMatch) != null) {
            this.start(mLastMatch);
        }
    }
    
    static class Tuple
    {
        final ValueAnimator mAnimator;
        final int[] mSpecs;
        
        Tuple(final int[] mSpecs, final ValueAnimator mAnimator) {
            this.mSpecs = mSpecs;
            this.mAnimator = mAnimator;
        }
    }
}
