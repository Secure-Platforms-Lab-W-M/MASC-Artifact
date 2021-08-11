// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.view.MotionEvent;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;

public class SwipeDismissBehavior<V extends View> extends Behavior<V>
{
    private static final float DEFAULT_ALPHA_END_DISTANCE = 0.5f;
    private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0f;
    private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5f;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    public static final int SWIPE_DIRECTION_ANY = 2;
    public static final int SWIPE_DIRECTION_END_TO_START = 1;
    public static final int SWIPE_DIRECTION_START_TO_END = 0;
    float mAlphaEndSwipeDistance;
    float mAlphaStartSwipeDistance;
    private final ViewDragHelper.Callback mDragCallback;
    float mDragDismissThreshold;
    private boolean mInterceptingEvents;
    OnDismissListener mListener;
    private float mSensitivity;
    private boolean mSensitivitySet;
    int mSwipeDirection;
    ViewDragHelper mViewDragHelper;
    
    public SwipeDismissBehavior() {
        this.mSensitivity = 0.0f;
        this.mSwipeDirection = 2;
        this.mDragDismissThreshold = 0.5f;
        this.mAlphaStartSwipeDistance = 0.0f;
        this.mAlphaEndSwipeDistance = 0.5f;
        this.mDragCallback = new ViewDragHelper.Callback() {
            private static final int INVALID_POINTER_ID = -1;
            private int mActivePointerId = -1;
            private int mOriginalCapturedViewLeft;
            
            private boolean shouldDismiss(final View view, final float n) {
                final boolean b = false;
                final boolean b2 = false;
                boolean b3 = false;
                if (n == 0.0f) {
                    final int left = view.getLeft();
                    final int mOriginalCapturedViewLeft = this.mOriginalCapturedViewLeft;
                    final int round = Math.round(view.getWidth() * SwipeDismissBehavior.this.mDragDismissThreshold);
                    boolean b4 = b2;
                    if (Math.abs(left - mOriginalCapturedViewLeft) >= round) {
                        b4 = true;
                    }
                    return b4;
                }
                final boolean b5 = ViewCompat.getLayoutDirection(view) == 1;
                if (SwipeDismissBehavior.this.mSwipeDirection == 2) {
                    return true;
                }
                if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
                    if (b5) {
                        if (n >= 0.0f) {
                            return b3;
                        }
                    }
                    else if (n <= 0.0f) {
                        return b3;
                    }
                    b3 = true;
                    return b3;
                }
                if (SwipeDismissBehavior.this.mSwipeDirection == 1) {
                    if (b5) {
                        final boolean b6 = b;
                        if (n <= 0.0f) {
                            return b6;
                        }
                    }
                    else {
                        final boolean b6 = b;
                        if (n >= 0.0f) {
                            return b6;
                        }
                    }
                    return true;
                }
                return false;
            }
            
            @Override
            public int clampViewPositionHorizontal(final View view, final int n, int n2) {
                if (ViewCompat.getLayoutDirection(view) == 1) {
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
                int n3;
                if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
                    if (n2 != 0) {
                        n2 = this.mOriginalCapturedViewLeft - view.getWidth();
                        n3 = this.mOriginalCapturedViewLeft;
                    }
                    else {
                        n2 = this.mOriginalCapturedViewLeft;
                        n3 = this.mOriginalCapturedViewLeft + view.getWidth();
                    }
                }
                else if (SwipeDismissBehavior.this.mSwipeDirection == 1) {
                    if (n2 != 0) {
                        n2 = this.mOriginalCapturedViewLeft;
                        n3 = this.mOriginalCapturedViewLeft + view.getWidth();
                    }
                    else {
                        n2 = this.mOriginalCapturedViewLeft - view.getWidth();
                        n3 = this.mOriginalCapturedViewLeft;
                    }
                }
                else {
                    n2 = this.mOriginalCapturedViewLeft - view.getWidth();
                    n3 = this.mOriginalCapturedViewLeft + view.getWidth();
                }
                return SwipeDismissBehavior.clamp(n2, n, n3);
            }
            
            @Override
            public int clampViewPositionVertical(final View view, final int n, final int n2) {
                return view.getTop();
            }
            
            @Override
            public int getViewHorizontalDragRange(final View view) {
                return view.getWidth();
            }
            
            @Override
            public void onViewCaptured(final View view, final int mActivePointerId) {
                this.mActivePointerId = mActivePointerId;
                this.mOriginalCapturedViewLeft = view.getLeft();
                final ViewParent parent = view.getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
            }
            
            @Override
            public void onViewDragStateChanged(final int n) {
                if (SwipeDismissBehavior.this.mListener != null) {
                    SwipeDismissBehavior.this.mListener.onDragStateChanged(n);
                }
            }
            
            @Override
            public void onViewPositionChanged(final View view, final int n, final int n2, final int n3, final int n4) {
                final float n5 = this.mOriginalCapturedViewLeft + view.getWidth() * SwipeDismissBehavior.this.mAlphaStartSwipeDistance;
                final float n6 = this.mOriginalCapturedViewLeft + view.getWidth() * SwipeDismissBehavior.this.mAlphaEndSwipeDistance;
                if (n <= n5) {
                    view.setAlpha(1.0f);
                    return;
                }
                if (n >= n6) {
                    view.setAlpha(0.0f);
                    return;
                }
                view.setAlpha(SwipeDismissBehavior.clamp(0.0f, 1.0f - SwipeDismissBehavior.fraction(n5, n6, (float)n), 1.0f));
            }
            
            @Override
            public void onViewReleased(final View view, final float n, final float n2) {
                this.mActivePointerId = -1;
                final int width = view.getWidth();
                boolean b = false;
                int mOriginalCapturedViewLeft2;
                if (this.shouldDismiss(view, n)) {
                    final int left = view.getLeft();
                    final int mOriginalCapturedViewLeft = this.mOriginalCapturedViewLeft;
                    if (left < mOriginalCapturedViewLeft) {
                        mOriginalCapturedViewLeft2 = mOriginalCapturedViewLeft - width;
                    }
                    else {
                        mOriginalCapturedViewLeft2 = mOriginalCapturedViewLeft + width;
                    }
                    b = true;
                }
                else {
                    mOriginalCapturedViewLeft2 = this.mOriginalCapturedViewLeft;
                }
                if (SwipeDismissBehavior.this.mViewDragHelper.settleCapturedViewAt(mOriginalCapturedViewLeft2, view.getTop())) {
                    ViewCompat.postOnAnimation(view, new SettleRunnable(view, b));
                    return;
                }
                if (b && SwipeDismissBehavior.this.mListener != null) {
                    SwipeDismissBehavior.this.mListener.onDismiss(view);
                }
            }
            
            @Override
            public boolean tryCaptureView(final View view, final int n) {
                return this.mActivePointerId == -1 && SwipeDismissBehavior.this.canSwipeDismissView(view);
            }
        };
    }
    
    static float clamp(final float n, final float n2, final float n3) {
        return Math.min(Math.max(n, n2), n3);
    }
    
    static int clamp(final int n, final int n2, final int n3) {
        return Math.min(Math.max(n, n2), n3);
    }
    
    private void ensureViewDragHelper(final ViewGroup viewGroup) {
        if (this.mViewDragHelper == null) {
            ViewDragHelper mViewDragHelper;
            if (this.mSensitivitySet) {
                mViewDragHelper = ViewDragHelper.create(viewGroup, this.mSensitivity, this.mDragCallback);
            }
            else {
                mViewDragHelper = ViewDragHelper.create(viewGroup, this.mDragCallback);
            }
            this.mViewDragHelper = mViewDragHelper;
        }
    }
    
    static float fraction(final float n, final float n2, final float n3) {
        return (n3 - n) / (n2 - n);
    }
    
    public boolean canSwipeDismissView(@NonNull final View view) {
        return true;
    }
    
    public int getDragState() {
        final ViewDragHelper mViewDragHelper = this.mViewDragHelper;
        if (mViewDragHelper != null) {
            return mViewDragHelper.getViewDragState();
        }
        return 0;
    }
    
    @Override
    public boolean onInterceptTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        boolean b = this.mInterceptingEvents;
        final int actionMasked = motionEvent.getActionMasked();
        Label_0080: {
            if (actionMasked != 3) {
                switch (actionMasked) {
                    default: {
                        break Label_0080;
                    }
                    case 0: {
                        this.mInterceptingEvents = coordinatorLayout.isPointInChildBounds(v, (int)motionEvent.getX(), (int)motionEvent.getY());
                        b = this.mInterceptingEvents;
                        break Label_0080;
                    }
                    case 1: {
                        break;
                    }
                }
            }
            this.mInterceptingEvents = false;
        }
        if (b) {
            this.ensureViewDragHelper(coordinatorLayout);
            return this.mViewDragHelper.shouldInterceptTouchEvent(motionEvent);
        }
        return false;
    }
    
    @Override
    public boolean onTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        final ViewDragHelper mViewDragHelper = this.mViewDragHelper;
        if (mViewDragHelper != null) {
            mViewDragHelper.processTouchEvent(motionEvent);
            return true;
        }
        return false;
    }
    
    public void setDragDismissDistance(final float n) {
        this.mDragDismissThreshold = clamp(0.0f, n, 1.0f);
    }
    
    public void setEndAlphaSwipeDistance(final float n) {
        this.mAlphaEndSwipeDistance = clamp(0.0f, n, 1.0f);
    }
    
    public void setListener(final OnDismissListener mListener) {
        this.mListener = mListener;
    }
    
    public void setSensitivity(final float mSensitivity) {
        this.mSensitivity = mSensitivity;
        this.mSensitivitySet = true;
    }
    
    public void setStartAlphaSwipeDistance(final float n) {
        this.mAlphaStartSwipeDistance = clamp(0.0f, n, 1.0f);
    }
    
    public void setSwipeDirection(final int mSwipeDirection) {
        this.mSwipeDirection = mSwipeDirection;
    }
    
    public interface OnDismissListener
    {
        void onDismiss(final View p0);
        
        void onDragStateChanged(final int p0);
    }
    
    private class SettleRunnable implements Runnable
    {
        private final boolean mDismiss;
        private final View mView;
        
        SettleRunnable(final View mView, final boolean mDismiss) {
            this.mView = mView;
            this.mDismiss = mDismiss;
        }
        
        @Override
        public void run() {
            if (SwipeDismissBehavior.this.mViewDragHelper != null && SwipeDismissBehavior.this.mViewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.mView, this);
                return;
            }
            if (this.mDismiss && SwipeDismissBehavior.this.mListener != null) {
                SwipeDismissBehavior.this.mListener.onDismiss(this.mView);
            }
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    private @interface SwipeDirection {
    }
}
