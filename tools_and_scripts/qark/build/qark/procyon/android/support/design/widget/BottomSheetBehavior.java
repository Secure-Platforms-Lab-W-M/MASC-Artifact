// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.os.Parcel;
import android.os.Parcelable$ClassLoaderCreator;
import android.os.Parcelable$Creator;
import android.support.v4.view.AbsSavedState;
import android.support.annotation.NonNull;
import android.view.ViewParent;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.support.annotation.VisibleForTesting;
import android.view.ViewGroup;
import android.view.ViewGroup$LayoutParams;
import android.util.TypedValue;
import android.content.res.TypedArray;
import android.view.ViewConfiguration;
import android.support.design.R;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.math.MathUtils;
import android.view.VelocityTracker;
import java.lang.ref.WeakReference;
import android.support.v4.widget.ViewDragHelper;
import android.view.View;

public class BottomSheetBehavior<V extends View> extends Behavior<V>
{
    private static final float HIDE_FRICTION = 0.1f;
    private static final float HIDE_THRESHOLD = 0.5f;
    public static final int PEEK_HEIGHT_AUTO = -1;
    public static final int STATE_COLLAPSED = 4;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_EXPANDED = 3;
    public static final int STATE_HIDDEN = 5;
    public static final int STATE_SETTLING = 2;
    int mActivePointerId;
    private BottomSheetCallback mCallback;
    private final ViewDragHelper.Callback mDragCallback;
    boolean mHideable;
    private boolean mIgnoreEvents;
    private int mInitialY;
    private int mLastNestedScrollDy;
    int mMaxOffset;
    private float mMaximumVelocity;
    int mMinOffset;
    private boolean mNestedScrolled;
    WeakReference<View> mNestedScrollingChildRef;
    int mParentHeight;
    private int mPeekHeight;
    private boolean mPeekHeightAuto;
    private int mPeekHeightMin;
    private boolean mSkipCollapsed;
    int mState;
    boolean mTouchingScrollingChild;
    private VelocityTracker mVelocityTracker;
    ViewDragHelper mViewDragHelper;
    WeakReference<V> mViewRef;
    
    public BottomSheetBehavior() {
        this.mState = 4;
        this.mDragCallback = new ViewDragHelper.Callback() {
            @Override
            public int clampViewPositionHorizontal(final View view, final int n, final int n2) {
                return view.getLeft();
            }
            
            @Override
            public int clampViewPositionVertical(final View view, final int n, int n2) {
                final int mMinOffset = BottomSheetBehavior.this.mMinOffset;
                if (BottomSheetBehavior.this.mHideable) {
                    n2 = BottomSheetBehavior.this.mParentHeight;
                }
                else {
                    n2 = BottomSheetBehavior.this.mMaxOffset;
                }
                return MathUtils.clamp(n, mMinOffset, n2);
            }
            
            @Override
            public int getViewVerticalDragRange(final View view) {
                if (BottomSheetBehavior.this.mHideable) {
                    return BottomSheetBehavior.this.mParentHeight - BottomSheetBehavior.this.mMinOffset;
                }
                return BottomSheetBehavior.this.mMaxOffset - BottomSheetBehavior.this.mMinOffset;
            }
            
            @Override
            public void onViewDragStateChanged(final int n) {
                if (n == 1) {
                    BottomSheetBehavior.this.setStateInternal(1);
                }
            }
            
            @Override
            public void onViewPositionChanged(final View view, final int n, final int n2, final int n3, final int n4) {
                BottomSheetBehavior.this.dispatchOnSlide(n2);
            }
            
            @Override
            public void onViewReleased(final View view, final float n, final float n2) {
                int n3;
                int stateInternal;
                if (n2 < 0.0f) {
                    n3 = BottomSheetBehavior.this.mMinOffset;
                    stateInternal = 3;
                }
                else if (BottomSheetBehavior.this.mHideable && BottomSheetBehavior.this.shouldHide(view, n2)) {
                    n3 = BottomSheetBehavior.this.mParentHeight;
                    stateInternal = 5;
                }
                else if (n2 == 0.0f) {
                    final int top = view.getTop();
                    if (Math.abs(top - BottomSheetBehavior.this.mMinOffset) < Math.abs(top - BottomSheetBehavior.this.mMaxOffset)) {
                        n3 = BottomSheetBehavior.this.mMinOffset;
                        stateInternal = 3;
                    }
                    else {
                        n3 = BottomSheetBehavior.this.mMaxOffset;
                        stateInternal = 4;
                    }
                }
                else {
                    n3 = BottomSheetBehavior.this.mMaxOffset;
                    stateInternal = 4;
                }
                if (BottomSheetBehavior.this.mViewDragHelper.settleCapturedViewAt(view.getLeft(), n3)) {
                    BottomSheetBehavior.this.setStateInternal(2);
                    ViewCompat.postOnAnimation(view, new SettleRunnable(view, stateInternal));
                    return;
                }
                BottomSheetBehavior.this.setStateInternal(stateInternal);
            }
            
            @Override
            public boolean tryCaptureView(final View view, final int n) {
                if (BottomSheetBehavior.this.mState == 1) {
                    return false;
                }
                if (BottomSheetBehavior.this.mTouchingScrollingChild) {
                    return false;
                }
                if (BottomSheetBehavior.this.mState == 3 && BottomSheetBehavior.this.mActivePointerId == n) {
                    final View view2 = BottomSheetBehavior.this.mNestedScrollingChildRef.get();
                    if (view2 != null && view2.canScrollVertically(-1)) {
                        return false;
                    }
                }
                return BottomSheetBehavior.this.mViewRef != null && BottomSheetBehavior.this.mViewRef.get() == view;
            }
        };
    }
    
    public BottomSheetBehavior(final Context context, final AttributeSet set) {
        super(context, set);
        this.mState = 4;
        this.mDragCallback = new ViewDragHelper.Callback() {
            @Override
            public int clampViewPositionHorizontal(final View view, final int n, final int n2) {
                return view.getLeft();
            }
            
            @Override
            public int clampViewPositionVertical(final View view, final int n, int n2) {
                final int mMinOffset = BottomSheetBehavior.this.mMinOffset;
                if (BottomSheetBehavior.this.mHideable) {
                    n2 = BottomSheetBehavior.this.mParentHeight;
                }
                else {
                    n2 = BottomSheetBehavior.this.mMaxOffset;
                }
                return MathUtils.clamp(n, mMinOffset, n2);
            }
            
            @Override
            public int getViewVerticalDragRange(final View view) {
                if (BottomSheetBehavior.this.mHideable) {
                    return BottomSheetBehavior.this.mParentHeight - BottomSheetBehavior.this.mMinOffset;
                }
                return BottomSheetBehavior.this.mMaxOffset - BottomSheetBehavior.this.mMinOffset;
            }
            
            @Override
            public void onViewDragStateChanged(final int n) {
                if (n == 1) {
                    BottomSheetBehavior.this.setStateInternal(1);
                }
            }
            
            @Override
            public void onViewPositionChanged(final View view, final int n, final int n2, final int n3, final int n4) {
                BottomSheetBehavior.this.dispatchOnSlide(n2);
            }
            
            @Override
            public void onViewReleased(final View view, final float n, final float n2) {
                int n3;
                int stateInternal;
                if (n2 < 0.0f) {
                    n3 = BottomSheetBehavior.this.mMinOffset;
                    stateInternal = 3;
                }
                else if (BottomSheetBehavior.this.mHideable && BottomSheetBehavior.this.shouldHide(view, n2)) {
                    n3 = BottomSheetBehavior.this.mParentHeight;
                    stateInternal = 5;
                }
                else if (n2 == 0.0f) {
                    final int top = view.getTop();
                    if (Math.abs(top - BottomSheetBehavior.this.mMinOffset) < Math.abs(top - BottomSheetBehavior.this.mMaxOffset)) {
                        n3 = BottomSheetBehavior.this.mMinOffset;
                        stateInternal = 3;
                    }
                    else {
                        n3 = BottomSheetBehavior.this.mMaxOffset;
                        stateInternal = 4;
                    }
                }
                else {
                    n3 = BottomSheetBehavior.this.mMaxOffset;
                    stateInternal = 4;
                }
                if (BottomSheetBehavior.this.mViewDragHelper.settleCapturedViewAt(view.getLeft(), n3)) {
                    BottomSheetBehavior.this.setStateInternal(2);
                    ViewCompat.postOnAnimation(view, new SettleRunnable(view, stateInternal));
                    return;
                }
                BottomSheetBehavior.this.setStateInternal(stateInternal);
            }
            
            @Override
            public boolean tryCaptureView(final View view, final int n) {
                if (BottomSheetBehavior.this.mState == 1) {
                    return false;
                }
                if (BottomSheetBehavior.this.mTouchingScrollingChild) {
                    return false;
                }
                if (BottomSheetBehavior.this.mState == 3 && BottomSheetBehavior.this.mActivePointerId == n) {
                    final View view2 = BottomSheetBehavior.this.mNestedScrollingChildRef.get();
                    if (view2 != null && view2.canScrollVertically(-1)) {
                        return false;
                    }
                }
                return BottomSheetBehavior.this.mViewRef != null && BottomSheetBehavior.this.mViewRef.get() == view;
            }
        };
        final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.BottomSheetBehavior_Layout);
        final TypedValue peekValue = obtainStyledAttributes.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
        if (peekValue != null && peekValue.data == -1) {
            this.setPeekHeight(peekValue.data);
        }
        else {
            this.setPeekHeight(obtainStyledAttributes.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
        }
        this.setHideable(obtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        this.setSkipCollapsed(obtainStyledAttributes.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        obtainStyledAttributes.recycle();
        this.mMaximumVelocity = (float)ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
    }
    
    public static <V extends View> BottomSheetBehavior<V> from(final V v) {
        final ViewGroup$LayoutParams layoutParams = v.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
        }
        final Behavior behavior = ((LayoutParams)layoutParams).getBehavior();
        if (behavior instanceof BottomSheetBehavior) {
            return (BottomSheetBehavior<V>)behavior;
        }
        throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
    }
    
    private float getYVelocity() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        return this.mVelocityTracker.getYVelocity(this.mActivePointerId);
    }
    
    private void reset() {
        this.mActivePointerId = -1;
        final VelocityTracker mVelocityTracker = this.mVelocityTracker;
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }
    
    void dispatchOnSlide(final int n) {
        final View view = this.mViewRef.get();
        if (view != null) {
            final BottomSheetCallback mCallback = this.mCallback;
            if (mCallback != null) {
                final int mMaxOffset = this.mMaxOffset;
                if (n > mMaxOffset) {
                    mCallback.onSlide(view, (mMaxOffset - n) / (float)(this.mParentHeight - mMaxOffset));
                    return;
                }
                mCallback.onSlide(view, (mMaxOffset - n) / (float)(mMaxOffset - this.mMinOffset));
            }
        }
    }
    
    @VisibleForTesting
    View findScrollingChild(final View view) {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            return view;
        }
        if (view instanceof ViewGroup) {
            final ViewGroup viewGroup = (ViewGroup)view;
            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
                final View scrollingChild = this.findScrollingChild(viewGroup.getChildAt(i));
                if (scrollingChild != null) {
                    return scrollingChild;
                }
            }
        }
        return null;
    }
    
    public final int getPeekHeight() {
        if (this.mPeekHeightAuto) {
            return -1;
        }
        return this.mPeekHeight;
    }
    
    @VisibleForTesting
    int getPeekHeightMin() {
        return this.mPeekHeightMin;
    }
    
    public boolean getSkipCollapsed() {
        return this.mSkipCollapsed;
    }
    
    public final int getState() {
        return this.mState;
    }
    
    public boolean isHideable() {
        return this.mHideable;
    }
    
    @Override
    public boolean onInterceptTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        if (!v.isShown()) {
            this.mIgnoreEvents = true;
            return false;
        }
        final int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.reset();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        Label_0241: {
            if (actionMasked != 3) {
                switch (actionMasked) {
                    default: {
                        break Label_0241;
                    }
                    case 0: {
                        final int n = (int)motionEvent.getX();
                        this.mInitialY = (int)motionEvent.getY();
                        final WeakReference<View> mNestedScrollingChildRef = this.mNestedScrollingChildRef;
                        View view;
                        if (mNestedScrollingChildRef != null) {
                            view = mNestedScrollingChildRef.get();
                        }
                        else {
                            view = null;
                        }
                        if (view != null && coordinatorLayout.isPointInChildBounds(view, n, this.mInitialY)) {
                            this.mActivePointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
                            this.mTouchingScrollingChild = true;
                        }
                        this.mIgnoreEvents = (this.mActivePointerId == -1 && !coordinatorLayout.isPointInChildBounds(v, n, this.mInitialY));
                        break Label_0241;
                    }
                    case 1: {
                        break;
                    }
                }
            }
            this.mTouchingScrollingChild = false;
            this.mActivePointerId = -1;
            if (this.mIgnoreEvents) {
                return this.mIgnoreEvents = false;
            }
        }
        if (!this.mIgnoreEvents && this.mViewDragHelper.shouldInterceptTouchEvent(motionEvent)) {
            return true;
        }
        final View view2 = this.mNestedScrollingChildRef.get();
        if (actionMasked == 2 && view2 != null && !this.mIgnoreEvents && this.mState != 1) {
            if (!coordinatorLayout.isPointInChildBounds(view2, (int)motionEvent.getX(), (int)motionEvent.getY())) {
                if (Math.abs(this.mInitialY - motionEvent.getY()) > this.mViewDragHelper.getTouchSlop()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean onLayoutChild(final CoordinatorLayout coordinatorLayout, final V v, int n) {
        if (ViewCompat.getFitsSystemWindows((View)coordinatorLayout) && !ViewCompat.getFitsSystemWindows(v)) {
            ViewCompat.setFitsSystemWindows(v, true);
        }
        final int top = v.getTop();
        coordinatorLayout.onLayoutChild(v, n);
        this.mParentHeight = coordinatorLayout.getHeight();
        if (this.mPeekHeightAuto) {
            if (this.mPeekHeightMin == 0) {
                this.mPeekHeightMin = coordinatorLayout.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            }
            n = Math.max(this.mPeekHeightMin, this.mParentHeight - coordinatorLayout.getWidth() * 9 / 16);
        }
        else {
            n = this.mPeekHeight;
        }
        this.mMinOffset = Math.max(0, this.mParentHeight - v.getHeight());
        this.mMaxOffset = Math.max(this.mParentHeight - n, this.mMinOffset);
        n = this.mState;
        if (n == 3) {
            ViewCompat.offsetTopAndBottom(v, this.mMinOffset);
        }
        else if (this.mHideable && n == 5) {
            ViewCompat.offsetTopAndBottom(v, this.mParentHeight);
        }
        else {
            n = this.mState;
            if (n == 4) {
                ViewCompat.offsetTopAndBottom(v, this.mMaxOffset);
            }
            else if (n == 1 || n == 2) {
                ViewCompat.offsetTopAndBottom(v, top - v.getTop());
            }
        }
        if (this.mViewDragHelper == null) {
            this.mViewDragHelper = ViewDragHelper.create(coordinatorLayout, this.mDragCallback);
        }
        this.mViewRef = new WeakReference<V>(v);
        this.mNestedScrollingChildRef = new WeakReference<View>(this.findScrollingChild(v));
        return true;
    }
    
    @Override
    public boolean onNestedPreFling(final CoordinatorLayout coordinatorLayout, final V v, final View view, final float n, final float n2) {
        return view == this.mNestedScrollingChildRef.get() && (this.mState != 3 || super.onNestedPreFling(coordinatorLayout, v, view, n, n2));
    }
    
    @Override
    public void onNestedPreScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, int top, final int mLastNestedScrollDy, final int[] array) {
        if (view != this.mNestedScrollingChildRef.get()) {
            return;
        }
        top = v.getTop();
        final int n = top - mLastNestedScrollDy;
        if (mLastNestedScrollDy > 0) {
            final int mMinOffset = this.mMinOffset;
            if (n < mMinOffset) {
                array[1] = top - mMinOffset;
                ViewCompat.offsetTopAndBottom(v, -array[1]);
                this.setStateInternal(3);
            }
            else {
                ViewCompat.offsetTopAndBottom(v, -(array[1] = mLastNestedScrollDy));
                this.setStateInternal(1);
            }
        }
        else if (mLastNestedScrollDy < 0) {
            if (!view.canScrollVertically(-1)) {
                final int mMaxOffset = this.mMaxOffset;
                if (n > mMaxOffset && !this.mHideable) {
                    array[1] = top - mMaxOffset;
                    ViewCompat.offsetTopAndBottom(v, -array[1]);
                    this.setStateInternal(4);
                }
                else {
                    ViewCompat.offsetTopAndBottom(v, -(array[1] = mLastNestedScrollDy));
                    this.setStateInternal(1);
                }
            }
        }
        this.dispatchOnSlide(v.getTop());
        this.mLastNestedScrollDy = mLastNestedScrollDy;
        this.mNestedScrolled = true;
    }
    
    @Override
    public void onRestoreInstanceState(final CoordinatorLayout coordinatorLayout, final V v, final Parcelable parcelable) {
        final SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(coordinatorLayout, v, savedState.getSuperState());
        if (savedState.state != 1 && savedState.state != 2) {
            this.mState = savedState.state;
            return;
        }
        this.mState = 4;
    }
    
    @Override
    public Parcelable onSaveInstanceState(final CoordinatorLayout coordinatorLayout, final V v) {
        return (Parcelable)new SavedState(super.onSaveInstanceState(coordinatorLayout, v), this.mState);
    }
    
    @Override
    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view, final View view2, final int n) {
        boolean b = false;
        this.mLastNestedScrollDy = 0;
        this.mNestedScrolled = false;
        if ((n & 0x2) != 0x0) {
            b = true;
        }
        return b;
    }
    
    @Override
    public void onStopNestedScroll(final CoordinatorLayout coordinatorLayout, final V v, final View view) {
        if (v.getTop() == this.mMinOffset) {
            this.setStateInternal(3);
            return;
        }
        final WeakReference<View> mNestedScrollingChildRef = this.mNestedScrollingChildRef;
        if (mNestedScrollingChildRef == null || view != mNestedScrollingChildRef.get()) {
            return;
        }
        if (!this.mNestedScrolled) {
            return;
        }
        int n;
        int stateInternal;
        if (this.mLastNestedScrollDy > 0) {
            n = this.mMinOffset;
            stateInternal = 3;
        }
        else if (this.mHideable && this.shouldHide(v, this.getYVelocity())) {
            n = this.mParentHeight;
            stateInternal = 5;
        }
        else if (this.mLastNestedScrollDy == 0) {
            final int top = v.getTop();
            if (Math.abs(top - this.mMinOffset) < Math.abs(top - this.mMaxOffset)) {
                n = this.mMinOffset;
                stateInternal = 3;
            }
            else {
                n = this.mMaxOffset;
                stateInternal = 4;
            }
        }
        else {
            n = this.mMaxOffset;
            stateInternal = 4;
        }
        if (this.mViewDragHelper.smoothSlideViewTo(v, v.getLeft(), n)) {
            this.setStateInternal(2);
            ViewCompat.postOnAnimation(v, new SettleRunnable(v, stateInternal));
        }
        else {
            this.setStateInternal(stateInternal);
        }
        this.mNestedScrolled = false;
    }
    
    @Override
    public boolean onTouchEvent(final CoordinatorLayout coordinatorLayout, final V v, final MotionEvent motionEvent) {
        if (!v.isShown()) {
            return false;
        }
        final int actionMasked = motionEvent.getActionMasked();
        if (this.mState == 1 && actionMasked == 0) {
            return true;
        }
        this.mViewDragHelper.processTouchEvent(motionEvent);
        if (actionMasked == 0) {
            this.reset();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        if (actionMasked == 2 && !this.mIgnoreEvents) {
            if (Math.abs(this.mInitialY - motionEvent.getY()) > this.mViewDragHelper.getTouchSlop()) {
                this.mViewDragHelper.captureChildView(v, motionEvent.getPointerId(motionEvent.getActionIndex()));
            }
        }
        return this.mIgnoreEvents ^ true;
    }
    
    public void setBottomSheetCallback(final BottomSheetCallback mCallback) {
        this.mCallback = mCallback;
    }
    
    public void setHideable(final boolean mHideable) {
        this.mHideable = mHideable;
    }
    
    public final void setPeekHeight(int n) {
        final int n2 = 0;
        if (n == -1) {
            if (!this.mPeekHeightAuto) {
                this.mPeekHeightAuto = true;
                n = 1;
            }
            else {
                n = n2;
            }
        }
        else if (!this.mPeekHeightAuto && this.mPeekHeight == n) {
            n = n2;
        }
        else {
            this.mPeekHeightAuto = false;
            this.mPeekHeight = Math.max(0, n);
            this.mMaxOffset = this.mParentHeight - n;
            n = 1;
        }
        if (n != 0 && this.mState == 4) {
            final WeakReference<V> mViewRef = this.mViewRef;
            if (mViewRef != null) {
                final View view = mViewRef.get();
                if (view != null) {
                    view.requestLayout();
                }
            }
        }
    }
    
    public void setSkipCollapsed(final boolean mSkipCollapsed) {
        this.mSkipCollapsed = mSkipCollapsed;
    }
    
    public final void setState(final int mState) {
        if (mState == this.mState) {
            return;
        }
        final WeakReference<V> mViewRef = this.mViewRef;
        if (mViewRef == null) {
            if (mState != 4 && mState != 3 && (!this.mHideable || mState != 5)) {
                return;
            }
            this.mState = mState;
        }
        else {
            final View view = mViewRef.get();
            if (view == null) {
                return;
            }
            final ViewParent parent = view.getParent();
            if (parent != null && parent.isLayoutRequested() && ViewCompat.isAttachedToWindow(view)) {
                view.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        BottomSheetBehavior.this.startSettlingAnimation(view, mState);
                    }
                });
                return;
            }
            this.startSettlingAnimation(view, mState);
        }
    }
    
    void setStateInternal(final int mState) {
        if (this.mState == mState) {
            return;
        }
        this.mState = mState;
        final View view = this.mViewRef.get();
        if (view != null) {
            final BottomSheetCallback mCallback = this.mCallback;
            if (mCallback != null) {
                mCallback.onStateChanged(view, mState);
            }
        }
    }
    
    boolean shouldHide(final View view, final float n) {
        return this.mSkipCollapsed || (view.getTop() >= this.mMaxOffset && Math.abs(view.getTop() + 0.1f * n - this.mMaxOffset) / this.mPeekHeight > 0.5f);
    }
    
    void startSettlingAnimation(final View view, final int stateInternal) {
        int n;
        if (stateInternal == 4) {
            n = this.mMaxOffset;
        }
        else if (stateInternal == 3) {
            n = this.mMinOffset;
        }
        else {
            if (!this.mHideable || stateInternal != 5) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Illegal state argument: ");
                sb.append(stateInternal);
                throw new IllegalArgumentException(sb.toString());
            }
            n = this.mParentHeight;
        }
        if (this.mViewDragHelper.smoothSlideViewTo(view, view.getLeft(), n)) {
            this.setStateInternal(2);
            ViewCompat.postOnAnimation(view, new SettleRunnable(view, stateInternal));
            return;
        }
        this.setStateInternal(stateInternal);
    }
    
    public abstract static class BottomSheetCallback
    {
        public abstract void onSlide(@NonNull final View p0, final float p1);
        
        public abstract void onStateChanged(@NonNull final View p0, final int p1);
    }
    
    protected static class SavedState extends AbsSavedState
    {
        public static final Parcelable$Creator<SavedState> CREATOR;
        final int state;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$ClassLoaderCreator<SavedState>() {
                public SavedState createFromParcel(final Parcel parcel) {
                    return new SavedState(parcel, null);
                }
                
                public SavedState createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
                    return new SavedState(parcel, classLoader);
                }
                
                public SavedState[] newArray(final int n) {
                    return new SavedState[n];
                }
            };
        }
        
        public SavedState(final Parcel parcel) {
            this(parcel, null);
        }
        
        public SavedState(final Parcel parcel, final ClassLoader classLoader) {
            super(parcel, classLoader);
            this.state = parcel.readInt();
        }
        
        public SavedState(final Parcelable parcelable, final int state) {
            super(parcelable);
            this.state = state;
        }
        
        @Override
        public void writeToParcel(final Parcel parcel, final int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.state);
        }
    }
    
    private class SettleRunnable implements Runnable
    {
        private final int mTargetState;
        private final View mView;
        
        SettleRunnable(final View mView, final int mTargetState) {
            this.mView = mView;
            this.mTargetState = mTargetState;
        }
        
        @Override
        public void run() {
            if (BottomSheetBehavior.this.mViewDragHelper != null && BottomSheetBehavior.this.mViewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.mView, this);
                return;
            }
            BottomSheetBehavior.this.setStateInternal(this.mTargetState);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface State {
    }
}
