/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.widget.OverScroller
 */
package android.support.design.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.ViewOffsetBehavior;
import android.support.v4.math.MathUtils;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;

abstract class HeaderBehavior<V extends View>
extends ViewOffsetBehavior<V> {
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId = -1;
    private Runnable mFlingRunnable;
    private boolean mIsBeingDragged;
    private int mLastMotionY;
    OverScroller mScroller;
    private int mTouchSlop = -1;
    private VelocityTracker mVelocityTracker;

    public HeaderBehavior() {
    }

    public HeaderBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void ensureVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
            return;
        }
    }

    boolean canDragView(V v) {
        return false;
    }

    final boolean fling(CoordinatorLayout coordinatorLayout, V v, int n, int n2, float f) {
        Runnable runnable = this.mFlingRunnable;
        if (runnable != null) {
            v.removeCallbacks(runnable);
            this.mFlingRunnable = null;
        }
        if (this.mScroller == null) {
            this.mScroller = new OverScroller(v.getContext());
        }
        this.mScroller.fling(0, this.getTopAndBottomOffset(), 0, Math.round(f), 0, 0, n, n2);
        if (this.mScroller.computeScrollOffset()) {
            this.mFlingRunnable = new FlingRunnable(this, coordinatorLayout, v);
            ViewCompat.postOnAnimation(v, this.mFlingRunnable);
            return true;
        }
        this.onFlingFinished(coordinatorLayout, v);
        return false;
    }

    int getMaxDragOffset(V v) {
        return - v.getHeight();
    }

    int getScrollRangeForDragFling(V v) {
        return v.getHeight();
    }

    int getTopBottomOffsetForScrollingSibling() {
        return this.getTopAndBottomOffset();
    }

    void onFlingFinished(CoordinatorLayout coordinatorLayout, V v) {
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        if (this.mTouchSlop < 0) {
            this.mTouchSlop = ViewConfiguration.get((Context)coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        if (motionEvent.getAction() == 2 && this.mIsBeingDragged) {
            return true;
        }
        switch (motionEvent.getActionMasked()) {
            default: {
                break;
            }
            case 2: {
                int n = this.mActivePointerId;
                if (n == -1 || (n = motionEvent.findPointerIndex(n)) == -1 || Math.abs((n = (int)motionEvent.getY(n)) - this.mLastMotionY) <= this.mTouchSlop) break;
                this.mIsBeingDragged = true;
                this.mLastMotionY = n;
                break;
            }
            case 1: 
            case 3: {
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
                coordinatorLayout = this.mVelocityTracker;
                if (coordinatorLayout == null) break;
                coordinatorLayout.recycle();
                this.mVelocityTracker = null;
                break;
            }
            case 0: {
                this.mIsBeingDragged = false;
                int n = (int)motionEvent.getX();
                int n2 = (int)motionEvent.getY();
                if (!this.canDragView(v) || !coordinatorLayout.isPointInChildBounds((View)v, n, n2)) break;
                this.mLastMotionY = n2;
                this.mActivePointerId = motionEvent.getPointerId(0);
                this.ensureVelocityTracker();
                break;
            }
        }
        coordinatorLayout = this.mVelocityTracker;
        if (coordinatorLayout != null) {
            coordinatorLayout.addMovement(motionEvent);
        }
        return this.mIsBeingDragged;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public boolean onTouchEvent(CoordinatorLayout var1_1, V var2_2, MotionEvent var3_3) {
        block12 : {
            if (this.mTouchSlop < 0) {
                this.mTouchSlop = ViewConfiguration.get((Context)var1_1.getContext()).getScaledTouchSlop();
            }
            switch (var3_3.getActionMasked()) {
                default: {
                    break block12;
                }
                case 3: {
                    ** GOTO lbl28
                }
                case 2: {
                    var5_4 = var3_3.findPointerIndex(this.mActivePointerId);
                    if (var5_4 == -1) {
                        return false;
                    }
                    var6_6 = (int)var3_3.getY(var5_4);
                    var5_4 = this.mLastMotionY - var6_6;
                    if (!this.mIsBeingDragged && (var7_8 = Math.abs(var5_4)) > (var8_9 = this.mTouchSlop)) {
                        this.mIsBeingDragged = true;
                        var5_4 = var5_4 > 0 ? (var5_4 -= var8_9) : (var5_4 += var8_9);
                    }
                    if (this.mIsBeingDragged) {
                        this.mLastMotionY = var6_6;
                        this.scroll(var1_1, var2_2, var5_4, this.getMaxDragOffset(var2_2), 0);
                    }
                    break block12;
                }
                case 1: {
                    var9_10 = this.mVelocityTracker;
                    if (var9_10 != null) {
                        var9_10.addMovement(var3_3);
                        this.mVelocityTracker.computeCurrentVelocity(1000);
                        var4_11 = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
                        this.fling(var1_1, var2_2, - this.getScrollRangeForDragFling(var2_2), 0, var4_11);
                    }
lbl28: // 4 sources:
                    this.mIsBeingDragged = false;
                    this.mActivePointerId = -1;
                    var1_1 = this.mVelocityTracker;
                    if (var1_1 != null) {
                        var1_1.recycle();
                        this.mVelocityTracker = null;
                    }
                    break block12;
                }
                case 0: 
            }
            var5_5 = (int)var3_3.getX();
            var6_7 = (int)var3_3.getY();
            if (var1_1.isPointInChildBounds((View)var2_2, var5_5, var6_7) == false) return false;
            if (this.canDragView(var2_2) == false) return false;
            this.mLastMotionY = var6_7;
            this.mActivePointerId = var3_3.getPointerId(0);
            this.ensureVelocityTracker();
        }
        var1_1 = this.mVelocityTracker;
        if (var1_1 == null) return true;
        var1_1.addMovement(var3_3);
        return true;
    }

    final int scroll(CoordinatorLayout coordinatorLayout, V v, int n, int n2, int n3) {
        return this.setHeaderTopBottomOffset(coordinatorLayout, v, this.getTopBottomOffsetForScrollingSibling() - n, n2, n3);
    }

    int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, V v, int n) {
        return this.setHeaderTopBottomOffset(coordinatorLayout, v, n, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    int setHeaderTopBottomOffset(CoordinatorLayout coordinatorLayout, V v, int n, int n2, int n3) {
        int n4 = this.getTopAndBottomOffset();
        if (n2 != 0 && n4 >= n2 && n4 <= n3) {
            if (n4 != (n = MathUtils.clamp(n, n2, n3))) {
                this.setTopAndBottomOffset(n);
                return n4 - n;
            }
            return 0;
        }
        return 0;
    }

    private class FlingRunnable
    implements Runnable {
        private final V mLayout;
        private final CoordinatorLayout mParent;

        FlingRunnable(V coordinatorLayout) {
            this.mParent = coordinatorLayout;
            this.mLayout = v;
        }

        @Override
        public void run() {
            if (this.mLayout != null && this$0.mScroller != null) {
                if (this$0.mScroller.computeScrollOffset()) {
                    HeaderBehavior headerBehavior = this$0;
                    headerBehavior.setHeaderTopBottomOffset(this.mParent, this.mLayout, headerBehavior.mScroller.getCurrY());
                    ViewCompat.postOnAnimation(this.mLayout, this);
                    return;
                }
                this$0.onFlingFinished(this.mParent, this.mLayout);
                return;
            }
        }
    }

}

