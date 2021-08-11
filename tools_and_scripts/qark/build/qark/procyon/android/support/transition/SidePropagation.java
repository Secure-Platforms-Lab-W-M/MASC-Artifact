// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.transition;

import android.graphics.Rect;
import android.view.ViewGroup;
import android.support.v4.view.ViewCompat;
import android.view.View;

public class SidePropagation extends VisibilityPropagation
{
    private float mPropagationSpeed;
    private int mSide;
    
    public SidePropagation() {
        this.mPropagationSpeed = 3.0f;
        this.mSide = 80;
    }
    
    private int distance(final View view, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
        final int mSide = this.mSide;
        final boolean b = false;
        boolean b2 = false;
        int mSide2;
        if (mSide == 8388611) {
            if (ViewCompat.getLayoutDirection(view) == 1) {
                b2 = true;
            }
            if (b2) {
                mSide2 = 5;
            }
            else {
                mSide2 = 3;
            }
        }
        else if (mSide == 8388613) {
            boolean b3 = b;
            if (ViewCompat.getLayoutDirection(view) == 1) {
                b3 = true;
            }
            if (b3) {
                mSide2 = 3;
            }
            else {
                mSide2 = 5;
            }
        }
        else {
            mSide2 = this.mSide;
        }
        if (mSide2 == 3) {
            return n7 - n + Math.abs(n4 - n2);
        }
        if (mSide2 == 5) {
            return n - n5 + Math.abs(n4 - n2);
        }
        if (mSide2 == 48) {
            return n8 - n2 + Math.abs(n3 - n);
        }
        if (mSide2 != 80) {
            return 0;
        }
        return n2 - n6 + Math.abs(n3 - n);
    }
    
    private int getMaxDistance(final ViewGroup viewGroup) {
        final int mSide = this.mSide;
        if (mSide != 3 && mSide != 5 && mSide != 8388611 && mSide != 8388613) {
            return viewGroup.getHeight();
        }
        return viewGroup.getWidth();
    }
    
    @Override
    public long getStartDelay(final ViewGroup viewGroup, final Transition transition, TransitionValues transitionValues, final TransitionValues transitionValues2) {
        if (transitionValues == null && transitionValues2 == null) {
            return 0L;
        }
        final Rect epicenter = transition.getEpicenter();
        int n;
        if (transitionValues2 != null && this.getViewVisibility(transitionValues) != 0) {
            n = 1;
            transitionValues = transitionValues2;
        }
        else {
            n = -1;
        }
        final int viewX = this.getViewX(transitionValues);
        final int viewY = this.getViewY(transitionValues);
        final int[] array = new int[2];
        viewGroup.getLocationOnScreen(array);
        final int n2 = array[0] + Math.round(viewGroup.getTranslationX());
        final int n3 = array[1] + Math.round(viewGroup.getTranslationY());
        final int n4 = n2 + viewGroup.getWidth();
        final int n5 = n3 + viewGroup.getHeight();
        int centerX;
        int centerY;
        if (epicenter != null) {
            centerX = epicenter.centerX();
            centerY = epicenter.centerY();
        }
        else {
            centerX = (n2 + n4) / 2;
            centerY = (n3 + n5) / 2;
        }
        final float n6 = this.distance((View)viewGroup, viewX, viewY, centerX, centerY, n2, n3, n4, n5) / (float)this.getMaxDistance(viewGroup);
        long duration = transition.getDuration();
        if (duration < 0L) {
            duration = 300L;
        }
        return Math.round(n * duration / this.mPropagationSpeed * n6);
    }
    
    public void setPropagationSpeed(final float mPropagationSpeed) {
        if (mPropagationSpeed != 0.0f) {
            this.mPropagationSpeed = mPropagationSpeed;
            return;
        }
        throw new IllegalArgumentException("propagationSpeed may not be 0");
    }
    
    public void setSide(final int mSide) {
        this.mSide = mSide;
    }
}
