/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.graphics.Canvas
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.StateListDrawable
 *  android.view.MotionEvent
 *  android.view.View
 */
package android.support.v7.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@VisibleForTesting
class FastScroller
extends RecyclerView.ItemDecoration
implements RecyclerView.OnItemTouchListener {
    private static final int ANIMATION_STATE_FADING_IN = 1;
    private static final int ANIMATION_STATE_FADING_OUT = 3;
    private static final int ANIMATION_STATE_IN = 2;
    private static final int ANIMATION_STATE_OUT = 0;
    private static final int DRAG_NONE = 0;
    private static final int DRAG_X = 1;
    private static final int DRAG_Y = 2;
    private static final int[] EMPTY_STATE_SET;
    private static final int HIDE_DELAY_AFTER_DRAGGING_MS = 1200;
    private static final int HIDE_DELAY_AFTER_VISIBLE_MS = 1500;
    private static final int HIDE_DURATION_MS = 500;
    private static final int[] PRESSED_STATE_SET;
    private static final int SCROLLBAR_FULL_OPAQUE = 255;
    private static final int SHOW_DURATION_MS = 500;
    private static final int STATE_DRAGGING = 2;
    private static final int STATE_HIDDEN = 0;
    private static final int STATE_VISIBLE = 1;
    private int mAnimationState = 0;
    private int mDragState = 0;
    private final Runnable mHideRunnable;
    @VisibleForTesting
    float mHorizontalDragX;
    private final int[] mHorizontalRange = new int[2];
    @VisibleForTesting
    int mHorizontalThumbCenterX;
    private final StateListDrawable mHorizontalThumbDrawable;
    private final int mHorizontalThumbHeight;
    @VisibleForTesting
    int mHorizontalThumbWidth;
    private final Drawable mHorizontalTrackDrawable;
    private final int mHorizontalTrackHeight;
    private final int mMargin;
    private boolean mNeedHorizontalScrollbar = false;
    private boolean mNeedVerticalScrollbar = false;
    private final RecyclerView.OnScrollListener mOnScrollListener;
    private RecyclerView mRecyclerView;
    private int mRecyclerViewHeight = 0;
    private int mRecyclerViewWidth = 0;
    private final int mScrollbarMinimumRange;
    private final ValueAnimator mShowHideAnimator = ValueAnimator.ofFloat((float[])new float[]{0.0f, 1.0f});
    private int mState = 0;
    @VisibleForTesting
    float mVerticalDragY;
    private final int[] mVerticalRange = new int[2];
    @VisibleForTesting
    int mVerticalThumbCenterY;
    private final StateListDrawable mVerticalThumbDrawable;
    @VisibleForTesting
    int mVerticalThumbHeight;
    private final int mVerticalThumbWidth;
    private final Drawable mVerticalTrackDrawable;
    private final int mVerticalTrackWidth;

    static {
        PRESSED_STATE_SET = new int[]{16842919};
        EMPTY_STATE_SET = new int[0];
    }

    FastScroller(RecyclerView recyclerView, StateListDrawable stateListDrawable, Drawable drawable2, StateListDrawable stateListDrawable2, Drawable drawable3, int n, int n2, int n3) {
        this.mHideRunnable = new Runnable(){

            @Override
            public void run() {
                FastScroller.this.hide(500);
            }
        };
        this.mOnScrollListener = new RecyclerView.OnScrollListener(){

            @Override
            public void onScrolled(RecyclerView recyclerView, int n, int n2) {
                FastScroller.this.updateScrollPosition(recyclerView.computeHorizontalScrollOffset(), recyclerView.computeVerticalScrollOffset());
            }
        };
        this.mVerticalThumbDrawable = stateListDrawable;
        this.mVerticalTrackDrawable = drawable2;
        this.mHorizontalThumbDrawable = stateListDrawable2;
        this.mHorizontalTrackDrawable = drawable3;
        this.mVerticalThumbWidth = Math.max(n, stateListDrawable.getIntrinsicWidth());
        this.mVerticalTrackWidth = Math.max(n, drawable2.getIntrinsicWidth());
        this.mHorizontalThumbHeight = Math.max(n, stateListDrawable2.getIntrinsicWidth());
        this.mHorizontalTrackHeight = Math.max(n, drawable3.getIntrinsicWidth());
        this.mScrollbarMinimumRange = n2;
        this.mMargin = n3;
        this.mVerticalThumbDrawable.setAlpha(255);
        this.mVerticalTrackDrawable.setAlpha(255);
        this.mShowHideAnimator.addListener((Animator.AnimatorListener)new AnimatorListener());
        this.mShowHideAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener)new AnimatorUpdater());
        this.attachToRecyclerView(recyclerView);
    }

    private void cancelHide() {
        this.mRecyclerView.removeCallbacks(this.mHideRunnable);
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration(this);
        this.mRecyclerView.removeOnItemTouchListener(this);
        this.mRecyclerView.removeOnScrollListener(this.mOnScrollListener);
        this.cancelHide();
    }

    private void drawHorizontalScrollbar(Canvas canvas) {
        int n = this.mRecyclerViewHeight;
        int n2 = this.mHorizontalThumbHeight;
        int n3 = this.mHorizontalThumbCenterX;
        int n4 = this.mHorizontalThumbWidth;
        this.mHorizontalThumbDrawable.setBounds(0, 0, n4, n2);
        this.mHorizontalTrackDrawable.setBounds(0, 0, this.mRecyclerViewWidth, this.mHorizontalTrackHeight);
        canvas.translate(0.0f, (float)(n -= n2));
        this.mHorizontalTrackDrawable.draw(canvas);
        canvas.translate((float)(n3 -= n4 / 2), 0.0f);
        this.mHorizontalThumbDrawable.draw(canvas);
        canvas.translate((float)(- n3), (float)(- n));
    }

    private void drawVerticalScrollbar(Canvas canvas) {
        int n = this.mRecyclerViewWidth;
        int n2 = this.mVerticalThumbWidth;
        n -= n2;
        int n3 = this.mVerticalThumbCenterY;
        int n4 = this.mVerticalThumbHeight;
        n3 -= n4 / 2;
        this.mVerticalThumbDrawable.setBounds(0, 0, n2, n4);
        this.mVerticalTrackDrawable.setBounds(0, 0, this.mVerticalTrackWidth, this.mRecyclerViewHeight);
        if (this.isLayoutRTL()) {
            this.mVerticalTrackDrawable.draw(canvas);
            canvas.translate((float)this.mVerticalThumbWidth, (float)n3);
            canvas.scale(-1.0f, 1.0f);
            this.mVerticalThumbDrawable.draw(canvas);
            canvas.scale(1.0f, 1.0f);
            canvas.translate((float)(- this.mVerticalThumbWidth), (float)(- n3));
            return;
        }
        canvas.translate((float)n, 0.0f);
        this.mVerticalTrackDrawable.draw(canvas);
        canvas.translate(0.0f, (float)n3);
        this.mVerticalThumbDrawable.draw(canvas);
        canvas.translate((float)(- n), (float)(- n3));
    }

    private int[] getHorizontalRange() {
        int n;
        int[] arrn = this.mHorizontalRange;
        arrn[0] = n = this.mMargin;
        arrn[1] = this.mRecyclerViewWidth - n;
        return arrn;
    }

    private int[] getVerticalRange() {
        int n;
        int[] arrn = this.mVerticalRange;
        arrn[0] = n = this.mMargin;
        arrn[1] = this.mRecyclerViewHeight - n;
        return arrn;
    }

    private void horizontalScrollTo(float f) {
        int[] arrn = this.getHorizontalRange();
        f = Math.max((float)arrn[0], Math.min((float)arrn[1], f));
        if (Math.abs((float)this.mHorizontalThumbCenterX - f) < 2.0f) {
            return;
        }
        int n = this.scrollTo(this.mHorizontalDragX, f, arrn, this.mRecyclerView.computeHorizontalScrollRange(), this.mRecyclerView.computeHorizontalScrollOffset(), this.mRecyclerViewWidth);
        if (n != 0) {
            this.mRecyclerView.scrollBy(n, 0);
        }
        this.mHorizontalDragX = f;
    }

    private boolean isLayoutRTL() {
        if (ViewCompat.getLayoutDirection((View)this.mRecyclerView) == 1) {
            return true;
        }
        return false;
    }

    private void requestRedraw() {
        this.mRecyclerView.invalidate();
    }

    private void resetHideDelay(int n) {
        this.cancelHide();
        this.mRecyclerView.postDelayed(this.mHideRunnable, (long)n);
    }

    private int scrollTo(float f, float f2, int[] arrn, int n, int n2, int n3) {
        int n4 = arrn[1] - arrn[0];
        if (n4 == 0) {
            return 0;
        }
        n3 = (int)((float)n * (f = (f2 - f) / (float)n4));
        if ((n2 += n3) < (n -= n3) && n2 >= 0) {
            return n3;
        }
        return 0;
    }

    private void setState(int n) {
        if (n == 2 && this.mState != 2) {
            this.mVerticalThumbDrawable.setState(PRESSED_STATE_SET);
            this.cancelHide();
        }
        if (n == 0) {
            this.requestRedraw();
        } else {
            this.show();
        }
        if (this.mState == 2 && n != 2) {
            this.mVerticalThumbDrawable.setState(EMPTY_STATE_SET);
            this.resetHideDelay(1200);
        } else if (n == 1) {
            this.resetHideDelay(1500);
        }
        this.mState = n;
    }

    private void setupCallbacks() {
        this.mRecyclerView.addItemDecoration(this);
        this.mRecyclerView.addOnItemTouchListener(this);
        this.mRecyclerView.addOnScrollListener(this.mOnScrollListener);
    }

    private void verticalScrollTo(float f) {
        int[] arrn = this.getVerticalRange();
        f = Math.max((float)arrn[0], Math.min((float)arrn[1], f));
        if (Math.abs((float)this.mVerticalThumbCenterY - f) < 2.0f) {
            return;
        }
        int n = this.scrollTo(this.mVerticalDragY, f, arrn, this.mRecyclerView.computeVerticalScrollRange(), this.mRecyclerView.computeVerticalScrollOffset(), this.mRecyclerViewHeight);
        if (n != 0) {
            this.mRecyclerView.scrollBy(0, n);
        }
        this.mVerticalDragY = f;
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 == recyclerView) {
            return;
        }
        if (recyclerView2 != null) {
            this.destroyCallbacks();
        }
        this.mRecyclerView = recyclerView;
        if (this.mRecyclerView != null) {
            this.setupCallbacks();
            return;
        }
    }

    @VisibleForTesting
    Drawable getHorizontalThumbDrawable() {
        return this.mHorizontalThumbDrawable;
    }

    @VisibleForTesting
    Drawable getHorizontalTrackDrawable() {
        return this.mHorizontalTrackDrawable;
    }

    @VisibleForTesting
    Drawable getVerticalThumbDrawable() {
        return this.mVerticalThumbDrawable;
    }

    @VisibleForTesting
    Drawable getVerticalTrackDrawable() {
        return this.mVerticalTrackDrawable;
    }

    public void hide() {
        this.hide(0);
    }

    @VisibleForTesting
    void hide(int n) {
        switch (this.mAnimationState) {
            default: {
                return;
            }
            case 2: {
                break;
            }
            case 1: {
                this.mShowHideAnimator.cancel();
            }
        }
        this.mAnimationState = 3;
        ValueAnimator valueAnimator = this.mShowHideAnimator;
        valueAnimator.setFloatValues(new float[]{((Float)valueAnimator.getAnimatedValue()).floatValue(), 0.0f});
        this.mShowHideAnimator.setDuration((long)n);
        this.mShowHideAnimator.start();
    }

    public boolean isDragging() {
        if (this.mState == 2) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    boolean isHidden() {
        if (this.mState == 0) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    boolean isPointInsideHorizontalThumb(float f, float f2) {
        int n;
        int n2;
        if (f2 >= (float)(this.mRecyclerViewHeight - this.mHorizontalThumbHeight) && f >= (float)((n = this.mHorizontalThumbCenterX) - (n2 = this.mHorizontalThumbWidth) / 2) && f <= (float)(n + n2 / 2)) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    boolean isPointInsideVerticalThumb(float f, float f2) {
        int n;
        int n2;
        if ((this.isLayoutRTL() ? f <= (float)(this.mVerticalThumbWidth / 2) : f >= (float)(this.mRecyclerViewWidth - this.mVerticalThumbWidth)) && f2 >= (float)((n = this.mVerticalThumbCenterY) - (n2 = this.mVerticalThumbHeight) / 2) && f2 <= (float)(n + n2 / 2)) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    boolean isVisible() {
        if (this.mState == 1) {
            return true;
        }
        return false;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        if (this.mRecyclerViewWidth == this.mRecyclerView.getWidth() && this.mRecyclerViewHeight == this.mRecyclerView.getHeight()) {
            if (this.mAnimationState != 0) {
                if (this.mNeedVerticalScrollbar) {
                    this.drawVerticalScrollbar(canvas);
                }
                if (this.mNeedHorizontalScrollbar) {
                    this.drawHorizontalScrollbar(canvas);
                    return;
                }
                return;
            }
            return;
        }
        this.mRecyclerViewWidth = this.mRecyclerView.getWidth();
        this.mRecyclerViewHeight = this.mRecyclerView.getHeight();
        this.setState(0);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        int n = this.mState;
        if (n == 1) {
            boolean bl = this.isPointInsideVerticalThumb(motionEvent.getX(), motionEvent.getY());
            boolean bl2 = this.isPointInsideHorizontalThumb(motionEvent.getX(), motionEvent.getY());
            if (motionEvent.getAction() == 0 && (bl || bl2)) {
                if (bl2) {
                    this.mDragState = 1;
                    this.mHorizontalDragX = (int)motionEvent.getX();
                } else if (bl) {
                    this.mDragState = 2;
                    this.mVerticalDragY = (int)motionEvent.getY();
                }
                this.setState(2);
                bl = true;
            } else {
                bl = false;
            }
            return bl;
        }
        if (n == 2) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean bl) {
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (this.mState == 0) {
            return;
        }
        if (motionEvent.getAction() == 0) {
            boolean bl = this.isPointInsideVerticalThumb(motionEvent.getX(), motionEvent.getY());
            boolean bl2 = this.isPointInsideHorizontalThumb(motionEvent.getX(), motionEvent.getY());
            if (bl || bl2) {
                if (bl2) {
                    this.mDragState = 1;
                    this.mHorizontalDragX = (int)motionEvent.getX();
                } else if (bl) {
                    this.mDragState = 2;
                    this.mVerticalDragY = (int)motionEvent.getY();
                }
                this.setState(2);
            }
            return;
        }
        if (motionEvent.getAction() == 1 && this.mState == 2) {
            this.mVerticalDragY = 0.0f;
            this.mHorizontalDragX = 0.0f;
            this.setState(1);
            this.mDragState = 0;
            return;
        }
        if (motionEvent.getAction() == 2 && this.mState == 2) {
            this.show();
            if (this.mDragState == 1) {
                this.horizontalScrollTo(motionEvent.getX());
            }
            if (this.mDragState == 2) {
                this.verticalScrollTo(motionEvent.getY());
                return;
            }
            return;
        }
    }

    public void show() {
        int n = this.mAnimationState;
        if (n != 0) {
            if (n != 3) {
                return;
            }
            this.mShowHideAnimator.cancel();
        }
        this.mAnimationState = 1;
        ValueAnimator valueAnimator = this.mShowHideAnimator;
        valueAnimator.setFloatValues(new float[]{((Float)valueAnimator.getAnimatedValue()).floatValue(), 1.0f});
        this.mShowHideAnimator.setDuration(500L);
        this.mShowHideAnimator.setStartDelay(0L);
        this.mShowHideAnimator.start();
    }

    void updateScrollPosition(int n, int n2) {
        float f;
        int n3;
        float f2;
        int n4 = this.mRecyclerView.computeVerticalScrollRange();
        boolean bl = n4 - (n3 = this.mRecyclerViewHeight) > 0 && this.mRecyclerViewHeight >= this.mScrollbarMinimumRange;
        this.mNeedVerticalScrollbar = bl;
        int n5 = this.mRecyclerView.computeHorizontalScrollRange();
        int n6 = this.mRecyclerViewWidth;
        bl = n5 - n6 > 0 && this.mRecyclerViewWidth >= this.mScrollbarMinimumRange;
        this.mNeedHorizontalScrollbar = bl;
        if (!this.mNeedVerticalScrollbar && !this.mNeedHorizontalScrollbar) {
            if (this.mState != 0) {
                this.setState(0);
                return;
            }
            return;
        }
        if (this.mNeedVerticalScrollbar) {
            f2 = n2;
            f = (float)n3 / 2.0f;
            this.mVerticalThumbCenterY = (int)((float)n3 * (f2 + f) / (float)n4);
            this.mVerticalThumbHeight = Math.min(n3, n3 * n3 / n4);
        }
        if (this.mNeedHorizontalScrollbar) {
            f2 = n;
            f = (float)n6 / 2.0f;
            this.mHorizontalThumbCenterX = (int)((float)n6 * (f2 + f) / (float)n5);
            this.mHorizontalThumbWidth = Math.min(n6, n6 * n6 / n5);
        }
        if ((n = this.mState) != 0 && n != 1) {
            return;
        }
        this.setState(1);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    private static @interface AnimationState {
    }

    private class AnimatorListener
    extends AnimatorListenerAdapter {
        private boolean mCanceled;

        private AnimatorListener() {
            this.mCanceled = false;
        }

        public void onAnimationCancel(Animator animator2) {
            this.mCanceled = true;
        }

        public void onAnimationEnd(Animator animator2) {
            if (this.mCanceled) {
                this.mCanceled = false;
                return;
            }
            if (((Float)FastScroller.this.mShowHideAnimator.getAnimatedValue()).floatValue() == 0.0f) {
                FastScroller.this.mAnimationState = 0;
                FastScroller.this.setState(0);
                return;
            }
            FastScroller.this.mAnimationState = 2;
            FastScroller.this.requestRedraw();
        }
    }

    private class AnimatorUpdater
    implements ValueAnimator.AnimatorUpdateListener {
        private AnimatorUpdater() {
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            int n = (int)(((Float)valueAnimator.getAnimatedValue()).floatValue() * 255.0f);
            FastScroller.this.mVerticalThumbDrawable.setAlpha(n);
            FastScroller.this.mVerticalTrackDrawable.setAlpha(n);
            FastScroller.this.requestRedraw();
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    private static @interface DragState {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    private static @interface State {
    }

}

