/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.animation.Interpolator
 *  android.widget.OverScroller
 */
package android.support.v4.widget;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

@Deprecated
public final class ScrollerCompat {
    OverScroller mScroller;

    ScrollerCompat(Context context, Interpolator interpolator) {
        context = interpolator != null ? new OverScroller(context, interpolator) : new OverScroller(context);
        this.mScroller = context;
    }

    @Deprecated
    public static ScrollerCompat create(Context context) {
        return ScrollerCompat.create(context, null);
    }

    @Deprecated
    public static ScrollerCompat create(Context context, Interpolator interpolator) {
        return new ScrollerCompat(context, interpolator);
    }

    @Deprecated
    public void abortAnimation() {
        this.mScroller.abortAnimation();
    }

    @Deprecated
    public boolean computeScrollOffset() {
        return this.mScroller.computeScrollOffset();
    }

    @Deprecated
    public void fling(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        this.mScroller.fling(n, n2, n3, n4, n5, n6, n7, n8);
    }

    @Deprecated
    public void fling(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10) {
        this.mScroller.fling(n, n2, n3, n4, n5, n6, n7, n8, n9, n10);
    }

    @Deprecated
    public float getCurrVelocity() {
        return this.mScroller.getCurrVelocity();
    }

    @Deprecated
    public int getCurrX() {
        return this.mScroller.getCurrX();
    }

    @Deprecated
    public int getCurrY() {
        return this.mScroller.getCurrY();
    }

    @Deprecated
    public int getFinalX() {
        return this.mScroller.getFinalX();
    }

    @Deprecated
    public int getFinalY() {
        return this.mScroller.getFinalY();
    }

    @Deprecated
    public boolean isFinished() {
        return this.mScroller.isFinished();
    }

    @Deprecated
    public boolean isOverScrolled() {
        return this.mScroller.isOverScrolled();
    }

    @Deprecated
    public void notifyHorizontalEdgeReached(int n, int n2, int n3) {
        this.mScroller.notifyHorizontalEdgeReached(n, n2, n3);
    }

    @Deprecated
    public void notifyVerticalEdgeReached(int n, int n2, int n3) {
        this.mScroller.notifyVerticalEdgeReached(n, n2, n3);
    }

    @Deprecated
    public boolean springBack(int n, int n2, int n3, int n4, int n5, int n6) {
        return this.mScroller.springBack(n, n2, n3, n4, n5, n6);
    }

    @Deprecated
    public void startScroll(int n, int n2, int n3, int n4) {
        this.mScroller.startScroll(n, n2, n3, n4);
    }

    @Deprecated
    public void startScroll(int n, int n2, int n3, int n4, int n5) {
        this.mScroller.startScroll(n, n2, n3, n4, n5);
    }
}

