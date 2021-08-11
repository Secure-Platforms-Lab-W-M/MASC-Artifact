/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.TimeInterpolator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Paint
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.ViewPropertyAnimator
 *  android.view.ViewTreeObserver
 *  android.view.ViewTreeObserver$OnPreDrawListener
 *  android.view.animation.Interpolator
 */
package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.R;
import android.support.design.widget.AnimationUtils;
import android.support.design.widget.CircularBorderDrawable;
import android.support.design.widget.ShadowDrawableWrapper;
import android.support.design.widget.ShadowViewDelegate;
import android.support.design.widget.StateListAnimator;
import android.support.design.widget.VisibilityAwareImageButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

@RequiresApi(value=14)
class FloatingActionButtonImpl {
    static final Interpolator ANIM_INTERPOLATOR = AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR;
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
    int mAnimState = 0;
    CircularBorderDrawable mBorderDrawable;
    Drawable mContentBackground;
    float mElevation;
    private ViewTreeObserver.OnPreDrawListener mPreDrawListener;
    float mPressedTranslationZ;
    Drawable mRippleDrawable;
    private float mRotation;
    ShadowDrawableWrapper mShadowDrawable;
    final ShadowViewDelegate mShadowViewDelegate;
    Drawable mShapeDrawable;
    private final StateListAnimator mStateListAnimator;
    private final Rect mTmpRect = new Rect();
    final VisibilityAwareImageButton mView;

    static {
        PRESSED_ENABLED_STATE_SET = new int[]{16842919, 16842910};
        FOCUSED_ENABLED_STATE_SET = new int[]{16842908, 16842910};
        ENABLED_STATE_SET = new int[]{16842910};
        EMPTY_STATE_SET = new int[0];
    }

    FloatingActionButtonImpl(VisibilityAwareImageButton visibilityAwareImageButton, ShadowViewDelegate shadowViewDelegate) {
        this.mView = visibilityAwareImageButton;
        this.mShadowViewDelegate = shadowViewDelegate;
        this.mStateListAnimator = new StateListAnimator();
        this.mStateListAnimator.addState(PRESSED_ENABLED_STATE_SET, this.createAnimator(new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, this.createAnimator(new ElevateToTranslationZAnimation()));
        this.mStateListAnimator.addState(ENABLED_STATE_SET, this.createAnimator(new ResetElevationAnimation()));
        this.mStateListAnimator.addState(EMPTY_STATE_SET, this.createAnimator(new DisabledElevationAnimation()));
        this.mRotation = this.mView.getRotation();
    }

    private ValueAnimator createAnimator(@NonNull ShadowAnimatorImpl shadowAnimatorImpl) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator((TimeInterpolator)ANIM_INTERPOLATOR);
        valueAnimator.setDuration(100L);
        valueAnimator.addListener((Animator.AnimatorListener)shadowAnimatorImpl);
        valueAnimator.addUpdateListener((ValueAnimator.AnimatorUpdateListener)shadowAnimatorImpl);
        valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
        return valueAnimator;
    }

    private static ColorStateList createColorStateList(int n) {
        int[][] arrarrn = new int[3][];
        int[] arrn = new int[3];
        arrarrn[0] = FOCUSED_ENABLED_STATE_SET;
        arrn[0] = n;
        int n2 = 0 + 1;
        arrarrn[n2] = PRESSED_ENABLED_STATE_SET;
        arrn[n2] = n;
        n = n2 + 1;
        arrarrn[n] = new int[0];
        arrn[n] = 0;
        return new ColorStateList((int[][])arrarrn, arrn);
    }

    private void ensurePreDrawListener() {
        if (this.mPreDrawListener == null) {
            this.mPreDrawListener = new ViewTreeObserver.OnPreDrawListener(){

                public boolean onPreDraw() {
                    FloatingActionButtonImpl.this.onPreDraw();
                    return true;
                }
            };
            return;
        }
    }

    private boolean shouldAnimateVisibilityChange() {
        if (ViewCompat.isLaidOut((View)this.mView) && !this.mView.isInEditMode()) {
            return true;
        }
        return false;
    }

    private void updateFromViewRotation() {
        Drawable drawable2;
        if (Build.VERSION.SDK_INT == 19) {
            if (this.mRotation % 90.0f != 0.0f) {
                if (this.mView.getLayerType() != 1) {
                    this.mView.setLayerType(1, null);
                }
            } else if (this.mView.getLayerType() != 0) {
                this.mView.setLayerType(0, null);
            }
        }
        if ((drawable2 = this.mShadowDrawable) != null) {
            drawable2.setRotation(- this.mRotation);
        }
        if ((drawable2 = this.mBorderDrawable) != null) {
            drawable2.setRotation(- this.mRotation);
            return;
        }
    }

    CircularBorderDrawable createBorderDrawable(int n, ColorStateList colorStateList) {
        Context context = this.mView.getContext();
        CircularBorderDrawable circularBorderDrawable = this.newCircularDrawable();
        circularBorderDrawable.setGradientColors(ContextCompat.getColor(context, R.color.design_fab_stroke_top_outer_color), ContextCompat.getColor(context, R.color.design_fab_stroke_top_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_inner_color), ContextCompat.getColor(context, R.color.design_fab_stroke_end_outer_color));
        circularBorderDrawable.setBorderWidth(n);
        circularBorderDrawable.setBorderTint(colorStateList);
        return circularBorderDrawable;
    }

    GradientDrawable createShapeDrawable() {
        GradientDrawable gradientDrawable = this.newGradientDrawableForShape();
        gradientDrawable.setShape(1);
        gradientDrawable.setColor(-1);
        return gradientDrawable;
    }

    final Drawable getContentBackground() {
        return this.mContentBackground;
    }

    float getElevation() {
        return this.mElevation;
    }

    void getPadding(Rect rect) {
        this.mShadowDrawable.getPadding(rect);
    }

    void hide(final @Nullable InternalVisibilityChangedListener internalVisibilityChangedListener, final boolean bl) {
        if (this.isOrWillBeHidden()) {
            return;
        }
        this.mView.animate().cancel();
        if (this.shouldAnimateVisibilityChange()) {
            this.mAnimState = 1;
            this.mView.animate().scaleX(0.0f).scaleY(0.0f).alpha(0.0f).setDuration(200L).setInterpolator((TimeInterpolator)AnimationUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){
                private boolean mCancelled;

                public void onAnimationCancel(Animator animator2) {
                    this.mCancelled = true;
                }

                public void onAnimationEnd(Animator object) {
                    object = FloatingActionButtonImpl.this;
                    object.mAnimState = 0;
                    if (!this.mCancelled) {
                        object = object.mView;
                        int n = bl ? 8 : 4;
                        object.internalSetVisibility(n, bl);
                        object = internalVisibilityChangedListener;
                        if (object != null) {
                            object.onHidden();
                            return;
                        }
                        return;
                    }
                }

                public void onAnimationStart(Animator animator2) {
                    FloatingActionButtonImpl.this.mView.internalSetVisibility(0, bl);
                    this.mCancelled = false;
                }
            });
            return;
        }
        VisibilityAwareImageButton visibilityAwareImageButton = this.mView;
        int n = bl ? 8 : 4;
        visibilityAwareImageButton.internalSetVisibility(n, bl);
        if (internalVisibilityChangedListener != null) {
            internalVisibilityChangedListener.onHidden();
            return;
        }
    }

    boolean isOrWillBeHidden() {
        int n = this.mView.getVisibility();
        boolean bl = false;
        boolean bl2 = false;
        if (n == 0) {
            if (this.mAnimState == 1) {
                bl2 = true;
            }
            return bl2;
        }
        bl2 = bl;
        if (this.mAnimState != 2) {
            bl2 = true;
        }
        return bl2;
    }

    boolean isOrWillBeShown() {
        int n = this.mView.getVisibility();
        boolean bl = false;
        boolean bl2 = false;
        if (n != 0) {
            if (this.mAnimState == 2) {
                bl2 = true;
            }
            return bl2;
        }
        bl2 = bl;
        if (this.mAnimState != 1) {
            bl2 = true;
        }
        return bl2;
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
            return;
        }
    }

    void onCompatShadowChanged() {
    }

    void onDetachedFromWindow() {
        if (this.mPreDrawListener != null) {
            this.mView.getViewTreeObserver().removeOnPreDrawListener(this.mPreDrawListener);
            this.mPreDrawListener = null;
            return;
        }
    }

    void onDrawableStateChanged(int[] arrn) {
        this.mStateListAnimator.setState(arrn);
    }

    void onElevationsChanged(float f, float f2) {
        ShadowDrawableWrapper shadowDrawableWrapper = this.mShadowDrawable;
        if (shadowDrawableWrapper != null) {
            shadowDrawableWrapper.setShadowSize(f, this.mPressedTranslationZ + f);
            this.updatePadding();
            return;
        }
    }

    void onPaddingUpdated(Rect rect) {
    }

    void onPreDraw() {
        float f = this.mView.getRotation();
        if (this.mRotation != f) {
            this.mRotation = f;
            this.updateFromViewRotation();
            return;
        }
    }

    boolean requirePreDrawListener() {
        return true;
    }

    void setBackgroundDrawable(ColorStateList context, PorterDuff.Mode mode, int n, int n2) {
        this.mShapeDrawable = DrawableCompat.wrap((Drawable)this.createShapeDrawable());
        DrawableCompat.setTintList(this.mShapeDrawable, (ColorStateList)context);
        if (mode != null) {
            DrawableCompat.setTintMode(this.mShapeDrawable, mode);
        }
        this.mRippleDrawable = DrawableCompat.wrap((Drawable)this.createShapeDrawable());
        DrawableCompat.setTintList(this.mRippleDrawable, FloatingActionButtonImpl.createColorStateList(n));
        if (n2 > 0) {
            this.mBorderDrawable = this.createBorderDrawable(n2, (ColorStateList)context);
            context = new Drawable[]{this.mBorderDrawable, this.mShapeDrawable, this.mRippleDrawable};
        } else {
            this.mBorderDrawable = null;
            context = new Context[]{this.mShapeDrawable, this.mRippleDrawable};
        }
        this.mContentBackground = new LayerDrawable((Drawable[])context);
        context = this.mView.getContext();
        mode = this.mContentBackground;
        float f = this.mShadowViewDelegate.getRadius();
        float f2 = this.mElevation;
        this.mShadowDrawable = new ShadowDrawableWrapper(context, (Drawable)mode, f, f2, f2 + this.mPressedTranslationZ);
        this.mShadowDrawable.setAddPaddingForCorners(false);
        this.mShadowViewDelegate.setBackgroundDrawable(this.mShadowDrawable);
    }

    void setBackgroundTintList(ColorStateList colorStateList) {
        Drawable drawable2 = this.mShapeDrawable;
        if (drawable2 != null) {
            DrawableCompat.setTintList(drawable2, colorStateList);
        }
        if ((drawable2 = this.mBorderDrawable) != null) {
            drawable2.setBorderTint(colorStateList);
            return;
        }
    }

    void setBackgroundTintMode(PorterDuff.Mode mode) {
        Drawable drawable2 = this.mShapeDrawable;
        if (drawable2 != null) {
            DrawableCompat.setTintMode(drawable2, mode);
            return;
        }
    }

    final void setElevation(float f) {
        if (this.mElevation != f) {
            this.mElevation = f;
            this.onElevationsChanged(f, this.mPressedTranslationZ);
            return;
        }
    }

    final void setPressedTranslationZ(float f) {
        if (this.mPressedTranslationZ != f) {
            this.mPressedTranslationZ = f;
            this.onElevationsChanged(this.mElevation, f);
            return;
        }
    }

    void setRippleColor(int n) {
        Drawable drawable2 = this.mRippleDrawable;
        if (drawable2 != null) {
            DrawableCompat.setTintList(drawable2, FloatingActionButtonImpl.createColorStateList(n));
            return;
        }
    }

    void show(final @Nullable InternalVisibilityChangedListener internalVisibilityChangedListener, final boolean bl) {
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
            this.mView.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setDuration(200L).setInterpolator((TimeInterpolator)AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR).setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(){

                public void onAnimationEnd(Animator object) {
                    FloatingActionButtonImpl.this.mAnimState = 0;
                    object = internalVisibilityChangedListener;
                    if (object != null) {
                        object.onShown();
                        return;
                    }
                }

                public void onAnimationStart(Animator animator2) {
                    FloatingActionButtonImpl.this.mView.internalSetVisibility(0, bl);
                }
            });
            return;
        }
        this.mView.internalSetVisibility(0, bl);
        this.mView.setAlpha(1.0f);
        this.mView.setScaleY(1.0f);
        this.mView.setScaleX(1.0f);
        if (internalVisibilityChangedListener != null) {
            internalVisibilityChangedListener.onShown();
            return;
        }
    }

    final void updatePadding() {
        Rect rect = this.mTmpRect;
        this.getPadding(rect);
        this.onPaddingUpdated(rect);
        this.mShadowViewDelegate.setShadowPadding(rect.left, rect.top, rect.right, rect.bottom);
    }

    private class DisabledElevationAnimation
    extends ShadowAnimatorImpl {
        DisabledElevationAnimation() {
            super();
        }

        @Override
        protected float getTargetShadowSize() {
            return 0.0f;
        }
    }

    private class ElevateToTranslationZAnimation
    extends ShadowAnimatorImpl {
        ElevateToTranslationZAnimation() {
            super();
        }

        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.mElevation + FloatingActionButtonImpl.this.mPressedTranslationZ;
        }
    }

    static interface InternalVisibilityChangedListener {
        public void onHidden();

        public void onShown();
    }

    private class ResetElevationAnimation
    extends ShadowAnimatorImpl {
        ResetElevationAnimation() {
            super();
        }

        @Override
        protected float getTargetShadowSize() {
            return FloatingActionButtonImpl.this.mElevation;
        }
    }

    private abstract class ShadowAnimatorImpl
    extends AnimatorListenerAdapter
    implements ValueAnimator.AnimatorUpdateListener {
        private float mShadowSizeEnd;
        private float mShadowSizeStart;
        private boolean mValidValues;

        private ShadowAnimatorImpl() {
        }

        protected abstract float getTargetShadowSize();

        public void onAnimationEnd(Animator animator2) {
            FloatingActionButtonImpl.this.mShadowDrawable.setShadowSize(this.mShadowSizeEnd);
            this.mValidValues = false;
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            if (!this.mValidValues) {
                this.mShadowSizeStart = FloatingActionButtonImpl.this.mShadowDrawable.getShadowSize();
                this.mShadowSizeEnd = this.getTargetShadowSize();
                this.mValidValues = true;
            }
            ShadowDrawableWrapper shadowDrawableWrapper = FloatingActionButtonImpl.this.mShadowDrawable;
            float f = this.mShadowSizeStart;
            shadowDrawableWrapper.setShadowSize(f + (this.mShadowSizeEnd - f) * valueAnimator.getAnimatedFraction());
        }
    }

}

