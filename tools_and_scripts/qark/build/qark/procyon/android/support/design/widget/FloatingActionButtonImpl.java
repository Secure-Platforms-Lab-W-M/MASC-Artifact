// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.design.widget;

import android.graphics.drawable.LayerDrawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.graphics.PorterDuff$Mode;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.annotation.Nullable;
import android.graphics.drawable.GradientDrawable;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.design.R;
import android.graphics.Paint;
import android.os.Build$VERSION;
import android.view.View;
import android.support.v4.view.ViewCompat;
import android.content.res.ColorStateList;
import android.animation.ValueAnimator$AnimatorUpdateListener;
import android.animation.Animator$AnimatorListener;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.graphics.Rect;
import android.view.ViewTreeObserver$OnPreDrawListener;
import android.graphics.drawable.Drawable;
import android.view.animation.Interpolator;
import android.support.annotation.RequiresApi;

@RequiresApi(14)
class FloatingActionButtonImpl
{
    static final Interpolator ANIM_INTERPOLATOR;
    static final int ANIM_STATE_HIDING = 1;
    static final int ANIM_STATE_NONE = 0;
    static final int ANIM_STATE_SHOWING = 2;
    static final int[] EMPTY_STATE_SET;
    static final int[] ENABLED_STATE_SET;
    static final int[] FOCUSED_ENABLED_STATE_SET;
    static final long PRESSED_ANIM_DELAY = 100L;
    static final long PRESSED_ANIM_DURATION = 100L;
    static final int[] PRESSED_ENABLED_STATE_SET;
    static final int SHOW_HIDE_ANIM_DURATION = 200;
    int mAnimState;
    CircularBorderDrawable mBorderDrawable;
    Drawable mContentBackground;
    float mElevation;
    private ViewTreeObserver$OnPreDrawListener mPreDrawListener;
    float mPressedTranslationZ;
    Drawable mRippleDrawable;
    private float mRotation;
    ShadowDrawableWrapper mShadowDrawable;
    final ShadowViewDelegate mShadowViewDelegate;
    Drawable mShapeDrawable;
    private final StateListAnimator mStateListAnimator;
    private final Rect mTmpRect;
    final VisibilityAwareImageButton mView;
    
    static {
        ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
        PRESSED_ENABLED_STATE_SET = new int[] { 16842919, 16842910 };
        FOCUSED_ENABLED_STATE_SET = new int[] { 16842908, 16842910 };
        ENABLED_STATE_SET = new int[] { 16842910 };
        EMPTY_STATE_SET = new int[0];
    }
    
    FloatingActionButtonImpl(final VisibilityAwareImageButton mView, final ShadowViewDelegate mShadowViewDelegate) {
        this.mAnimState = 0;
        this.mTmpRect = new Rect();
        this.mView = mView;
        this.mShadowViewDelegate = mShadowViewDelegate;
        (this.mStateListAnimator = new StateListAnimator()).addState(FloatingActionButtonImpl.PRESSED_ENABLED_STATE_SET, this.createAnimator((ShadowAnimatorImpl)new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(FloatingActionButtonImpl.FOCUSED_ENABLED_STATE_SET, this.createAnimator((ShadowAnimatorImpl)new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(FloatingActionButtonImpl.ENABLED_STATE_SET, this.createAnimator((ShadowAnimatorImpl)new ResetElevationAnimation()));
        this.mStateListAnimator.addState(FloatingActionButtonImpl.EMPTY_STATE_SET, this.createAnimator((ShadowAnimatorImpl)new DisabledElevationAnimation()));
        this.mRotation = this.mView.getRotation();
    }
    
    private ValueAnimator createAnimator(@NonNull final ShadowAnimatorImpl shadowAnimatorImpl) {
        final ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator((TimeInterpolator)FloatingActionButtonImpl.ANIM_INTERPOLATOR);
        valueAnimator.setDuration(100L);
        valueAnimator.addListener((Animator$AnimatorListener)shadowAnimatorImpl);
        valueAnimator.addUpdateListener((ValueAnimator$AnimatorUpdateListener)shadowAnimatorImpl);
        valueAnimator.setFloatValues(new float[] { 0.0f, 1.0f });
        return valueAnimator;
    }
    
    private static ColorStateList createColorStateList(int n) {
        final int[][] array = new int[3][];
        final int[] array2 = new int[3];
        array[0] = FloatingActionButtonImpl.FOCUSED_ENABLED_STATE_SET;
        array2[0] = n;
        final int n2 = 0 + 1;
        array[n2] = FloatingActionButtonImpl.PRESSED_ENABLED_STATE_SET;
        array2[n2] = n;
        n = n2 + 1;
        array[n] = new int[0];
        array2[n] = 0;
        return new ColorStateList(array, array2);
    }
    
    private void ensurePreDrawListener() {
        if (this.mPreDrawListener == null) {
            this.mPreDrawListener = (ViewTreeObserver$OnPreDrawListener)new ViewTreeObserver$OnPreDrawListener() {
                public boolean onPreDraw() {
                    FloatingActionButtonImpl.this.onPreDraw();
                    return true;
                }
            };
        }
    }
    
    private boolean shouldAnimateVisibilityChange() {
        return ViewCompat.isLaidOut((View)this.mView) && !this.mView.isInEditMode();
    }
    
    private void updateFromViewRotation() {
        if (Build$VERSION.SDK_INT == 19) {
            if (this.mRotation % 90.0f != 0.0f) {
                if (this.mView.getLayerType() != 1) {
                    this.mView.setLayerType(1, (Paint)null);
                }
            }
            else if (this.mView.getLayerType() != 0) {
                this.mView.setLayerType(0, (Paint)null);
            }
        }
        final ShadowDrawableWrapper mShadowDrawable = this.mShadowDrawable;
        if (mShadowDrawable != null) {
            mShadowDrawable.setRotation(-this.mRotation);
        }
        final CircularBorderDrawable mBorderDrawable = this.mBorderDrawable;
        if (mBorderDrawable != null) {
            mBorderDrawable.setRotation(-this.mRotation);
        }
    }
    
    CircularBorderDrawable createBorderDrawable(final int n, final ColorStateList borderTint) {
        final Context context = this.mView.getContext();
        final CircularBorderDrawable circularDrawable = this.newCircularDrawable();
        circularDrawable.setGradientColors(ContextCompat.getColor(context, R.color.design_fab_stroke_top_outer_color), ContextCompat.getColor(context, R.color.design_fab_stroke_top_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_outer_color));
        circularDrawable.setBorderWidth((float)n);
        circularDrawable.setBorderTint(borderTint);
        return circularDrawable;
    }
    
    GradientDrawable createShapeDrawable() {
        final GradientDrawable gradientDrawableForShape = this.newGradientDrawableForShape();
        gradientDrawableForShape.setShape(1);
        gradientDrawableForShape.setColor(-1);
        return gradientDrawableForShape;
    }
    
    final Drawable getContentBackground() {
        return this.mContentBackground;
    }
    
    float getElevation() {
        return this.mElevation;
    }
    
    void getPadding(final Rect rect) {
        this.mShadowDrawable.getPadding(rect);
    }
    
    void hide(@Nullable final InternalVisibilityChangedListener internalVisibilityChangedListener, final boolean b) {
        if (this.isOrWillBeHidden()) {
            return;
        }
        this.mView.animate().cancel();
        if (this.shouldAnimateVisibilityChange()) {
            this.mAnimState = 1;
            this.mView.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(200L).setInterpolator((TimeInterpolator)AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                private boolean mCancelled;
                
                public void onAnimationCancel(final Animator animator) {
                    this.mCancelled = true;
                }
                
                public void onAnimationEnd(final Animator animator) {
                    final FloatingActionButtonImpl this$0 = FloatingActionButtonImpl.this;
                    this$0.mAnimState = 0;
                    if (this.mCancelled) {
                        return;
                    }
                    final VisibilityAwareImageButton mView = this$0.mView;
                    int n;
                    if (b) {
                        n = 8;
                    }
                    else {
                        n = 4;
                    }
                    mView.internalSetVisibility(n, b);
                    final InternalVisibilityChangedListener val$listener = internalVisibilityChangedListener;
                    if (val$listener != null) {
                        val$listener.onHidden();
                    }
                }
                
                public void onAnimationStart(final Animator animator) {
                    FloatingActionButtonImpl.this.mView.internalSetVisibility(0, b);
                    this.mCancelled = false;
                }
            });
            return;
        }
        final VisibilityAwareImageButton mView = this.mView;
        int n;
        if (b) {
            n = 8;
        }
        else {
            n = 4;
        }
        mView.internalSetVisibility(n, b);
        if (internalVisibilityChangedListener != null) {
            internalVisibilityChangedListener.onHidden();
        }
    }
    
    boolean isOrWillBeHidden() {
        final int visibility = this.mView.getVisibility();
        final boolean b = false;
        boolean b2 = false;
        if (visibility == 0) {
            if (this.mAnimState == 1) {
                b2 = true;
            }
            return b2;
        }
        boolean b3 = b;
        if (this.mAnimState != 2) {
            b3 = true;
        }
        return b3;
    }
    
    boolean isOrWillBeShown() {
        final int visibility = this.mView.getVisibility();
        final boolean b = false;
        boolean b2 = false;
        if (visibility != 0) {
            if (this.mAnimState == 2) {
                b2 = true;
            }
            return b2;
        }
        boolean b3 = b;
        if (this.mAnimState != 1) {
            b3 = true;
        }
        return b3;
    }
    
    void jumpDrawableToCurrentState() {
        this.mStateListAnimator.jumpToCurrentState();
    }
    
    CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawable();
    }
    
    GradientDrawable newGradientDrawableForShape() {
        return new GradientDrawable();
    }
    
    void onAttachedToWindow() {
        if (this.requirePreDrawListener()) {
            this.ensurePreDrawListener();
            this.mView.getViewTreeObserver().addOnPreDrawListener(this.mPreDrawListener);
        }
    }
    
    void onCompatShadowChanged() {
    }
    
    void onDetachedFromWindow() {
        if (this.mPreDrawListener != null) {
            this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mPreDrawListener);
            this.mPreDrawListener = null;
        }
    }
    
    void onDrawableStateChanged(final int[] state) {
        this.mStateListAnimator.setState(state);
    }
    
    void onElevationsChanged(final float n, final float n2) {
        final ShadowDrawableWrapper mShadowDrawable = this.mShadowDrawable;
        if (mShadowDrawable != null) {
            mShadowDrawable.setShadowSize(n, this.mPressedTranslationZ + n);
            this.updatePadding();
        }
    }
    
    void onPaddingUpdated(final Rect rect) {
    }
    
    void onPreDraw() {
        final float rotation = this.mView.getRotation();
        if (this.mRotation != rotation) {
            this.mRotation = rotation;
            this.updateFromViewRotation();
        }
    }
    
    boolean requirePreDrawListener() {
        return true;
    }
    
    void setBackgroundDrawable(final ColorStateList list, final PorterDuff$Mode porterDuff$Mode, final int n, final int n2) {
        DrawableCompat.setTintList(this.mShapeDrawable = DrawableCompat.wrap((Drawable)this.createShapeDrawable()), list);
        if (porterDuff$Mode != null) {
            DrawableCompat.setTintMode(this.mShapeDrawable, porterDuff$Mode);
        }
        DrawableCompat.setTintList(this.mRippleDrawable = DrawableCompat.wrap((Drawable)this.createShapeDrawable()), createColorStateList(n));
        Drawable[] array;
        if (n2 > 0) {
            this.mBorderDrawable = this.createBorderDrawable(n2, list);
            array = new Drawable[] { this.mBorderDrawable, this.mShapeDrawable, this.mRippleDrawable };
        }
        else {
            this.mBorderDrawable = null;
            array = new Drawable[] { this.mShapeDrawable, this.mRippleDrawable };
        }
        this.mContentBackground = (Drawable)new LayerDrawable(array);
        final Context context = this.mView.getContext();
        final Drawable mContentBackground = this.mContentBackground;
        final float radius = this.mShadowViewDelegate.getRadius();
        final float mElevation = this.mElevation;
        (this.mShadowDrawable = new ShadowDrawableWrapper(context, mContentBackground, radius, mElevation, mElevation + this.mPressedTranslationZ)).setAddPaddingForCorners(false);
        this.mShadowViewDelegate.setBackgroundDrawable(this.mShadowDrawable);
    }
    
    void setBackgroundTintList(final ColorStateList borderTint) {
        final Drawable mShapeDrawable = this.mShapeDrawable;
        if (mShapeDrawable != null) {
            DrawableCompat.setTintList(mShapeDrawable, borderTint);
        }
        final CircularBorderDrawable mBorderDrawable = this.mBorderDrawable;
        if (mBorderDrawable != null) {
            mBorderDrawable.setBorderTint(borderTint);
        }
    }
    
    void setBackgroundTintMode(final PorterDuff$Mode porterDuff$Mode) {
        final Drawable mShapeDrawable = this.mShapeDrawable;
        if (mShapeDrawable != null) {
            DrawableCompat.setTintMode(mShapeDrawable, porterDuff$Mode);
        }
    }
    
    final void setElevation(final float mElevation) {
        if (this.mElevation != mElevation) {
            this.onElevationsChanged(this.mElevation = mElevation, this.mPressedTranslationZ);
        }
    }
    
    final void setPressedTranslationZ(final float mPressedTranslationZ) {
        if (this.mPressedTranslationZ != mPressedTranslationZ) {
            this.mPressedTranslationZ = mPressedTranslationZ;
            this.onElevationsChanged(this.mElevation, mPressedTranslationZ);
        }
    }
    
    void setRippleColor(final int n) {
        final Drawable mRippleDrawable = this.mRippleDrawable;
        if (mRippleDrawable != null) {
            DrawableCompat.setTintList(mRippleDrawable, createColorStateList(n));
        }
    }
    
    void show(@Nullable final InternalVisibilityChangedListener internalVisibilityChangedListener, final boolean b) {
        if (this.isOrWillBeShown()) {
            return;
        }
        this.mView.animate().cancel();
        if (this.shouldAnimateVisibilityChange()) {
            this.mAnimState = 2;
            if (this.mView.getVisibility() != 0) {
                this.mView.setAlpha(0.0f);
                this.mView.setScaleY(0.0f);
                this.mView.setScaleX(0.0f);
            }
            this.mView.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(200L).setInterpolator((TimeInterpolator)AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener((Animator$AnimatorListener)new AnimatorListenerAdapter() {
                public void onAnimationEnd(final Animator animator) {
                    FloatingActionButtonImpl.this.mAnimState = 0;
                    final InternalVisibilityChangedListener val$listener = internalVisibilityChangedListener;
                    if (val$listener != null) {
                        val$listener.onShown();
                    }
                }
                
                public void onAnimationStart(final Animator animator) {
                    FloatingActionButtonImpl.this.mView.internalSetVisibility(0, b);
                }
            });
            return;
        }
        this.mView.internalSetVisibility(0, b);
        this.mView.setAlpha(1.0f);
        this.mView.setScaleY(1.0f);
        this.mView.setScaleX(1.0f);
        if (internalVisibilityChangedListener != null) {
            internalVisibilityChangedListener.onShown();
        }
    }
    
    final void updatePadding() {
        final Rect mTmpRect = this.mTmpRect;
        this.getPadding(mTmpRect);
        this.onPaddingUpdated(mTmpRect);
        this.mShadowViewDelegate.setShadowPadding(mTmpRect.left, mTmpRect.top, mTmpRect.right, mTmpRect.bottom);
    }
    
    private class DisabledElevationAnimation extends ShadowAnimatorImpl
    {
        DisabledElevationAnimation() {
        }
        
        @Override
        protected float getTargetShadowSize() {
            return 0.0f;
        }
    }
    
    private class ElevateToTranslationZAnimation extends ShadowAnimatorImpl
    {
        ElevateToTranslationZAnimation() {
        }
        
        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.mElevation + FloatingActionButtonImpl.this.mPressedTranslationZ;
        }
    }
    
    interface InternalVisibilityChangedListener
    {
        void onHidden();
        
        void onShown();
    }
    
    private class ResetElevationAnimation extends ShadowAnimatorImpl
    {
        ResetElevationAnimation() {
        }
        
        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.mElevation;
        }
    }
    
    private abstract class ShadowAnimatorImpl extends AnimatorListenerAdapter implements ValueAnimator$AnimatorUpdateListener
    {
        private float mShadowSizeEnd;
        private float mShadowSizeStart;
        private boolean mValidValues;
        
        protected abstract float getTargetShadowSize();
        
        public void onAnimationEnd(final Animator animator) {
            FloatingActionButtonImpl.this.mShadowDrawable.setShadowSize(this.mShadowSizeEnd);
            this.mValidValues = false;
        }
        
        public void onAnimationUpdate(final ValueAnimator valueAnimator) {
            if (!this.mValidValues) {
                this.mShadowSizeStart = FloatingActionButtonImpl.this.mShadowDrawable.getShadowSize();
                this.mShadowSizeEnd = this.getTargetShadowSize();
                this.mValidValues = true;
            }
            final ShadowDrawableWrapper mShadowDrawable = FloatingActionButtonImpl.this.mShadowDrawable;
            final float mShadowSizeStart = this.mShadowSizeStart;
            mShadowDrawable.setShadowSize(mShadowSizeStart + (this.mShadowSizeEnd - mShadowSizeStart) * valueAnimator.getAnimatedFraction());
        }
    }
}
