/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewParent
 *  android.view.animation.Interpolator
 *  android.widget.OverScroller
 */
package android.support.v4.widget;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import java.util.Arrays;

public class ViewDragHelper {
    private static final int BASE_SETTLE_DURATION = 256;
    public static final int DIRECTION_ALL = 3;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int EDGE_ALL = 15;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int EDGE_SIZE = 20;
    public static final int EDGE_TOP = 4;
    public static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "ViewDragHelper";
    private static final Interpolator sInterpolator = new Interpolator(){

        public float getInterpolation(float f) {
            return f * f * f * f * (f -= 1.0f) + 1.0f;
        }
    };
    private int mActivePointerId = -1;
    private final Callback mCallback;
    private View mCapturedView;
    private int mDragState;
    private int[] mEdgeDragsInProgress;
    private int[] mEdgeDragsLocked;
    private int mEdgeSize;
    private int[] mInitialEdgesTouched;
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;
    private float mMaxVelocity;
    private float mMinVelocity;
    private final ViewGroup mParentView;
    private int mPointersDown;
    private boolean mReleaseInProgress;
    private OverScroller mScroller;
    private final Runnable mSetIdleRunnable;
    private int mTouchSlop;
    private int mTrackingEdges;
    private VelocityTracker mVelocityTracker;

    private ViewDragHelper(Context context, ViewGroup viewGroup, Callback callback) {
        this.mSetIdleRunnable = new Runnable(){

            @Override
            public void run() {
                ViewDragHelper.this.setDragState(0);
            }
        };
        if (viewGroup != null) {
            if (callback != null) {
                this.mParentView = viewGroup;
                this.mCallback = callback;
                viewGroup = ViewConfiguration.get((Context)context);
                this.mEdgeSize = (int)(20.0f * context.getResources().getDisplayMetrics().density + 0.5f);
                this.mTouchSlop = viewGroup.getScaledTouchSlop();
                this.mMaxVelocity = viewGroup.getScaledMaximumFlingVelocity();
                this.mMinVelocity = viewGroup.getScaledMinimumFlingVelocity();
                this.mScroller = new OverScroller(context, sInterpolator);
                return;
            }
            throw new IllegalArgumentException("Callback may not be null");
        }
        throw new IllegalArgumentException("Parent view may not be null");
    }

    private boolean checkNewEdgeDrag(float f, float f2, int n, int n2) {
        f = Math.abs(f);
        f2 = Math.abs(f2);
        int n3 = this.mInitialEdgesTouched[n];
        boolean bl = false;
        if ((n3 & n2) == n2 && (this.mTrackingEdges & n2) != 0 && (this.mEdgeDragsLocked[n] & n2) != n2 && (this.mEdgeDragsInProgress[n] & n2) != n2) {
            n3 = this.mTouchSlop;
            if (f <= (float)n3 && f2 <= (float)n3) {
                return false;
            }
            if (f < 0.5f * f2 && this.mCallback.onEdgeLock(n2)) {
                int[] arrn = this.mEdgeDragsLocked;
                arrn[n] = arrn[n] | n2;
                return false;
            }
            boolean bl2 = bl;
            if ((this.mEdgeDragsInProgress[n] & n2) == 0) {
                bl2 = bl;
                if (f > (float)this.mTouchSlop) {
                    bl2 = true;
                }
            }
            return bl2;
        }
        return false;
    }

    private boolean checkTouchSlop(View view, float f, float f2) {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        if (view == null) {
            return false;
        }
        int n = this.mCallback.getViewHorizontalDragRange(view) > 0 ? 1 : 0;
        boolean bl4 = this.mCallback.getViewVerticalDragRange(view) > 0;
        if (n != 0 && bl4) {
            n = this.mTouchSlop;
            if (f * f + f2 * f2 > (float)(n * n)) {
                bl3 = true;
            }
            return bl3;
        }
        if (n != 0) {
            bl3 = bl;
            if (Math.abs(f) > (float)this.mTouchSlop) {
                bl3 = true;
            }
            return bl3;
        }
        if (bl4) {
            bl3 = bl2;
            if (Math.abs(f2) > (float)this.mTouchSlop) {
                bl3 = true;
            }
            return bl3;
        }
        return false;
    }

    private float clampMag(float f, float f2, float f3) {
        float f4 = Math.abs(f);
        if (f4 < f2) {
            return 0.0f;
        }
        if (f4 > f3) {
            if (f > 0.0f) {
                return f3;
            }
            return - f3;
        }
        return f;
    }

    private int clampMag(int n, int n2, int n3) {
        int n4 = Math.abs(n);
        if (n4 < n2) {
            return 0;
        }
        if (n4 > n3) {
            if (n > 0) {
                return n3;
            }
            return - n3;
        }
        return n;
    }

    private void clearMotionHistory() {
        float[] arrf = this.mInitialMotionX;
        if (arrf == null) {
            return;
        }
        Arrays.fill(arrf, 0.0f);
        Arrays.fill(this.mInitialMotionY, 0.0f);
        Arrays.fill(this.mLastMotionX, 0.0f);
        Arrays.fill(this.mLastMotionY, 0.0f);
        Arrays.fill(this.mInitialEdgesTouched, 0);
        Arrays.fill(this.mEdgeDragsInProgress, 0);
        Arrays.fill(this.mEdgeDragsLocked, 0);
        this.mPointersDown = 0;
    }

    private void clearMotionHistory(int n) {
        if (this.mInitialMotionX != null) {
            if (!this.isPointerDown(n)) {
                return;
            }
            this.mInitialMotionX[n] = 0.0f;
            this.mInitialMotionY[n] = 0.0f;
            this.mLastMotionX[n] = 0.0f;
            this.mLastMotionY[n] = 0.0f;
            this.mInitialEdgesTouched[n] = 0;
            this.mEdgeDragsInProgress[n] = 0;
            this.mEdgeDragsLocked[n] = 0;
            this.mPointersDown &= ~ (1 << n);
            return;
        }
    }

    private int computeAxisDuration(int n, int n2, int n3) {
        if (n == 0) {
            return 0;
        }
        int n4 = this.mParentView.getWidth();
        int n5 = n4 / 2;
        float f = Math.min(1.0f, (float)Math.abs(n) / (float)n4);
        float f2 = n5;
        float f3 = n5;
        f = this.distanceInfluenceForSnapDuration(f);
        n = (n2 = Math.abs(n2)) > 0 ? Math.round(Math.abs((f2 + f3 * f) / (float)n2) * 1000.0f) * 4 : (int)((1.0f + (float)Math.abs(n) / (float)n3) * 256.0f);
        return Math.min(n, 600);
    }

    private int computeSettleDuration(View view, int n, int n2, int n3, int n4) {
        float f;
        float f2;
        n3 = this.clampMag(n3, (int)this.mMinVelocity, (int)this.mMaxVelocity);
        n4 = this.clampMag(n4, (int)this.mMinVelocity, (int)this.mMaxVelocity);
        int n5 = Math.abs(n);
        int n6 = Math.abs(n2);
        int n7 = Math.abs(n3);
        int n8 = Math.abs(n4);
        int n9 = n7 + n8;
        int n10 = n5 + n6;
        if (n3 != 0) {
            f2 = n7;
            f = n9;
        } else {
            f2 = n5;
            f = n10;
        }
        float f3 = f2 / f;
        if (n4 != 0) {
            f2 = n8;
            f = n9;
        } else {
            f2 = n6;
            f = n10;
        }
        n = this.computeAxisDuration(n, n3, this.mCallback.getViewHorizontalDragRange(view));
        n2 = this.computeAxisDuration(n2, n4, this.mCallback.getViewVerticalDragRange(view));
        return (int)((float)n * f3 + (float)n2 * (f2 /= f));
    }

    public static ViewDragHelper create(ViewGroup object, float f, Callback callback) {
        object = ViewDragHelper.create((ViewGroup)object, callback);
        object.mTouchSlop = (int)((float)object.mTouchSlop * (1.0f / f));
        return object;
    }

    public static ViewDragHelper create(ViewGroup viewGroup, Callback callback) {
        return new ViewDragHelper(viewGroup.getContext(), viewGroup, callback);
    }

    private void dispatchViewReleased(float f, float f2) {
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, f, f2);
        this.mReleaseInProgress = false;
        if (this.mDragState == 1) {
            this.setDragState(0);
            return;
        }
    }

    private float distanceInfluenceForSnapDuration(float f) {
        return (float)Math.sin((f - 0.5f) * 0.47123894f);
    }

    private void dragTo(int n, int n2, int n3, int n4) {
        int n5 = n;
        int n6 = n2;
        int n7 = this.mCapturedView.getLeft();
        int n8 = this.mCapturedView.getTop();
        if (n3 != 0) {
            n = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, n, n3);
            ViewCompat.offsetLeftAndRight(this.mCapturedView, n - n7);
        } else {
            n = n5;
        }
        if (n4 != 0) {
            n6 = this.mCallback.clampViewPositionVertical(this.mCapturedView, n2, n4);
            ViewCompat.offsetTopAndBottom(this.mCapturedView, n6 - n8);
        }
        if (n3 == 0 && n4 == 0) {
            return;
        }
        this.mCallback.onViewPositionChanged(this.mCapturedView, n, n6, n - n7, n6 - n8);
    }

    private void ensureMotionHistorySizeForId(int n) {
        float[] arrf = this.mInitialMotionX;
        if (arrf != null && arrf.length > n) {
            return;
        }
        arrf = new float[n + 1];
        float[] arrf2 = new float[n + 1];
        float[] arrf3 = new float[n + 1];
        float[] arrf4 = new float[n + 1];
        int[] arrn = new int[n + 1];
        int[] arrn2 = new int[n + 1];
        int[] arrn3 = new int[n + 1];
        float[] arrf5 = this.mInitialMotionX;
        if (arrf5 != null) {
            System.arraycopy(arrf5, 0, arrf, 0, arrf5.length);
            arrf5 = this.mInitialMotionY;
            System.arraycopy(arrf5, 0, arrf2, 0, arrf5.length);
            arrf5 = this.mLastMotionX;
            System.arraycopy(arrf5, 0, arrf3, 0, arrf5.length);
            arrf5 = this.mLastMotionY;
            System.arraycopy(arrf5, 0, arrf4, 0, arrf5.length);
            arrf5 = this.mInitialEdgesTouched;
            System.arraycopy(arrf5, 0, arrn, 0, arrf5.length);
            arrf5 = this.mEdgeDragsInProgress;
            System.arraycopy(arrf5, 0, arrn2, 0, arrf5.length);
            arrf5 = this.mEdgeDragsLocked;
            System.arraycopy(arrf5, 0, arrn3, 0, arrf5.length);
        }
        this.mInitialMotionX = arrf;
        this.mInitialMotionY = arrf2;
        this.mLastMotionX = arrf3;
        this.mLastMotionY = arrf4;
        this.mInitialEdgesTouched = arrn;
        this.mEdgeDragsInProgress = arrn2;
        this.mEdgeDragsLocked = arrn3;
    }

    private boolean forceSettleCapturedViewAt(int n, int n2, int n3, int n4) {
        int n5 = this.mCapturedView.getLeft();
        int n6 = this.mCapturedView.getTop();
        if ((n -= n5) == 0 && (n2 -= n6) == 0) {
            this.mScroller.abortAnimation();
            this.setDragState(0);
            return false;
        }
        n3 = this.computeSettleDuration(this.mCapturedView, n, n2, n3, n4);
        this.mScroller.startScroll(n5, n6, n, n2, n3);
        this.setDragState(2);
        return true;
    }

    private int getEdgesTouched(int n, int n2) {
        int n3 = 0;
        if (n < this.mParentView.getLeft() + this.mEdgeSize) {
            n3 = false | true;
        }
        int n4 = n3;
        if (n2 < this.mParentView.getTop() + this.mEdgeSize) {
            n4 = n3 | 4;
        }
        n3 = n4;
        if (n > this.mParentView.getRight() - this.mEdgeSize) {
            n3 = n4 | 2;
        }
        n = n3;
        if (n2 > this.mParentView.getBottom() - this.mEdgeSize) {
            n = n3 | 8;
        }
        return n;
    }

    private boolean isValidPointerForActionMove(int n) {
        if (!this.isPointerDown(n)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Ignoring pointerId=");
            stringBuilder.append(n);
            stringBuilder.append(" because ACTION_DOWN was not received ");
            stringBuilder.append("for this pointer before ACTION_MOVE. It likely happened because ");
            stringBuilder.append(" ViewDragHelper did not receive all the events in the event stream.");
            Log.e((String)"ViewDragHelper", (String)stringBuilder.toString());
            return false;
        }
        return true;
    }

    private void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        this.dispatchViewReleased(this.clampMag(this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), this.clampMag(this.mVelocityTracker.getYVelocity(this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
    }

    private void reportNewEdgeDrags(float f, float f2, int n) {
        int n2 = 0;
        if (this.checkNewEdgeDrag(f, f2, n, 1)) {
            n2 = false | true;
        }
        if (this.checkNewEdgeDrag(f2, f, n, 4)) {
            n2 |= 4;
        }
        if (this.checkNewEdgeDrag(f, f2, n, 2)) {
            n2 |= 2;
        }
        if (this.checkNewEdgeDrag(f2, f, n, 8)) {
            n2 |= 8;
        }
        if (n2 != 0) {
            int[] arrn = this.mEdgeDragsInProgress;
            arrn[n] = arrn[n] | n2;
            this.mCallback.onEdgeDragStarted(n2, n);
            return;
        }
    }

    private void saveInitialMotion(float f, float f2, int n) {
        this.ensureMotionHistorySizeForId(n);
        float[] arrf = this.mInitialMotionX;
        this.mLastMotionX[n] = f;
        arrf[n] = f;
        arrf = this.mInitialMotionY;
        this.mLastMotionY[n] = f2;
        arrf[n] = f2;
        this.mInitialEdgesTouched[n] = this.getEdgesTouched((int)f, (int)f2);
        this.mPointersDown |= 1 << n;
    }

    private void saveLastMotion(MotionEvent motionEvent) {
        int n = motionEvent.getPointerCount();
        for (int i = 0; i < n; ++i) {
            int n2 = motionEvent.getPointerId(i);
            if (!this.isValidPointerForActionMove(n2)) continue;
            float f = motionEvent.getX(i);
            float f2 = motionEvent.getY(i);
            this.mLastMotionX[n2] = f;
            this.mLastMotionY[n2] = f2;
        }
    }

    public void abort() {
        this.cancel();
        if (this.mDragState == 2) {
            int n = this.mScroller.getCurrX();
            int n2 = this.mScroller.getCurrY();
            this.mScroller.abortAnimation();
            int n3 = this.mScroller.getCurrX();
            int n4 = this.mScroller.getCurrY();
            this.mCallback.onViewPositionChanged(this.mCapturedView, n3, n4, n3 - n, n4 - n2);
        }
        this.setDragState(0);
    }

    protected boolean canScroll(View view, boolean bl, int n, int n2, int n3, int n4) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)view;
            int n5 = view.getScrollX();
            int n6 = view.getScrollY();
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                View view2 = viewGroup.getChildAt(i);
                if (n3 + n5 < view2.getLeft() || n3 + n5 >= view2.getRight() || n4 + n6 < view2.getTop() || n4 + n6 >= view2.getBottom() || !this.canScroll(view2, true, n, n2, n3 + n5 - view2.getLeft(), n4 + n6 - view2.getTop())) continue;
                return true;
            }
        }
        if (bl && (view.canScrollHorizontally(- n) || view.canScrollVertically(- n2))) {
            return true;
        }
        return false;
    }

    public void cancel() {
        this.mActivePointerId = -1;
        this.clearMotionHistory();
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
            return;
        }
    }

    public void captureChildView(View object, int n) {
        if (object.getParent() == this.mParentView) {
            this.mCapturedView = object;
            this.mActivePointerId = n;
            this.mCallback.onViewCaptured((View)object, n);
            this.setDragState(1);
            return;
        }
        object = new StringBuilder();
        object.append("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (");
        object.append((Object)this.mParentView);
        object.append(")");
        throw new IllegalArgumentException(object.toString());
    }

    public boolean checkTouchSlop(int n) {
        int n2 = this.mInitialMotionX.length;
        for (int i = 0; i < n2; ++i) {
            if (!this.checkTouchSlop(n, i)) continue;
            return true;
        }
        return false;
    }

    public boolean checkTouchSlop(int n, int n2) {
        boolean bl = this.isPointerDown(n2);
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        if (!bl) {
            return false;
        }
        boolean bl5 = (n & 1) == 1;
        n = (n & 2) == 2 ? 1 : 0;
        float f = this.mLastMotionX[n2] - this.mInitialMotionX[n2];
        float f2 = this.mLastMotionY[n2] - this.mInitialMotionY[n2];
        if (bl5 && n != 0) {
            n = this.mTouchSlop;
            if (f * f + f2 * f2 > (float)(n * n)) {
                bl4 = true;
            }
            return bl4;
        }
        if (bl5) {
            bl4 = bl2;
            if (Math.abs(f) > (float)this.mTouchSlop) {
                bl4 = true;
            }
            return bl4;
        }
        if (n != 0) {
            bl4 = bl3;
            if (Math.abs(f2) > (float)this.mTouchSlop) {
                bl4 = true;
            }
            return bl4;
        }
        return false;
    }

    public boolean continueSettling(boolean bl) {
        int n = this.mDragState;
        boolean bl2 = false;
        if (n == 2) {
            boolean bl3 = this.mScroller.computeScrollOffset();
            n = this.mScroller.getCurrX();
            int n2 = this.mScroller.getCurrY();
            int n3 = n - this.mCapturedView.getLeft();
            int n4 = n2 - this.mCapturedView.getTop();
            if (n3 != 0) {
                ViewCompat.offsetLeftAndRight(this.mCapturedView, n3);
            }
            if (n4 != 0) {
                ViewCompat.offsetTopAndBottom(this.mCapturedView, n4);
            }
            if (n3 != 0 || n4 != 0) {
                this.mCallback.onViewPositionChanged(this.mCapturedView, n, n2, n3, n4);
            }
            if (bl3 && n == this.mScroller.getFinalX() && n2 == this.mScroller.getFinalY()) {
                this.mScroller.abortAnimation();
                bl3 = false;
            }
            if (!bl3) {
                if (bl) {
                    this.mParentView.post(this.mSetIdleRunnable);
                } else {
                    this.setDragState(0);
                }
            }
        }
        bl = bl2;
        if (this.mDragState == 2) {
            bl = true;
        }
        return bl;
    }

    public View findTopChildUnder(int n, int n2) {
        for (int i = this.mParentView.getChildCount() - 1; i >= 0; --i) {
            View view = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(i));
            if (n < view.getLeft() || n >= view.getRight() || n2 < view.getTop() || n2 >= view.getBottom()) continue;
            return view;
        }
        return null;
    }

    public void flingCapturedView(int n, int n2, int n3, int n4) {
        if (this.mReleaseInProgress) {
            this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId), n, n3, n2, n4);
            this.setDragState(2);
            return;
        }
        throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
    }

    public int getActivePointerId() {
        return this.mActivePointerId;
    }

    public View getCapturedView() {
        return this.mCapturedView;
    }

    public int getEdgeSize() {
        return this.mEdgeSize;
    }

    public float getMinVelocity() {
        return this.mMinVelocity;
    }

    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public int getViewDragState() {
        return this.mDragState;
    }

    public boolean isCapturedViewUnder(int n, int n2) {
        return this.isViewUnder(this.mCapturedView, n, n2);
    }

    public boolean isEdgeTouched(int n) {
        int n2 = this.mInitialEdgesTouched.length;
        for (int i = 0; i < n2; ++i) {
            if (!this.isEdgeTouched(n, i)) continue;
            return true;
        }
        return false;
    }

    public boolean isEdgeTouched(int n, int n2) {
        if (this.isPointerDown(n2) && (this.mInitialEdgesTouched[n2] & n) != 0) {
            return true;
        }
        return false;
    }

    public boolean isPointerDown(int n) {
        if ((this.mPointersDown & 1 << n) != 0) {
            return true;
        }
        return false;
    }

    public boolean isViewUnder(View view, int n, int n2) {
        if (view == null) {
            return false;
        }
        if (n >= view.getLeft() && n < view.getRight() && n2 >= view.getTop() && n2 < view.getBottom()) {
            return true;
        }
        return false;
    }

    public void processTouchEvent(MotionEvent motionEvent) {
        int n = motionEvent.getActionMasked();
        int n2 = motionEvent.getActionIndex();
        if (n == 0) {
            this.cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        switch (n) {
            default: {
                return;
            }
            case 6: {
                int n3 = motionEvent.getPointerId(n2);
                if (this.mDragState == 1 && n3 == this.mActivePointerId) {
                    int n4 = -1;
                    int n5 = motionEvent.getPointerCount();
                    n = 0;
                    do {
                        float f;
                        View view;
                        float f2;
                        View view2;
                        n2 = n4;
                        if (n >= n5) break;
                        n2 = motionEvent.getPointerId(n);
                        if (n2 != this.mActivePointerId && (view = this.findTopChildUnder((int)(f = motionEvent.getX(n)), (int)(f2 = motionEvent.getY(n)))) == (view2 = this.mCapturedView) && this.tryCaptureViewForDrag(view2, n2)) {
                            n2 = this.mActivePointerId;
                            break;
                        }
                        ++n;
                    } while (true);
                    if (n2 == -1) {
                        this.releaseViewForPointerUp();
                    }
                }
                this.clearMotionHistory(n3);
                return;
            }
            case 5: {
                n = motionEvent.getPointerId(n2);
                float f = motionEvent.getX(n2);
                float f3 = motionEvent.getY(n2);
                this.saveInitialMotion(f, f3, n);
                if (this.mDragState == 0) {
                    this.tryCaptureViewForDrag(this.findTopChildUnder((int)f, (int)f3), n);
                    n2 = this.mInitialEdgesTouched[n];
                    int n6 = this.mTrackingEdges;
                    if ((n2 & n6) != 0) {
                        this.mCallback.onEdgeTouched(n6 & n2, n);
                    }
                } else if (this.isCapturedViewUnder((int)f, (int)f3)) {
                    this.tryCaptureViewForDrag(this.mCapturedView, n);
                    return;
                }
                return;
            }
            case 3: {
                if (this.mDragState == 1) {
                    this.dispatchViewReleased(0.0f, 0.0f);
                }
                this.cancel();
                return;
            }
            case 2: {
                if (this.mDragState == 1) {
                    if (!this.isValidPointerForActionMove(this.mActivePointerId)) {
                        return;
                    }
                    n = motionEvent.findPointerIndex(this.mActivePointerId);
                    float f = motionEvent.getX(n);
                    float f4 = motionEvent.getY(n);
                    float[] arrf = this.mLastMotionX;
                    n2 = this.mActivePointerId;
                    n = (int)(f - arrf[n2]);
                    n2 = (int)(f4 - this.mLastMotionY[n2]);
                    this.dragTo(this.mCapturedView.getLeft() + n, this.mCapturedView.getTop() + n2, n, n2);
                    this.saveLastMotion(motionEvent);
                    return;
                }
                n2 = motionEvent.getPointerCount();
                for (n = 0; n < n2; ++n) {
                    View view;
                    int n7 = motionEvent.getPointerId(n);
                    if (!this.isValidPointerForActionMove(n7)) continue;
                    float f = motionEvent.getX(n);
                    float f5 = motionEvent.getY(n);
                    float f6 = f - this.mInitialMotionX[n7];
                    float f7 = f5 - this.mInitialMotionY[n7];
                    this.reportNewEdgeDrags(f6, f7, n7);
                    if (this.mDragState == 1 || this.checkTouchSlop(view = this.findTopChildUnder((int)f, (int)f5), f6, f7) && this.tryCaptureViewForDrag(view, n7)) break;
                }
                this.saveLastMotion(motionEvent);
                return;
            }
            case 1: {
                if (this.mDragState == 1) {
                    this.releaseViewForPointerUp();
                }
                this.cancel();
                return;
            }
            case 0: 
        }
        float f = motionEvent.getX();
        float f8 = motionEvent.getY();
        n = motionEvent.getPointerId(0);
        motionEvent = this.findTopChildUnder((int)f, (int)f8);
        this.saveInitialMotion(f, f8, n);
        this.tryCaptureViewForDrag((View)motionEvent, n);
        n2 = this.mInitialEdgesTouched[n];
        int n8 = this.mTrackingEdges;
        if ((n2 & n8) != 0) {
            this.mCallback.onEdgeTouched(n8 & n2, n);
            return;
        }
    }

    void setDragState(int n) {
        this.mParentView.removeCallbacks(this.mSetIdleRunnable);
        if (this.mDragState != n) {
            this.mDragState = n;
            this.mCallback.onViewDragStateChanged(n);
            if (this.mDragState == 0) {
                this.mCapturedView = null;
                return;
            }
            return;
        }
    }

    public void setEdgeTrackingEnabled(int n) {
        this.mTrackingEdges = n;
    }

    public void setMinVelocity(float f) {
        this.mMinVelocity = f;
    }

    public boolean settleCapturedViewAt(int n, int n2) {
        if (this.mReleaseInProgress) {
            return this.forceSettleCapturedViewAt(n, n2, (int)this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int)this.mVelocityTracker.getYVelocity(this.mActivePointerId));
        }
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    }

    public boolean shouldInterceptTouchEvent(MotionEvent motionEvent) {
        int n = motionEvent.getActionMasked();
        int n2 = motionEvent.getActionIndex();
        if (n == 0) {
            this.cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        switch (n) {
            default: {
                break;
            }
            case 6: {
                this.clearMotionHistory(motionEvent.getPointerId(n2));
                break;
            }
            case 5: {
                int n3 = motionEvent.getPointerId(n2);
                float f = motionEvent.getX(n2);
                float f2 = motionEvent.getY(n2);
                this.saveInitialMotion(f, f2, n3);
                n2 = this.mDragState;
                if (n2 == 0) {
                    n2 = this.mInitialEdgesTouched[n3];
                    n = this.mTrackingEdges;
                    if ((n2 & n) == 0) break;
                    this.mCallback.onEdgeTouched(n & n2, n3);
                    break;
                }
                if (n2 != 2 || (motionEvent = this.findTopChildUnder((int)f, (int)f2)) != this.mCapturedView) break;
                this.tryCaptureViewForDrag((View)motionEvent, n3);
                break;
            }
            case 2: {
                if (this.mInitialMotionX == null || this.mInitialMotionY == null) break;
                int n4 = motionEvent.getPointerCount();
                for (int i = 0; i < n4; ++i) {
                    int n5 = motionEvent.getPointerId(i);
                    if (!this.isValidPointerForActionMove(n5)) continue;
                    float f = motionEvent.getX(i);
                    float f3 = motionEvent.getY(i);
                    float f4 = f - this.mInitialMotionX[n5];
                    float f5 = f3 - this.mInitialMotionY[n5];
                    View view = this.findTopChildUnder((int)f, (int)f3);
                    boolean bl = view != null && this.checkTouchSlop(view, f4, f5);
                    if (bl) {
                        int n6 = view.getLeft();
                        int n7 = (int)f4;
                        n7 = this.mCallback.clampViewPositionHorizontal(view, n7 + n6, (int)f4);
                        int n8 = view.getTop();
                        int n9 = (int)f5;
                        n9 = this.mCallback.clampViewPositionVertical(view, n9 + n8, (int)f5);
                        int n10 = this.mCallback.getViewHorizontalDragRange(view);
                        int n11 = this.mCallback.getViewVerticalDragRange(view);
                        if (n10 == 0 || n10 > 0 && n7 == n6) {
                            if (n11 == 0 || n11 > 0 && n9 == n8) break;
                        }
                    }
                    this.reportNewEdgeDrags(f4, f5, n5);
                    if (this.mDragState == 1 || bl && this.tryCaptureViewForDrag(view, n5)) break;
                }
                this.saveLastMotion(motionEvent);
                break;
            }
            case 1: 
            case 3: {
                this.cancel();
                break;
            }
            case 0: {
                float f = motionEvent.getX();
                float f6 = motionEvent.getY();
                int n12 = motionEvent.getPointerId(0);
                this.saveInitialMotion(f, f6, n12);
                motionEvent = this.findTopChildUnder((int)f, (int)f6);
                if (motionEvent == this.mCapturedView && this.mDragState == 2) {
                    this.tryCaptureViewForDrag((View)motionEvent, n12);
                }
                if (((n2 = this.mInitialEdgesTouched[n12]) & (n = this.mTrackingEdges)) == 0) break;
                this.mCallback.onEdgeTouched(n & n2, n12);
                break;
            }
        }
        if (this.mDragState == 1) {
            return true;
        }
        return false;
    }

    public boolean smoothSlideViewTo(View view, int n, int n2) {
        this.mCapturedView = view;
        this.mActivePointerId = -1;
        boolean bl = this.forceSettleCapturedViewAt(n, n2, 0, 0);
        if (!bl && this.mDragState == 0 && this.mCapturedView != null) {
            this.mCapturedView = null;
            return bl;
        }
        return bl;
    }

    boolean tryCaptureViewForDrag(View view, int n) {
        if (view == this.mCapturedView && this.mActivePointerId == n) {
            return true;
        }
        if (view != null && this.mCallback.tryCaptureView(view, n)) {
            this.mActivePointerId = n;
            this.captureChildView(view, n);
            return true;
        }
        return false;
    }

    public static abstract class Callback {
        public int clampViewPositionHorizontal(View view, int n, int n2) {
            return 0;
        }

        public int clampViewPositionVertical(View view, int n, int n2) {
            return 0;
        }

        public int getOrderedChildIndex(int n) {
            return n;
        }

        public int getViewHorizontalDragRange(View view) {
            return 0;
        }

        public int getViewVerticalDragRange(View view) {
            return 0;
        }

        public void onEdgeDragStarted(int n, int n2) {
        }

        public boolean onEdgeLock(int n) {
            return false;
        }

        public void onEdgeTouched(int n, int n2) {
        }

        public void onViewCaptured(View view, int n) {
        }

        public void onViewDragStateChanged(int n) {
        }

        public void onViewPositionChanged(View view, int n, int n2, int n3, int n4) {
        }

        public void onViewReleased(View view, float f, float f2) {
        }

        public abstract boolean tryCaptureView(View var1, int var2);
    }

}

