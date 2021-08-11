/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.AnimatorSet
 *  android.animation.AnimatorSet$Builder
 *  android.animation.ObjectAnimator
 *  android.animation.StateListAnimator
 *  android.animation.TimeInterpolator
 *  android.content.res.ColorStateList
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.InsetDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.graphics.drawable.RippleDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Property
 *  android.view.View
 *  android.view.animation.Interpolator
 */
package android.support.design.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.animation.TimeInterpolator;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CircularBorderDrawable;
import android.support.design.widget.CircularBorderDrawableLollipop;
import android.support.design.widget.FloatingActionButtonImpl;
import android.support.design.widget.ShadowDrawableWrapper;
import android.support.design.widget.ShadowViewDelegate;
import android.support.design.widget.VisibilityAwareImageButton;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Property;
import android.view.View;
import android.view.animation.Interpolator;
import java.util.ArrayList;

@RequiresApi(value=21)
class FloatingActionButtonLollipop
extends FloatingActionButtonImpl {
    private InsetDrawable mInsetDrawable;

    FloatingActionButtonLollipop(VisibilityAwareImageButton visibilityAwareImageButton, ShadowViewDelegate shadowViewDelegate) {
        super(visibilityAwareImageButton, shadowViewDelegate);
    }

    @Override
    public float getElevation() {
        return this.mView.getElevation();
    }

    @Override
    void getPadding(Rect rect) {
        if (this.mShadowViewDelegate.isCompatPaddingEnabled()) {
            float f = this.mShadowViewDelegate.getRadius();
            float f2 = this.getElevation() + this.mPressedTranslationZ;
            int n = (int)Math.ceil(ShadowDrawableWrapper.calculateHorizontalPadding(f2, f, false));
            int n2 = (int)Math.ceil(ShadowDrawableWrapper.calculateVerticalPadding(f2, f, false));
            rect.set(n, n2, n, n2);
            return;
        }
        rect.set(0, 0, 0, 0);
    }

    @Override
    void jumpDrawableToCurrentState() {
    }

    @Override
    CircularBorderDrawable newCircularDrawable() {
        return new CircularBorderDrawableLollipop();
    }

    @Override
    GradientDrawable newGradientDrawableForShape() {
        return new AlwaysStatefulGradientDrawable();
    }

    @Override
    void onCompatShadowChanged() {
        this.updatePadding();
    }

    @Override
    void onDrawableStateChanged(int[] arrn) {
    }

    @Override
    void onElevationsChanged(float f, float f2) {
        if (Build.VERSION.SDK_INT == 21) {
            if (this.mView.isEnabled()) {
                this.mView.setElevation(f);
                if (!this.mView.isFocused() && !this.mView.isPressed()) {
                    this.mView.setTranslationZ(0.0f);
                } else {
                    this.mView.setTranslationZ(f2);
                }
            } else {
                this.mView.setElevation(0.0f);
                this.mView.setTranslationZ(0.0f);
            }
        } else {
            StateListAnimator stateListAnimator = new StateListAnimator();
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play((Animator)ObjectAnimator.ofFloat((Object)((Object)this.mView), (String)"elevation", (float[])new float[]{f}).setDuration(0L)).with((Animator)ObjectAnimator.ofFloat((Object)((Object)this.mView), (Property)View.TRANSLATION_Z, (float[])new float[]{f2}).setDuration(100L));
            animatorSet.setInterpolator((TimeInterpolator)ANIM_INTERPOLATOR);
            stateListAnimator.addState(PRESSED_ENABLED_STATE_SET, (Animator)animatorSet);
            animatorSet = new AnimatorSet();
            animatorSet.play((Animator)ObjectAnimator.ofFloat((Object)((Object)this.mView), (String)"elevation", (float[])new float[]{f}).setDuration(0L)).with((Animator)ObjectAnimator.ofFloat((Object)((Object)this.mView), (Property)View.TRANSLATION_Z, (float[])new float[]{f2}).setDuration(100L));
            animatorSet.setInterpolator((TimeInterpolator)ANIM_INTERPOLATOR);
            stateListAnimator.addState(FOCUSED_ENABLED_STATE_SET, (Animator)animatorSet);
            animatorSet = new AnimatorSet();
            ArrayList<ObjectAnimator> arrayList = new ArrayList<ObjectAnimator>();
            arrayList.add(ObjectAnimator.ofFloat((Object)((Object)this.mView), (String)"elevation", (float[])new float[]{f}).setDuration(0L));
            if (Build.VERSION.SDK_INT >= 22 && Build.VERSION.SDK_INT <= 24) {
                arrayList.add(ObjectAnimator.ofFloat((Object)((Object)this.mView), (Property)View.TRANSLATION_Z, (float[])new float[]{this.mView.getTranslationZ()}).setDuration(100L));
            }
            arrayList.add(ObjectAnimator.ofFloat((Object)((Object)this.mView), (Property)View.TRANSLATION_Z, (float[])new float[]{0.0f}).setDuration(100L));
            animatorSet.playSequentially((Animator[])arrayList.toArray((T[])new ObjectAnimator[0]));
            animatorSet.setInterpolator((TimeInterpolator)ANIM_INTERPOLATOR);
            stateListAnimator.addState(ENABLED_STATE_SET, (Animator)animatorSet);
            animatorSet = new AnimatorSet();
            animatorSet.play((Animator)ObjectAnimator.ofFloat((Object)((Object)this.mView), (String)"elevation", (float[])new float[]{0.0f}).setDuration(0L)).with((Animator)ObjectAnimator.ofFloat((Object)((Object)this.mView), (Property)View.TRANSLATION_Z, (float[])new float[]{0.0f}).setDuration(0L));
            animatorSet.setInterpolator((TimeInterpolator)ANIM_INTERPOLATOR);
            stateListAnimator.addState(EMPTY_STATE_SET, (Animator)animatorSet);
            this.mView.setStateListAnimator(stateListAnimator);
        }
        if (this.mShadowViewDelegate.isCompatPaddingEnabled()) {
            this.updatePadding();
            return;
        }
    }

    @Override
    void onPaddingUpdated(Rect rect) {
        if (this.mShadowViewDelegate.isCompatPaddingEnabled()) {
            this.mInsetDrawable = new InsetDrawable(this.mRippleDrawable, rect.left, rect.top, rect.right, rect.bottom);
            this.mShadowViewDelegate.setBackgroundDrawable((Drawable)this.mInsetDrawable);
            return;
        }
        this.mShadowViewDelegate.setBackgroundDrawable(this.mRippleDrawable);
    }

    @Override
    boolean requirePreDrawListener() {
        return false;
    }

    @Override
    void setBackgroundDrawable(ColorStateList colorStateList, PorterDuff.Mode mode, int n, int n2) {
        this.mShapeDrawable = DrawableCompat.wrap((Drawable)this.createShapeDrawable());
        DrawableCompat.setTintList(this.mShapeDrawable, colorStateList);
        if (mode != null) {
            DrawableCompat.setTintMode(this.mShapeDrawable, mode);
        }
        if (n2 > 0) {
            this.mBorderDrawable = this.createBorderDrawable(n2, colorStateList);
            colorStateList = new LayerDrawable(new Drawable[]{this.mBorderDrawable, this.mShapeDrawable});
        } else {
            this.mBorderDrawable = null;
            colorStateList = this.mShapeDrawable;
        }
        this.mContentBackground = this.mRippleDrawable = new RippleDrawable(ColorStateList.valueOf((int)n), (Drawable)colorStateList, null);
        this.mShadowViewDelegate.setBackgroundDrawable(this.mRippleDrawable);
    }

    @Override
    void setRippleColor(int n) {
        if (this.mRippleDrawable instanceof RippleDrawable) {
            ((RippleDrawable)this.mRippleDrawable).setColor(ColorStateList.valueOf((int)n));
            return;
        }
        super.setRippleColor(n);
    }

    static class AlwaysStatefulGradientDrawable
    extends GradientDrawable {
        AlwaysStatefulGradientDrawable() {
        }

        public boolean isStateful() {
            return true;
        }
    }

}

