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

class ScrollerCompatGingerbread {
    ScrollerCompatGingerbread() {
    }

    public static void abortAnimation(Object object) {
        ((OverScroller)object).abortAnimation();
    }

    public static boolean computeScrollOffset(Object object) {
        return ((OverScroller)object).computeScrollOffset();
    }

    public static Object createScroller(Context context, Interpolator interpolator) {
        if (interpolator != null) {
            return new OverScroller(context, interpolator);
        }
        return new OverScroller(context);
    }

    public static void fling(Object object, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        ((OverScroller)object).fling(n, n2, n3, n4, n5, n6, n7, n8);
    }

    public static void fling(Object object, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, int n9, int n10) {
        ((OverScroller)object).fling(n, n2, n3, n4, n5, n6, n7, n8, n9, n10);
    }

    public static int getCurrX(Object object) {
        return ((OverScroller)object).getCurrX();
    }

    public static int getCurrY(Object object) {
        return ((OverScroller)object).getCurrY();
    }

    public static int getFinalX(Object object) {
        return ((OverScroller)object).getFinalX();
    }

    public static int getFinalY(Object object) {
        return ((OverScroller)object).getFinalY();
    }

    public static boolean isFinished(Object object) {
        return ((OverScroller)object).isFinished();
    }

    public static boolean isOverScrolled(Object object) {
        return ((OverScroller)object).isOverScrolled();
    }

    public static void notifyHorizontalEdgeReached(Object object, int n, int n2, int n3) {
        ((OverScroller)object).notifyHorizontalEdgeReached(n, n2, n3);
    }

    public static void notifyVerticalEdgeReached(Object object, int n, int n2, int n3) {
        ((OverScroller)object).notifyVerticalEdgeReached(n, n2, n3);
    }

    public static boolean springBack(Object object, int n, int n2, int n3, int n4, int n5, int n6) {
        return ((OverScroller)object).springBack(n, n2, n3, n4, n5, n6);
    }

    public static void startScroll(Object object, int n, int n2, int n3, int n4) {
        ((OverScroller)object).startScroll(n, n2, n3, n4);
    }

    public static void startScroll(Object object, int n, int n2, int n3, int n4, int n5) {
        ((OverScroller)object).startScroll(n, n2, n3, n4, n5);
    }
}

