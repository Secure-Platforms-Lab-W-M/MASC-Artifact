/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.TypedArray
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.util.AttributeSet
 *  android.util.TypedValue
 *  android.view.MotionEvent
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 */
package android.support.design.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.annotation.VisibleForTesting;
import android.support.design.R;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.math.MathUtils;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;

public class BottomSheetBehavior<V extends View>
extends CoordinatorLayout.Behavior<V> {
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
    int mState = 4;
    boolean mTouchingScrollingChild;
    private VelocityTracker mVelocityTracker;
    ViewDragHelper mViewDragHelper;
    WeakReference<V> mViewRef;

    public BottomSheetBehavior() {
        this.mDragCallback = new ViewDragHelper.Callback(){

            @Override
            public int clampViewPositionHorizontal(View view, int n, int n2) {
                return view.getLeft();
            }

            @Override
            public int clampViewPositionVertical(View view, int n, int n2) {
                int n3 = BottomSheetBehavior.this.mMinOffset;
                n2 = BottomSheetBehavior.this.mHideable ? BottomSheetBehavior.this.mParentHeight : BottomSheetBehavior.this.mMaxOffset;
                return MathUtils.clamp(n, n3, n2);
            }

            @Override
            public int getViewVerticalDragRange(View view) {
                if (BottomSheetBehavior.this.mHideable) {
                    return BottomSheetBehavior.this.mParentHeight - BottomSheetBehavior.this.mMinOffset;
                }
                return BottomSheetBehavior.this.mMaxOffset - BottomSheetBehavior.this.mMinOffset;
            }

            @Override
            public void onViewDragStateChanged(int n) {
                if (n == 1) {
                    BottomSheetBehavior.this.setStateInternal(1);
                    return;
                }
            }

            @Override
            public void onViewPositionChanged(View view, int n, int n2, int n3, int n4) {
                BottomSheetBehavior.this.dispatchOnSlide(n2);
            }

            @Override
            public void onViewReleased(View view, float f, float f2) {
                int n;
                int n2;
                if (f2 < 0.0f) {
                    n2 = BottomSheetBehavior.this.mMinOffset;
                    n = 3;
                } else if (BottomSheetBehavior.this.mHideable && BottomSheetBehavior.this.shouldHide(view, f2)) {
                    n2 = BottomSheetBehavior.this.mParentHeight;
                    n = 5;
                } else if (f2 == 0.0f) {
                    n2 = view.getTop();
                    if (Math.abs(n2 - BottomSheetBehavior.this.mMinOffset) < Math.abs(n2 - BottomSheetBehavior.this.mMaxOffset)) {
                        n2 = BottomSheetBehavior.this.mMinOffset;
                        n = 3;
                    } else {
                        n2 = BottomSheetBehavior.this.mMaxOffset;
                        n = 4;
                    }
                } else {
                    n2 = BottomSheetBehavior.this.mMaxOffset;
                    n = 4;
                }
                if (BottomSheetBehavior.this.mViewDragHelper.settleCapturedViewAt(view.getLeft(), n2)) {
                    BottomSheetBehavior.this.setStateInternal(2);
                    ViewCompat.postOnAnimation(view, new SettleRunnable(view, n));
                    return;
                }
                BottomSheetBehavior.this.setStateInternal(n);
            }

            @Override
            public boolean tryCaptureView(View view, int n) {
                View view2;
                if (BottomSheetBehavior.this.mState == 1) {
                    return false;
                }
                if (BottomSheetBehavior.this.mTouchingScrollingChild) {
                    return false;
                }
                if (BottomSheetBehavior.this.mState == 3 && BottomSheetBehavior.this.mActivePointerId == n && (view2 = BottomSheetBehavior.this.mNestedScrollingChildRef.get()) != null && view2.canScrollVertically(-1)) {
                    return false;
                }
                if (BottomSheetBehavior.this.mViewRef != null && BottomSheetBehavior.this.mViewRef.get() == view) {
                    return true;
                }
                return false;
            }
        };
    }

    public BottomSheetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDragCallback = new ;
        attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.BottomSheetBehavior_Layout);
        TypedValue typedValue = attributeSet.peekValue(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight);
        if (typedValue != null && typedValue.data == -1) {
            this.setPeekHeight(typedValue.data);
        } else {
            this.setPeekHeight(attributeSet.getDimensionPixelSize(R.styleable.BottomSheetBehavior_Layout_behavior_peekHeight, -1));
        }
        this.setHideable(attributeSet.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_hideable, false));
        this.setSkipCollapsed(attributeSet.getBoolean(R.styleable.BottomSheetBehavior_Layout_behavior_skipCollapsed, false));
        attributeSet.recycle();
        this.mMaximumVelocity = ViewConfiguration.get((Context)context).getScaledMaximumFlingVelocity();
    }

    public static <V extends View> BottomSheetBehavior<V> from(V object) {
        if ((object = object.getLayoutParams()) instanceof CoordinatorLayout.LayoutParams) {
            if ((object = ((CoordinatorLayout.LayoutParams)((Object)object)).getBehavior()) instanceof BottomSheetBehavior) {
                return (BottomSheetBehavior)object;
            }
            throw new IllegalArgumentException("The view is not associated with BottomSheetBehavior");
        }
        throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
    }

    private float getYVelocity() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
        return this.mVelocityTracker.getYVelocity(this.mActivePointerId);
    }

    private void reset() {
        this.mActivePointerId = -1;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
            return;
        }
    }

    void dispatchOnSlide(int n) {
        BottomSheetCallback bottomSheetCallback;
        View view = (View)this.mViewRef.get();
        if (view != null && (bottomSheetCallback = this.mCallback) != null) {
            int n2 = this.mMaxOffset;
            if (n > n2) {
                bottomSheetCallback.onSlide(view, (float)(n2 - n) / (float)(this.mParentHeight - n2));
                return;
            }
            bottomSheetCallback.onSlide(view, (float)(n2 - n) / (float)(n2 - this.mMinOffset));
            return;
        }
    }

    @VisibleForTesting
    View findScrollingChild(View view) {
        if (ViewCompat.isNestedScrollingEnabled(view)) {
            return view;
        }
        if (view instanceof ViewGroup) {
            view = (ViewGroup)view;
            int n = view.getChildCount();
            for (int i = 0; i < n; ++i) {
                View view2 = this.findScrollingChild(view.getChildAt(i));
                if (view2 == null) continue;
                return view2;
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

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout var1_1, V var2_2, MotionEvent var3_3) {
        if (!var2_2.isShown()) {
            this.mIgnoreEvents = true;
            return false;
        }
        var4_4 = var3_3.getActionMasked();
        if (var4_4 == 0) {
            this.reset();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(var3_3);
        if (var4_4 != 3) {
            switch (var4_4) {
                default: {
                    ** break;
                }
                case 0: {
                    var5_5 = (int)var3_3.getX();
                    this.mInitialY = (int)var3_3.getY();
                    var7_6 = this.mNestedScrollingChildRef;
                    var7_6 = var7_6 != null ? var7_6.get() : null;
                    if (var7_6 != null && var1_1.isPointInChildBounds(var7_6, var5_5, this.mInitialY)) {
                        this.mActivePointerId = var3_3.getPointerId(var3_3.getActionIndex());
                        this.mTouchingScrollingChild = true;
                    }
                    var6_7 = this.mActivePointerId == -1 && var1_1.isPointInChildBounds((View)var2_2, var5_5, this.mInitialY) == false;
                    this.mIgnoreEvents = var6_7;
                    ** break;
                }
                case 1: 
            }
        }
        this.mTouchingScrollingChild = false;
        this.mActivePointerId = -1;
        if (this.mIgnoreEvents) {
            this.mIgnoreEvents = false;
            return false;
        }
lbl31: // 4 sources:
        if (!this.mIgnoreEvents && this.mViewDragHelper.shouldInterceptTouchEvent(var3_3)) {
            return true;
        }
        var2_2 = this.mNestedScrollingChildRef.get();
        if (var4_4 != 2) return false;
        if (var2_2 == null) return false;
        if (this.mIgnoreEvents != false) return false;
        if (this.mState == 1) return false;
        if (var1_1.isPointInChildBounds((View)var2_2, (int)var3_3.getX(), (int)var3_3.getY()) != false) return false;
        if (Math.abs((float)this.mInitialY - var3_3.getY()) <= (float)this.mViewDragHelper.getTouchSlop()) return false;
        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout coordinatorLayout, V v, int n) {
        if (ViewCompat.getFitsSystemWindows((View)coordinatorLayout) && !ViewCompat.getFitsSystemWindows(v)) {
            ViewCompat.setFitsSystemWindows(v, true);
        }
        int n2 = v.getTop();
        coordinatorLayout.onLayoutChild((View)v, n);
        this.mParentHeight = coordinatorLayout.getHeight();
        if (this.mPeekHeightAuto) {
            if (this.mPeekHeightMin == 0) {
                this.mPeekHeightMin = coordinatorLayout.getResources().getDimensionPixelSize(R.dimen.design_bottom_sheet_peek_height_min);
            }
            n = Math.max(this.mPeekHeightMin, this.mParentHeight - coordinatorLayout.getWidth() * 9 / 16);
        } else {
            n = this.mPeekHeight;
        }
        this.mMinOffset = Math.max(0, this.mParentHeight - v.getHeight());
        this.mMaxOffset = Math.max(this.mParentHeight - n, this.mMinOffset);
        n = this.mState;
        if (n == 3) {
            ViewCompat.offsetTopAndBottom(v, this.mMinOffset);
        } else if (this.mHideable && n == 5) {
            ViewCompat.offsetTopAndBottom(v, this.mParentHeight);
        } else {
            n = this.mState;
            if (n == 4) {
                ViewCompat.offsetTopAndBottom(v, this.mMaxOffset);
            } else if (n == 1 || n == 2) {
                ViewCompat.offsetTopAndBottom(v, n2 - v.getTop());
            }
        }
        if (this.mViewDragHelper == null) {
            this.mViewDragHelper = ViewDragHelper.create(coordinatorLayout, this.mDragCallback);
        }
        this.mViewRef = new WeakReference<V>(v);
        this.mNestedScrollingChildRef = new WeakReference<View>(this.findScrollingChild((View)v));
        return true;
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, V v, View view, float f, float f2) {
        if (view == this.mNestedScrollingChildRef.get() && (this.mState != 3 || super.onNestedPreFling(coordinatorLayout, v, view, f, f2))) {
            return true;
        }
        return false;
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, V v, View view, int n, int n2, int[] arrn) {
        if (view != this.mNestedScrollingChildRef.get()) {
            return;
        }
        n = v.getTop();
        int n3 = n - n2;
        if (n2 > 0) {
            int n4 = this.mMinOffset;
            if (n3 < n4) {
                arrn[1] = n - n4;
                ViewCompat.offsetTopAndBottom(v, - arrn[1]);
                this.setStateInternal(3);
            } else {
                arrn[1] = n2;
                ViewCompat.offsetTopAndBottom(v, - n2);
                this.setStateInternal(1);
            }
        } else if (n2 < 0 && !view.canScrollVertically(-1)) {
            int n5 = this.mMaxOffset;
            if (n3 > n5 && !this.mHideable) {
                arrn[1] = n - n5;
                ViewCompat.offsetTopAndBottom(v, - arrn[1]);
                this.setStateInternal(4);
            } else {
                arrn[1] = n2;
                ViewCompat.offsetTopAndBottom(v, - n2);
                this.setStateInternal(1);
            }
        }
        this.dispatchOnSlide(v.getTop());
        this.mLastNestedScrollDy = n2;
        this.mNestedScrolled = true;
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout coordinatorLayout, V v, Parcelable parcelable) {
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(coordinatorLayout, v, parcelable.getSuperState());
        if (parcelable.state != 1 && parcelable.state != 2) {
            this.mState = parcelable.state;
            return;
        }
        this.mState = 4;
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout coordinatorLayout, V v) {
        return new SavedState(super.onSaveInstanceState(coordinatorLayout, v), this.mState);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, V v, View view, View view2, int n) {
        boolean bl = false;
        this.mLastNestedScrollDy = 0;
        this.mNestedScrolled = false;
        if ((n & 2) != 0) {
            bl = true;
        }
        return bl;
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout weakReference, V v, View view) {
        if (v.getTop() == this.mMinOffset) {
            this.setStateInternal(3);
            return;
        }
        weakReference = this.mNestedScrollingChildRef;
        if (weakReference != null && view == weakReference.get()) {
            int n;
            int n2;
            if (!this.mNestedScrolled) {
                return;
            }
            if (this.mLastNestedScrollDy > 0) {
                n2 = this.mMinOffset;
                n = 3;
            } else if (this.mHideable && this.shouldHide((View)v, this.getYVelocity())) {
                n2 = this.mParentHeight;
                n = 5;
            } else if (this.mLastNestedScrollDy == 0) {
                n2 = v.getTop();
                if (Math.abs(n2 - this.mMinOffset) < Math.abs(n2 - this.mMaxOffset)) {
                    n2 = this.mMinOffset;
                    n = 3;
                } else {
                    n2 = this.mMaxOffset;
                    n = 4;
                }
            } else {
                n2 = this.mMaxOffset;
                n = 4;
            }
            if (this.mViewDragHelper.smoothSlideViewTo((View)v, v.getLeft(), n2)) {
                this.setStateInternal(2);
                ViewCompat.postOnAnimation(v, new SettleRunnable((View)v, n));
            } else {
                this.setStateInternal(n);
            }
            this.mNestedScrolled = false;
            return;
        }
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, V v, MotionEvent motionEvent) {
        if (!v.isShown()) {
            return false;
        }
        int n = motionEvent.getActionMasked();
        if (this.mState == 1 && n == 0) {
            return true;
        }
        this.mViewDragHelper.processTouchEvent(motionEvent);
        if (n == 0) {
            this.reset();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        if (n == 2 && !this.mIgnoreEvents && Math.abs((float)this.mInitialY - motionEvent.getY()) > (float)this.mViewDragHelper.getTouchSlop()) {
            this.mViewDragHelper.captureChildView((View)v, motionEvent.getPointerId(motionEvent.getActionIndex()));
        }
        return this.mIgnoreEvents ^ true;
    }

    public void setBottomSheetCallback(BottomSheetCallback bottomSheetCallback) {
        this.mCallback = bottomSheetCallback;
    }

    public void setHideable(boolean bl) {
        this.mHideable = bl;
    }

    public final void setPeekHeight(int n) {
        View view;
        int n2 = 0;
        if (n == -1) {
            if (!this.mPeekHeightAuto) {
                this.mPeekHeightAuto = true;
                n = 1;
            } else {
                n = n2;
            }
        } else if (!this.mPeekHeightAuto && this.mPeekHeight == n) {
            n = n2;
        } else {
            this.mPeekHeightAuto = false;
            this.mPeekHeight = Math.max(0, n);
            this.mMaxOffset = this.mParentHeight - n;
            n = 1;
        }
        if (n != 0 && this.mState == 4 && (view = this.mViewRef) != null) {
            if ((view = (View)view.get()) != null) {
                view.requestLayout();
                return;
            }
            return;
        }
    }

    public void setSkipCollapsed(boolean bl) {
        this.mSkipCollapsed = bl;
    }

    public final void setState(final int n) {
        if (n == this.mState) {
            return;
        }
        final View view = this.mViewRef;
        if (view == null) {
            if (!(n == 4 || n == 3 || this.mHideable && n == 5)) {
                return;
            }
            this.mState = n;
            return;
        }
        if ((view = (View)view.get()) == null) {
            return;
        }
        ViewParent viewParent = view.getParent();
        if (viewParent != null && viewParent.isLayoutRequested() && ViewCompat.isAttachedToWindow(view)) {
            view.post(new Runnable(){

                @Override
                public void run() {
                    BottomSheetBehavior.this.startSettlingAnimation(view, n);
                }
            });
            return;
        }
        this.startSettlingAnimation(view, n);
    }

    void setStateInternal(int n) {
        BottomSheetCallback bottomSheetCallback;
        if (this.mState == n) {
            return;
        }
        this.mState = n;
        View view = (View)this.mViewRef.get();
        if (view != null && (bottomSheetCallback = this.mCallback) != null) {
            bottomSheetCallback.onStateChanged(view, n);
            return;
        }
    }

    boolean shouldHide(View view, float f) {
        if (this.mSkipCollapsed) {
            return true;
        }
        if (view.getTop() < this.mMaxOffset) {
            return false;
        }
        if (Math.abs((float)view.getTop() + 0.1f * f - (float)this.mMaxOffset) / (float)this.mPeekHeight > 0.5f) {
            return true;
        }
        return false;
    }

    void startSettlingAnimation(View object, int n) {
        block7 : {
            int n2;
            block5 : {
                block6 : {
                    block4 : {
                        if (n != 4) break block4;
                        n2 = this.mMaxOffset;
                        break block5;
                    }
                    if (n != 3) break block6;
                    n2 = this.mMinOffset;
                    break block5;
                }
                if (!this.mHideable || n != 5) break block7;
                n2 = this.mParentHeight;
            }
            if (this.mViewDragHelper.smoothSlideViewTo((View)object, object.getLeft(), n2)) {
                this.setStateInternal(2);
                ViewCompat.postOnAnimation((View)object, new SettleRunnable((View)object, n));
                return;
            }
            this.setStateInternal(n);
            return;
        }
        object = new StringBuilder();
        object.append("Illegal state argument: ");
        object.append(n);
        throw new IllegalArgumentException(object.toString());
    }

    public static abstract class BottomSheetCallback {
        public abstract void onSlide(@NonNull View var1, float var2);

        public abstract void onStateChanged(@NonNull View var1, int var2);
    }

    protected static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        final int state;

        public SavedState(Parcel parcel) {
            this(parcel, null);
        }

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.state = parcel.readInt();
        }

        public SavedState(Parcelable parcelable, int n) {
            super(parcelable);
            this.state = n;
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.state);
        }

    }

    private class SettleRunnable
    implements Runnable {
        private final int mTargetState;
        private final View mView;

        SettleRunnable(View view, int n) {
            this.mView = view;
            this.mTargetState = n;
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

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public static @interface State {
    }

}

