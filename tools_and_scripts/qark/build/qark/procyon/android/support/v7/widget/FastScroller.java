// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.view.MotionEvent;
import android.support.annotation.Nullable;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.graphics.Canvas;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.Animator$AnimatorListener;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.VisibleForTesting;

@VisibleForTesting
class FastScroller extends ItemDecoration implements OnItemTouchListener
{
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
    private int mAnimationState;
    private int mDragState;
    private final Runnable mHideRunnable;
    @VisibleForTesting
    float mHorizontalDragX;
    private final int[] mHorizontalRange;
    @VisibleForTesting
    int mHorizontalThumbCenterX;
    private final StateListDrawable mHorizontalThumbDrawable;
    private final int mHorizontalThumbHeight;
    @VisibleForTesting
    int mHorizontalThumbWidth;
    private final Drawable mHorizontalTrackDrawable;
    private final int mHorizontalTrackHeight;
    private final int mMargin;
    private boolean mNeedHorizontalScrollbar;
    private boolean mNeedVerticalScrollbar;
    private final OnScrollListener mOnScrollListener;
    private RecyclerView mRecyclerView;
    private int mRecyclerViewHeight;
    private int mRecyclerViewWidth;
    private final int mScrollbarMinimumRange;
    private final ValueAnimator mShowHideAnimator;
    private int mState;
    @VisibleForTesting
    float mVerticalDragY;
    private final int[] mVerticalRange;
    @VisibleForTesting
    int mVerticalThumbCenterY;
    private final StateListDrawable mVerticalThumbDrawable;
    @VisibleForTesting
    int mVerticalThumbHeight;
    private final int mVerticalThumbWidth;
    private final Drawable mVerticalTrackDrawable;
    private final int mVerticalTrackWidth;
    
    static {
        PRESSED_STATE_SET = new int[] { 16842919 };
        EMPTY_STATE_SET = new int[0];
    }
    
    FastScroller(final RecyclerView recyclerView, final StateListDrawable mVerticalThumbDrawable, final Drawable mVerticalTrackDrawable, final StateListDrawable mHorizontalThumbDrawable, final Drawable mHorizontalTrackDrawable, final int n, final int mScrollbarMinimumRange, final int mMargin) {
        this.mRecyclerViewWidth = 0;
        this.mRecyclerViewHeight = 0;
        this.mNeedVerticalScrollbar = false;
        this.mNeedHorizontalScrollbar = false;
        this.mState = 0;
        this.mDragState = 0;
        this.mVerticalRange = new int[2];
        this.mHorizontalRange = new int[2];
        this.mShowHideAnimator = ValueAnimator.ofFloat(new float[] { 0.0f, 1.0f });
        this.mAnimationState = 0;
        this.mHideRunnable = new Runnable() {
            @Override
            public void run() {
                FastScroller.this.hide(500);
            }
        };
        this.mOnScrollListener = new OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, final int n, final int n2) {
                FastScroller.this.updateScrollPosition(recyclerView.computeHorizontalScrollOffset(), recyclerView.computeVerticalScrollOffset());
            }
        };
        this.mVerticalThumbDrawable = mVerticalThumbDrawable;
        this.mVerticalTrackDrawable = mVerticalTrackDrawable;
        this.mHorizontalThumbDrawable = mHorizontalThumbDrawable;
        this.mHorizontalTrackDrawable = mHorizontalTrackDrawable;
        this.mVerticalThumbWidth = Math.max(n, mVerticalThumbDrawable.getIntrinsicWidth());
        this.mVerticalTrackWidth = Math.max(n, mVerticalTrackDrawable.getIntrinsicWidth());
        this.mHorizontalThumbHeight = Math.max(n, mHorizontalThumbDrawable.getIntrinsicWidth());
        this.mHorizontalTrackHeight = Math.max(n, mHorizontalTrackDrawable.getIntrinsicWidth());
        this.mScrollbarMinimumRange = mScrollbarMinimumRange;
        this.mMargin = mMargin;
        this.mVerticalThumbDrawable.setAlpha(255);
        this.mVerticalTrackDrawable.setAlpha(255);
        this.mShowHideAnimator.addListener((Animator$AnimatorListener)new AnimatorListener());
        this.mShowHideAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)new AnimatorUpdater());
        this.attachToRecyclerView(recyclerView);
    }
    
    private void cancelHide() {
        this.mRecyclerView.removeCallbacks(this.mHideRunnable);
    }
    
    private void destroyCallbacks() {
        this.mRecyclerView.removeItemDecoration((RecyclerView.ItemDecoration)this);
        this.mRecyclerView.removeOnItemTouchListener((RecyclerView.OnItemTouchListener)this);
        this.mRecyclerView.removeOnScrollListener(this.mOnScrollListener);
        this.cancelHide();
    }
    
    private void drawHorizontalScrollbar(final Canvas canvas) {
        final int mRecyclerViewHeight = this.mRecyclerViewHeight;
        final int mHorizontalThumbHeight = this.mHorizontalThumbHeight;
        final int n = mRecyclerViewHeight - mHorizontalThumbHeight;
        final int mHorizontalThumbCenterX = this.mHorizontalThumbCenterX;
        final int mHorizontalThumbWidth = this.mHorizontalThumbWidth;
        final int n2 = mHorizontalThumbCenterX - mHorizontalThumbWidth / 2;
        this.mHorizontalThumbDrawable.setBounds(0, 0, mHorizontalThumbWidth, mHorizontalThumbHeight);
        this.mHorizontalTrackDrawable.setBounds(0, 0, this.mRecyclerViewWidth, this.mHorizontalTrackHeight);
        canvas.translate(0.0f, (float)n);
        this.mHorizontalTrackDrawable.draw(canvas);
        canvas.translate((float)n2, 0.0f);
        this.mHorizontalThumbDrawable.draw(canvas);
        canvas.translate((float)(-n2), (float)(-n));
    }
    
    private void drawVerticalScrollbar(final Canvas canvas) {
        final int mRecyclerViewWidth = this.mRecyclerViewWidth;
        final int mVerticalThumbWidth = this.mVerticalThumbWidth;
        final int n = mRecyclerViewWidth - mVerticalThumbWidth;
        final int mVerticalThumbCenterY = this.mVerticalThumbCenterY;
        final int mVerticalThumbHeight = this.mVerticalThumbHeight;
        final int n2 = mVerticalThumbCenterY - mVerticalThumbHeight / 2;
        this.mVerticalThumbDrawable.setBounds(0, 0, mVerticalThumbWidth, mVerticalThumbHeight);
        this.mVerticalTrackDrawable.setBounds(0, 0, this.mVerticalTrackWidth, this.mRecyclerViewHeight);
        if (this.isLayoutRTL()) {
            this.mVerticalTrackDrawable.draw(canvas);
            canvas.translate((float)this.mVerticalThumbWidth, (float)n2);
            canvas.scale(-1.0f, 1.0f);
            this.mVerticalThumbDrawable.draw(canvas);
            canvas.scale(1.0f, 1.0f);
            canvas.translate((float)(-this.mVerticalThumbWidth), (float)(-n2));
            return;
        }
        canvas.translate((float)n, 0.0f);
        this.mVerticalTrackDrawable.draw(canvas);
        canvas.translate(0.0f, (float)n2);
        this.mVerticalThumbDrawable.draw(canvas);
        canvas.translate((float)(-n), (float)(-n2));
    }
    
    private int[] getHorizontalRange() {
        final int[] mHorizontalRange = this.mHorizontalRange;
        final int mMargin = this.mMargin;
        mHorizontalRange[0] = mMargin;
        mHorizontalRange[1] = this.mRecyclerViewWidth - mMargin;
        return mHorizontalRange;
    }
    
    private int[] getVerticalRange() {
        final int[] mVerticalRange = this.mVerticalRange;
        final int mMargin = this.mMargin;
        mVerticalRange[0] = mMargin;
        mVerticalRange[1] = this.mRecyclerViewHeight - mMargin;
        return mVerticalRange;
    }
    
    private void horizontalScrollTo(float max) {
        final int[] horizontalRange = this.getHorizontalRange();
        max = Math.max((float)horizontalRange[0], Math.min((float)horizontalRange[1], max));
        if (Math.abs(this.mHorizontalThumbCenterX - max) < 2.0f) {
            return;
        }
        final int scrollTo = this.scrollTo(this.mHorizontalDragX, max, horizontalRange, this.mRecyclerView.computeHorizontalScrollRange(), this.mRecyclerView.computeHorizontalScrollOffset(), this.mRecyclerViewWidth);
        if (scrollTo != 0) {
            this.mRecyclerView.scrollBy(scrollTo, 0);
        }
        this.mHorizontalDragX = max;
    }
    
    private boolean isLayoutRTL() {
        return ViewCompat.getLayoutDirection((View)this.mRecyclerView) == 1;
    }
    
    private void requestRedraw() {
        this.mRecyclerView.invalidate();
    }
    
    private void resetHideDelay(final int n) {
        this.cancelHide();
        this.mRecyclerView.postDelayed(this.mHideRunnable, (long)n);
    }
    
    private int scrollTo(float n, final float n2, final int[] array, int n3, int n4, int n5) {
        final int n6 = array[1] - array[0];
        if (n6 == 0) {
            return 0;
        }
        n = (n2 - n) / n6;
        n3 -= n5;
        n5 = (int)(n3 * n);
        n4 += n5;
        if (n4 < n3 && n4 >= 0) {
            return n5;
        }
        return 0;
    }
    
    private void setState(final int mState) {
        if (mState == 2 && this.mState != 2) {
            this.mVerticalThumbDrawable.setState(FastScroller.PRESSED_STATE_SET);
            this.cancelHide();
        }
        if (mState == 0) {
            this.requestRedraw();
        }
        else {
            this.show();
        }
        if (this.mState == 2 && mState != 2) {
            this.mVerticalThumbDrawable.setState(FastScroller.EMPTY_STATE_SET);
            this.resetHideDelay(1200);
        }
        else if (mState == 1) {
            this.resetHideDelay(1500);
        }
        this.mState = mState;
    }
    
    private void setupCallbacks() {
        this.mRecyclerView.addItemDecoration((RecyclerView.ItemDecoration)this);
        this.mRecyclerView.addOnItemTouchListener((RecyclerView.OnItemTouchListener)this);
        this.mRecyclerView.addOnScrollListener(this.mOnScrollListener);
    }
    
    private void verticalScrollTo(float max) {
        final int[] verticalRange = this.getVerticalRange();
        max = Math.max((float)verticalRange[0], Math.min((float)verticalRange[1], max));
        if (Math.abs(this.mVerticalThumbCenterY - max) < 2.0f) {
            return;
        }
        final int scrollTo = this.scrollTo(this.mVerticalDragY, max, verticalRange, this.mRecyclerView.computeVerticalScrollRange(), this.mRecyclerView.computeVerticalScrollOffset(), this.mRecyclerViewHeight);
        if (scrollTo != 0) {
            this.mRecyclerView.scrollBy(0, scrollTo);
        }
        this.mVerticalDragY = max;
    }
    
    public void attachToRecyclerView(@Nullable final RecyclerView mRecyclerView) {
        final RecyclerView mRecyclerView2 = this.mRecyclerView;
        if (mRecyclerView2 == mRecyclerView) {
            return;
        }
        if (mRecyclerView2 != null) {
            this.destroyCallbacks();
        }
        this.mRecyclerView = mRecyclerView;
        if (this.mRecyclerView != null) {
            this.setupCallbacks();
        }
    }
    
    @VisibleForTesting
    Drawable getHorizontalThumbDrawable() {
        return (Drawable)this.mHorizontalThumbDrawable;
    }
    
    @VisibleForTesting
    Drawable getHorizontalTrackDrawable() {
        return this.mHorizontalTrackDrawable;
    }
    
    @VisibleForTesting
    Drawable getVerticalThumbDrawable() {
        return (Drawable)this.mVerticalThumbDrawable;
    }
    
    @VisibleForTesting
    Drawable getVerticalTrackDrawable() {
        return this.mVerticalTrackDrawable;
    }
    
    public void hide() {
        this.hide(0);
    }
    
    @VisibleForTesting
    void hide(final int n) {
        switch (this.mAnimationState) {
            default: {
                return;
            }
            case 2: {
                break;
            }
            case 1: {
                this.mShowHideAnimator.cancel();
                break;
            }
        }
        this.mAnimationState = 3;
        final ValueAnimator mShowHideAnimator = this.mShowHideAnimator;
        mShowHideAnimator.setFloatValues(new float[] { (float)mShowHideAnimator.getAnimatedValue(), 0.0f });
        this.mShowHideAnimator.setDuration((long)n);
        this.mShowHideAnimator.start();
    }
    
    public boolean isDragging() {
        return this.mState == 2;
    }
    
    @VisibleForTesting
    boolean isHidden() {
        return this.mState == 0;
    }
    
    @VisibleForTesting
    boolean isPointInsideHorizontalThumb(final float n, final float n2) {
        if (n2 >= this.mRecyclerViewHeight - this.mHorizontalThumbHeight) {
            final int mHorizontalThumbCenterX = this.mHorizontalThumbCenterX;
            final int mHorizontalThumbWidth = this.mHorizontalThumbWidth;
            if (n >= mHorizontalThumbCenterX - mHorizontalThumbWidth / 2 && n <= mHorizontalThumbCenterX + mHorizontalThumbWidth / 2) {
                return true;
            }
        }
        return false;
    }
    
    @VisibleForTesting
    boolean isPointInsideVerticalThumb(final float n, final float n2) {
        if (this.isLayoutRTL()) {
            if (n > this.mVerticalThumbWidth / 2) {
                return false;
            }
        }
        else if (n < this.mRecyclerViewWidth - this.mVerticalThumbWidth) {
            return false;
        }
        final int mVerticalThumbCenterY = this.mVerticalThumbCenterY;
        final int mVerticalThumbHeight = this.mVerticalThumbHeight;
        if (n2 >= mVerticalThumbCenterY - mVerticalThumbHeight / 2 && n2 <= mVerticalThumbCenterY + mVerticalThumbHeight / 2) {
            return true;
        }
        return false;
    }
    
    @VisibleForTesting
    boolean isVisible() {
        return this.mState == 1;
    }
    
    @Override
    public void onDrawOver(final Canvas canvas, final RecyclerView recyclerView, final RecyclerView.State state) {
        if (this.mRecyclerViewWidth != this.mRecyclerView.getWidth() || this.mRecyclerViewHeight != this.mRecyclerView.getHeight()) {
            this.mRecyclerViewWidth = this.mRecyclerView.getWidth();
            this.mRecyclerViewHeight = this.mRecyclerView.getHeight();
            this.setState(0);
            return;
        }
        if (this.mAnimationState == 0) {
            return;
        }
        if (this.mNeedVerticalScrollbar) {
            this.drawVerticalScrollbar(canvas);
        }
        if (this.mNeedHorizontalScrollbar) {
            this.drawHorizontalScrollbar(canvas);
        }
    }
    
    @Override
    public boolean onInterceptTouchEvent(final RecyclerView recyclerView, final MotionEvent motionEvent) {
        final int mState = this.mState;
        if (mState == 1) {
            final boolean pointInsideVerticalThumb = this.isPointInsideVerticalThumb(motionEvent.getX(), motionEvent.getY());
            final boolean pointInsideHorizontalThumb = this.isPointInsideHorizontalThumb(motionEvent.getX(), motionEvent.getY());
            boolean b;
            if (motionEvent.getAction() == 0 && (pointInsideVerticalThumb || pointInsideHorizontalThumb)) {
                if (pointInsideHorizontalThumb) {
                    this.mDragState = 1;
                    this.mHorizontalDragX = (float)(int)motionEvent.getX();
                }
                else if (pointInsideVerticalThumb) {
                    this.mDragState = 2;
                    this.mVerticalDragY = (float)(int)motionEvent.getY();
                }
                this.setState(2);
                b = true;
            }
            else {
                b = false;
            }
            return b;
        }
        return mState == 2;
    }
    
    @Override
    public void onRequestDisallowInterceptTouchEvent(final boolean b) {
    }
    
    @Override
    public void onTouchEvent(final RecyclerView recyclerView, final MotionEvent motionEvent) {
        if (this.mState == 0) {
            return;
        }
        if (motionEvent.getAction() == 0) {
            final boolean pointInsideVerticalThumb = this.isPointInsideVerticalThumb(motionEvent.getX(), motionEvent.getY());
            final boolean pointInsideHorizontalThumb = this.isPointInsideHorizontalThumb(motionEvent.getX(), motionEvent.getY());
            if (pointInsideVerticalThumb || pointInsideHorizontalThumb) {
                if (pointInsideHorizontalThumb) {
                    this.mDragState = 1;
                    this.mHorizontalDragX = (float)(int)motionEvent.getX();
                }
                else if (pointInsideVerticalThumb) {
                    this.mDragState = 2;
                    this.mVerticalDragY = (float)(int)motionEvent.getY();
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
        if (motionEvent.getAction() != 2 || this.mState != 2) {
            return;
        }
        this.show();
        if (this.mDragState == 1) {
            this.horizontalScrollTo(motionEvent.getX());
        }
        if (this.mDragState == 2) {
            this.verticalScrollTo(motionEvent.getY());
        }
    }
    
    public void show() {
        final int mAnimationState = this.mAnimationState;
        if (mAnimationState != 0) {
            if (mAnimationState != 3) {
                return;
            }
            this.mShowHideAnimator.cancel();
        }
        this.mAnimationState = 1;
        final ValueAnimator mShowHideAnimator = this.mShowHideAnimator;
        mShowHideAnimator.setFloatValues(new float[] { (float)mShowHideAnimator.getAnimatedValue(), 1.0f });
        this.mShowHideAnimator.setDuration(500L);
        this.mShowHideAnimator.setStartDelay(0L);
        this.mShowHideAnimator.start();
    }
    
    void updateScrollPosition(int mState, final int n) {
        final int computeVerticalScrollRange = this.mRecyclerView.computeVerticalScrollRange();
        final int mRecyclerViewHeight = this.mRecyclerViewHeight;
        this.mNeedVerticalScrollbar = (computeVerticalScrollRange - mRecyclerViewHeight > 0 && this.mRecyclerViewHeight >= this.mScrollbarMinimumRange);
        final int computeHorizontalScrollRange = this.mRecyclerView.computeHorizontalScrollRange();
        final int mRecyclerViewWidth = this.mRecyclerViewWidth;
        this.mNeedHorizontalScrollbar = (computeHorizontalScrollRange - mRecyclerViewWidth > 0 && this.mRecyclerViewWidth >= this.mScrollbarMinimumRange);
        if (!this.mNeedVerticalScrollbar && !this.mNeedHorizontalScrollbar) {
            if (this.mState != 0) {
                this.setState(0);
            }
        }
        else {
            if (this.mNeedVerticalScrollbar) {
                this.mVerticalThumbCenterY = (int)(mRecyclerViewHeight * (n + mRecyclerViewHeight / 2.0f) / computeVerticalScrollRange);
                this.mVerticalThumbHeight = Math.min(mRecyclerViewHeight, mRecyclerViewHeight * mRecyclerViewHeight / computeVerticalScrollRange);
            }
            if (this.mNeedHorizontalScrollbar) {
                this.mHorizontalThumbCenterX = (int)(mRecyclerViewWidth * (mState + mRecyclerViewWidth / 2.0f) / computeHorizontalScrollRange);
                this.mHorizontalThumbWidth = Math.min(mRecyclerViewWidth, mRecyclerViewWidth * mRecyclerViewWidth / computeHorizontalScrollRange);
            }
            mState = this.mState;
            if (mState != 0 && mState != 1) {
                return;
            }
            this.setState(1);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface AnimationState {
    }
    
    private class AnimatorListener extends AnimatorListenerAdapter
    {
        private boolean mCanceled;
        
        private AnimatorListener() {
            this.mCanceled = false;
        }
        
        public void onAnimationCancel(final Animator animator) {
            this.mCanceled = true;
        }
        
        public void onAnimationEnd(final Animator animator) {
            if (this.mCanceled) {
                this.mCanceled = false;
                return;
            }
            if ((float)FastScroller.this.mShowHideAnimator.getAnimatedValue() == 0.0f) {
                FastScroller.this.mAnimationState = 0;
                FastScroller.this.setState(0);
                return;
            }
            FastScroller.this.mAnimationState = 2;
            FastScroller.this.requestRedraw();
        }
    }
    
    private class AnimatorUpdater implements ValueAnimator$AnimatorUpdateListener
    {
        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
            final int n = (int)((float)valueAnimator.getAnimatedValue() * 255.0f);
            FastScroller.this.mVerticalThumbDrawable.setAlpha(n);
            FastScroller.this.mVerticalTrackDrawable.setAlpha(n);
            FastScroller.this.requestRedraw();
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface DragState {
    }
    
    @Retention(RetentionPolicy.SOURCE)
    private @interface State {
    }
}
