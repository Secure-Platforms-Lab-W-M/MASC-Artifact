/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.SystemClock
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.ViewConfiguration
 */
package androidx.viewpager2.widget;

import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ScrollEventAdapter;
import androidx.viewpager2.widget.ViewPager2;

final class FakeDrag {
    private int mActualDraggedDistance;
    private long mFakeDragBeginTime;
    private int mMaximumVelocity;
    private final RecyclerView mRecyclerView;
    private float mRequestedDragDistance;
    private final ScrollEventAdapter mScrollEventAdapter;
    private VelocityTracker mVelocityTracker;
    private final ViewPager2 mViewPager;

    FakeDrag(ViewPager2 viewPager2, ScrollEventAdapter scrollEventAdapter, RecyclerView recyclerView) {
        this.mViewPager = viewPager2;
        this.mScrollEventAdapter = scrollEventAdapter;
        this.mRecyclerView = recyclerView;
    }

    private void addFakeMotionEvent(long l, int n, float f, float f2) {
        MotionEvent motionEvent = MotionEvent.obtain((long)this.mFakeDragBeginTime, (long)l, (int)n, (float)f, (float)f2, (int)0);
        this.mVelocityTracker.addMovement(motionEvent);
        motionEvent.recycle();
    }

    private void beginFakeVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
            this.mMaximumVelocity = ViewConfiguration.get((Context)this.mViewPager.getContext()).getScaledMaximumFlingVelocity();
            return;
        }
        velocityTracker.clear();
    }

    boolean beginFakeDrag() {
        if (this.mScrollEventAdapter.isDragging()) {
            return false;
        }
        this.mActualDraggedDistance = 0;
        this.mRequestedDragDistance = (float)false;
        this.mFakeDragBeginTime = SystemClock.uptimeMillis();
        this.beginFakeVelocityTracker();
        this.mScrollEventAdapter.notifyBeginFakeDrag();
        if (!this.mScrollEventAdapter.isIdle()) {
            this.mRecyclerView.stopScroll();
        }
        this.addFakeMotionEvent(this.mFakeDragBeginTime, 0, 0.0f, 0.0f);
        return true;
    }

    boolean endFakeDrag() {
        if (!this.mScrollEventAdapter.isFakeDragging()) {
            return false;
        }
        this.mScrollEventAdapter.notifyEndFakeDrag();
        VelocityTracker velocityTracker = this.mVelocityTracker;
        velocityTracker.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
        int n = (int)velocityTracker.getXVelocity();
        int n2 = (int)velocityTracker.getYVelocity();
        if (!this.mRecyclerView.fling(n, n2)) {
            this.mViewPager.snapToPage();
        }
        return true;
    }

    boolean fakeDragBy(float f) {
        boolean bl = this.mScrollEventAdapter.isFakeDragging();
        int n = 0;
        if (!bl) {
            return false;
        }
        this.mRequestedDragDistance = f = this.mRequestedDragDistance - f;
        int n2 = Math.round(f - (float)this.mActualDraggedDistance);
        this.mActualDraggedDistance += n2;
        long l = SystemClock.uptimeMillis();
        boolean bl2 = this.mViewPager.getOrientation() == 0;
        int n3 = bl2 ? n2 : 0;
        if (bl2) {
            n2 = n;
        }
        f = bl2 ? this.mRequestedDragDistance : 0.0f;
        float f2 = bl2 ? 0.0f : this.mRequestedDragDistance;
        this.mRecyclerView.scrollBy(n3, n2);
        this.addFakeMotionEvent(l, 2, f, f2);
        return true;
    }

    boolean isFakeDragging() {
        return this.mScrollEventAdapter.isFakeDragging();
    }
}

