/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 */
package android.support.design.widget;

import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class SwipeDismissBehavior<V extends View>
extends CoordinatorLayout.Behavior<V> {
    private static final float DEFAULT_ALPHA_END_DISTANCE = 0.5f;
    private static final float DEFAULT_ALPHA_START_DISTANCE = 0.0f;
    private static final float DEFAULT_DRAG_DISMISS_THRESHOLD = 0.5f;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    public static final int SWIPE_DIRECTION_ANY = 2;
    public static final int SWIPE_DIRECTION_END_TO_START = 1;
    public static final int SWIPE_DIRECTION_START_TO_END = 0;
    float mAlphaEndSwipeDistance = 0.5f;
    float mAlphaStartSwipeDistance = 0.0f;
    private final ViewDragHelper.Callback mDragCallback;
    float mDragDismissThreshold = 0.5f;
    private boolean mInterceptingEvents;
    OnDismissListener mListener;
    private float mSensitivity = 0.0f;
    private boolean mSensitivitySet;
    int mSwipeDirection = 2;
    ViewDragHelper mViewDragHelper;

    public SwipeDismissBehavior() {
        this.mDragCallback = new ViewDragHelper.Callback(){
            private static final int INVALID_POINTER_ID = -1;
            private int mActivePointerId;
            private int mOriginalCapturedViewLeft;
            {
                this.mActivePointerId = -1;
            }

            private boolean shouldDismiss(View view, float f) {
                boolean bl;
                boolean bl2;
                block7 : {
                    block8 : {
                        block10 : {
                            block11 : {
                                boolean bl3;
                                block9 : {
                                    bl3 = false;
                                    bl2 = false;
                                    bl = false;
                                    if (f == 0.0f) break block7;
                                    boolean bl4 = ViewCompat.getLayoutDirection(view) == 1;
                                    if (SwipeDismissBehavior.this.mSwipeDirection == 2) {
                                        return true;
                                    }
                                    if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
                                        if (bl4 ? f < 0.0f : f > 0.0f) {
                                            bl = true;
                                        }
                                        return bl;
                                    }
                                    if (SwipeDismissBehavior.this.mSwipeDirection != 1) break block8;
                                    if (!bl4) break block9;
                                    bl = bl3;
                                    if (f <= 0.0f) break block10;
                                    break block11;
                                }
                                bl = bl3;
                                if (f >= 0.0f) break block10;
                            }
                            bl = true;
                        }
                        return bl;
                    }
                    return false;
                }
                int n = view.getLeft();
                int n2 = this.mOriginalCapturedViewLeft;
                int n3 = Math.round((float)view.getWidth() * SwipeDismissBehavior.this.mDragDismissThreshold);
                bl = bl2;
                if (Math.abs(n - n2) >= n3) {
                    bl = true;
                }
                return bl;
            }

            @Override
            public int clampViewPositionHorizontal(View view, int n, int n2) {
                int n3;
                n2 = ViewCompat.getLayoutDirection(view) == 1 ? 1 : 0;
                if (SwipeDismissBehavior.this.mSwipeDirection == 0) {
                    if (n2 != 0) {
                        n2 = this.mOriginalCapturedViewLeft - view.getWidth();
                        n3 = this.mOriginalCapturedViewLeft;
                    } else {
                        n2 = this.mOriginalCapturedViewLeft;
                        n3 = this.mOriginalCapturedViewLeft + view.getWidth();
                    }
                } else if (SwipeDismissBehavior.this.mSwipeDirection == 1) {
                    if (n2 != 0) {
                        n2 = this.mOriginalCapturedViewLeft;
                        n3 = this.mOriginalCapturedViewLeft + view.getWidth();
                    } else {
                        n2 = this.mOriginalCapturedViewLeft - view.getWidth();
                        n3 = this.mOriginalCapturedViewLeft;
                    }
                } else {
                    n2 = this.mOriginalCapturedViewLeft - view.getWidth();
                    n3 = this.mOriginalCapturedViewLeft + view.getWidth();
                }
                return SwipeDismissBehavior.clamp(n2, n, n3);
            }

            @Override
            public int clampViewPositionVertical(View view, int n, int n2) {
                return view.getTop();
            }

            @Override
            public int getViewHorizontalDragRange(View view) {
                return view.getWidth();
            }

            @Override
            public void onViewCaptured(View view, int n) {
                this.mActivePointerId = n;
                this.mOriginalCapturedViewLeft = view.getLeft();
                if ((view = view.getParent()) != null) {
                    view.requestDisallowInterceptTouchEvent(true);
                    return;
                }
            }

            @Override
            public void onViewDragStateChanged(int n) {
                if (SwipeDismissBehavior.this.mListener != null) {
                    SwipeDismissBehavior.this.mListener.onDragStateChanged(n);
                    return;
                }
            }

            @Override
            public void onViewPositionChanged(View view, int n, int n2, int n3, int n4) {
                float f = (float)this.mOriginalCapturedViewLeft + (float)view.getWidth() * SwipeDismissBehavior.this.mAlphaStartSwipeDistance;
                float f2 = (float)this.mOriginalCapturedViewLeft + (float)view.getWidth() * SwipeDismissBehavior.this.mAlphaEndSwipeDistance;
                if ((float)n <= f) {
                    view.setAlpha(1.0f);
                    return;
                }
                if ((float)n >= f2) {
                    view.setAlpha(0.0f);
                    return;
                }
                view.setAlpha(SwipeDismissBehavior.clamp(0.0f, 1.0f - SwipeDismissBehavior.fraction(f, f2, n), 1.0f));
            }

            @Override
            public void onViewReleased(View view, float f, float f2) {
                this.mActivePointerId = -1;
                int n = view.getWidth();
                boolean bl = false;
                if (this.shouldDismiss(view, f)) {
                    int n2;
                    int n3 = view.getLeft();
                    n = n3 < (n2 = this.mOriginalCapturedViewLeft) ? n2 - n : n2 + n;
                    bl = true;
                } else {
                    n = this.mOriginalCapturedViewLeft;
                }
                if (SwipeDismissBehavior.this.mViewDragHelper.settleCapturedViewAt(n, view.getTop())) {
                    ViewCompat.postOnAnimation(view, new SettleRunnable(view, bl));
                    return;
                }
                if (bl && SwipeDismissBehavior.this.mListener != null) {
                    SwipeDismissBehavior.this.mListener.onDismiss(view);
                    return;
                }
            }

            @Override
            public boolean tryCaptureView(View view, int n) {
                if (this.mActivePointerId == -1 && SwipeDismissBehavior.this.canSwipeDismissView(view)) {
                    return true;
                }
                return false;
            }
        };
    }

    static float clamp(float f, float f2, float f3) {
        return Math.min(Math.max(f, f2), f3);
    }

    static int clamp(int n, int n2, int n3) {
        return Math.min(Math.max(n, n2), n3);
    }

    private void ensureViewDragHelper(ViewGroup object) {
        if (this.mViewDragHelper == null) {
            object = this.mSensitivitySet ? ViewDragHelper.create((ViewGroup)object, this.mSensitivity, this.mDragCallback) : ViewDragHelper.create((ViewGroup)object, this.mDragCallback);
            this.mViewDragHelper = object;
            return;
        }
    }

    static float fraction(float f, float f2, float f3) {
        return (f3 - f) / (f2 - f);
    }

    public boolean canSwipeDismissView(@NonNull View view) {
        return true;
    }

    public int getDragState() {
        ViewDragHelper viewDragHelper = this.mViewDragHelper;
        if (viewDragHelper != null) {
            return viewDragHelper.getViewDragState();
        }
        return 0;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout var1_1, V var2_2, MotionEvent var3_3) {
        var5_4 = this.mInterceptingEvents;
        var4_5 = var3_3.getActionMasked();
        if (var4_5 != 3) {
            switch (var4_5) {
                default: {
                    ** break;
                }
                case 0: {
                    var5_4 = this.mInterceptingEvents = var1_1.isPointInChildBounds((View)var2_2, (int)var3_3.getX(), (int)var3_3.getY());
                    ** break;
                }
                case 1: 
            }
        }
        this.mInterceptingEvents = false;
lbl12: // 3 sources:
        if (var5_4 == false) return false;
        this.ensureViewDragHelper(var1_1);
        return this.mViewDragHelper.shouldInterceptTouchEvent(var3_3);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout object, V v, MotionEvent motionEvent) {
        object = this.mViewDragHelper;
        if (object != null) {
            object.processTouchEvent(motionEvent);
            return true;
        }
        return false;
    }

    public void setDragDismissDistance(float f) {
        this.mDragDismissThreshold = SwipeDismissBehavior.clamp(0.0f, f, 1.0f);
    }

    public void setEndAlphaSwipeDistance(float f) {
        this.mAlphaEndSwipeDistance = SwipeDismissBehavior.clamp(0.0f, f, 1.0f);
    }

    public void setListener(OnDismissListener onDismissListener) {
        this.mListener = onDismissListener;
    }

    public void setSensitivity(float f) {
        this.mSensitivity = f;
        this.mSensitivitySet = true;
    }

    public void setStartAlphaSwipeDistance(float f) {
        this.mAlphaStartSwipeDistance = SwipeDismissBehavior.clamp(0.0f, f, 1.0f);
    }

    public void setSwipeDirection(int n) {
        this.mSwipeDirection = n;
    }

    public static interface OnDismissListener {
        public void onDismiss(View var1);

        public void onDragStateChanged(int var1);
    }

    private class SettleRunnable
    implements Runnable {
        private final boolean mDismiss;
        private final View mView;

        SettleRunnable(View view, boolean bl) {
            this.mView = view;
            this.mDismiss = bl;
        }

        @Override
        public void run() {
            if (SwipeDismissBehavior.this.mViewDragHelper != null && SwipeDismissBehavior.this.mViewDragHelper.continueSettling(true)) {
                ViewCompat.postOnAnimation(this.mView, this);
                return;
            }
            if (this.mDismiss && SwipeDismissBehavior.this.mListener != null) {
                SwipeDismissBehavior.this.mListener.onDismiss(this.mView);
                return;
            }
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    private static @interface SwipeDirection {
    }

}

