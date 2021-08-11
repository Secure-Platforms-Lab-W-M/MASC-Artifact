// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.support.v4.math.MathUtils;
import android.view.ViewConfiguration;
import android.view.MotionEvent;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.content.Context;
import android.view.VelocityTracker;
import android.widget.OverScroller;
import android.view.View;

abstract class HeaderBehavior<V extends View> extends ViewOffsetBehavior<V>
{
    private static final int INVALID_POINTER = -1;
    private int mActivePointerId;
    private Runnable mFlingRunnable;
    private boolean mIsBeingDragged;
    private int mLastMotionY;
    OverScroller mScroller;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    
    public HeaderBehavior() {
        this.mActivePointerId = -1;
        this.mTouchSlop = -1;
    }
    
    public HeaderBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.mActivePointerId = -1;
        this.mTouchSlop = -1;
    }
    
    private void ensureVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }
    
    boolean canDragView(final V v) {
        return false;
    }
    
    final boolean fling(final CoordinatorLayout coordinatorLayout, final V v, final int n, final int n2, final float n3) {
        final Runnable mFlingRunnable = this.mFlingRunnable;
        if (mFlingRunnable != null) {
            v.removeCallbacks(mFlingRunnable);
            this.mFlingRunnable = null;
        }
        if (this.mScroller == null) {
            this.mScroller = new OverScroller(v.getContext());
        }
        this.mScroller.fling(0, this.getTopAndBottomOffset(), 0, Math.round(n3), 0, 0, n, n2);
        if (this.mScroller.computeScrollOffset()) {
            ViewCompat.postOnAnimation(v, this.mFlingRunnable = new FlingRunnable(coordinatorLayout, v));
            return true;
        }
        this.onFlingFinished(coordinatorLayout, v);
        return false;
    }
    
    int getMaxDragOffset(final V v) {
        return -v.getHeight();
    }
    
    int getScrollRangeForDragFling(final V v) {
        return v.getHeight();
    }
    
    int getTopBottomOffsetForScrollingSibling() {
        return this.getTopAndBottomOffset();
    }
    
    void onFlingFinished(final CoordinatorLayout coordinatorLayout, final V v) {
    }
    
    @Override
    public boolean onInterceptTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        if (this.mTouchSlop < 0) {
            this.mTouchSlop = ViewConfiguration.get(coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        if (motionEvent.getAction() == 2 && this.mIsBeingDragged) {
            return true;
        }
        switch (motionEvent.getActionMasked()) {
            case 2: {
                final int mActivePointerId = this.mActivePointerId;
                if (mActivePointerId == -1) {
                    break;
                }
                final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                if (pointerIndex == -1) {
                    break;
                }
                final int mLastMotionY = (int)motionEvent.getY(pointerIndex);
                if (Math.abs(mLastMotionY - this.mLastMotionY) > this.mTouchSlop) {
                    this.mIsBeingDragged = true;
                    this.mLastMotionY = mLastMotionY;
                    break;
                }
                break;
            }
            case 1:
            case 3: {
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
                final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                    break;
                }
                break;
            }
            case 0: {
                this.mIsBeingDragged = false;
                final int n = (int)motionEvent.getX();
                final int mLastMotionY2 = (int)motionEvent.getY();
                if (this.canDragView(v) && coordinatorLayout.isPointInChildBounds(v, n, mLastMotionY2)) {
                    this.mLastMotionY = mLastMotionY2;
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.ensureVelocityTracker();
                    break;
                }
                break;
            }
        }
        final VelocityTracker mVelocityTracker2 = this.mVelocityTracker;
        if (mVelocityTracker2 != null) {
            mVelocityTracker2.addMovement(motionEvent);
        }
        return this.mIsBeingDragged;
    }
    
    @Override
    public boolean onTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        if (this.mTouchSlop < 0) {
            this.mTouchSlop = ViewConfiguration.get(coordinatorLayout.getContext()).getScaledTouchSlop();
        }
        Label_0344: {
            switch (motionEvent.getActionMasked()) {
                default: {
                    break Label_0344;
                }
                case 3: {
                    break;
                }
                case 2: {
                    final int pointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (pointerIndex == -1) {
                        return false;
                    }
                    final int mLastMotionY = (int)motionEvent.getY(pointerIndex);
                    int n = this.mLastMotionY - mLastMotionY;
                    if (!this.mIsBeingDragged) {
                        final int abs = Math.abs(n);
                        final int mTouchSlop = this.mTouchSlop;
                        if (abs > mTouchSlop) {
                            this.mIsBeingDragged = true;
                            if (n > 0) {
                                n -= mTouchSlop;
                            }
                            else {
                                n += mTouchSlop;
                            }
                        }
                    }
                    if (this.mIsBeingDragged) {
                        this.mLastMotionY = mLastMotionY;
                        this.scroll(coordinatorLayout, v, n, this.getMaxDragOffset(v), 0);
                    }
                    break Label_0344;
                }
                case 1: {
                    final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                    if (mVelocityTracker != null) {
                        mVelocityTracker.addMovement(motionEvent);
                        this.mVelocityTracker.computeCurrentVelocity(1000);
                        this.fling(coordinatorLayout, v, -this.getScrollRangeForDragFling(v), 0, this.mVelocityTracker.getYVelocity(this.mActivePointerId));
                        break;
                    }
                    break;
                }
                case 0: {
                    final int n2 = (int)motionEvent.getX();
                    final int mLastMotionY2 = (int)motionEvent.getY();
                    if (coordinatorLayout.isPointInChildBounds(v, n2, mLastMotionY2) && this.canDragView(v)) {
                        this.mLastMotionY = mLastMotionY2;
                        this.mActivePointerId = motionEvent.getPointerId(0);
                        this.ensureVelocityTracker();
                        break Label_0344;
                    }
                    return false;
                }
            }
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
            final VelocityTracker mVelocityTracker2 = this.mVelocityTracker;
            if (mVelocityTracker2 != null) {
                mVelocityTracker2.recycle();
                this.mVelocityTracker = null;
            }
        }
        final VelocityTracker mVelocityTracker3 = this.mVelocityTracker;
        if (mVelocityTracker3 != null) {
            mVelocityTracker3.addMovement(motionEvent);
            return true;
        }
        return true;
    }
    
    final int scroll(final CoordinatorLayout coordinatorLayout, final V v, final int n, final int n2, final int n3) {
        return this.setHeaderTopBottomOffset(coordinatorLayout, v, this.getTopBottomOffsetForScrollingSibling() - n, n2, n3);
    }
    
    int setHeaderTopBottomOffset(final CoordinatorLayout coordinatorLayout, final V v, final int n) {
        return this.setHeaderTopBottomOffset(coordinatorLayout, v, n, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    int setHeaderTopBottomOffset(final CoordinatorLayout coordinatorLayout, final V v, int clamp, final int n, final int n2) {
        final int topAndBottomOffset = this.getTopAndBottomOffset();
        if (n == 0 || topAndBottomOffset < n || topAndBottomOffset > n2) {
            return 0;
        }
        clamp = MathUtils.clamp(clamp, n, n2);
        if (topAndBottomOffset != clamp) {
            this.setTopAndBottomOffset(clamp);
            return topAndBottomOffset - clamp;
        }
        return 0;
    }
    
    private class FlingRunnable implements Runnable
    {
        private final V mLayout;
        private final CoordinatorLayout mParent;
        
        FlingRunnable(final CoordinatorLayout mParent, final V mLayout) {
            this.mParent = mParent;
            this.mLayout = mLayout;
        }
        
        @Override
        public void run() {
            if (this.mLayout == null || HeaderBehavior.this.mScroller == null) {
                return;
            }
            if (HeaderBehavior.this.mScroller.computeScrollOffset()) {
                final HeaderBehavior this$0 = HeaderBehavior.this;
                this$0.setHeaderTopBottomOffset(this.mParent, this.mLayout, this$0.mScroller.getCurrY());
                ViewCompat.postOnAnimation(this.mLayout, this);
                return;
            }
            HeaderBehavior.this.onFlingFinished(this.mParent, this.mLayout);
        }
    }
}
