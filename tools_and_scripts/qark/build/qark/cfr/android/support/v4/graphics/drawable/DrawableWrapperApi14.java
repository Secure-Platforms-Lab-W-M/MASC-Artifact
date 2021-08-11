/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.Canvas
 *  android.graphics.ColorFilter
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.Region
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.Drawable$Callback
 *  android.graphics.drawable.Drawable$ConstantState
 */
package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.graphics.drawable.DrawableWrapper;
import android.support.v4.graphics.drawable.TintAwareDrawable;

@RequiresApi(value=14)
class DrawableWrapperApi14
extends Drawable
implements Drawable.Callback,
DrawableWrapper,
TintAwareDrawable {
    static final PorterDuff.Mode DEFAULT_TINT_MODE = PorterDuff.Mode.SRC_IN;
    private boolean mColorFilterSet;
    private int mCurrentColor;
    private PorterDuff.Mode mCurrentMode;
    Drawable mDrawable;
    private boolean mMutated;
    DrawableWrapperState mState;

    DrawableWrapperApi14(@Nullable Drawable drawable2) {
        this.mState = this.mutateConstantState();
        this.setWrappedDrawable(drawable2);
    }

    DrawableWrapperApi14(@NonNull DrawableWrapperState drawableWrapperState, @Nullable Resources resources) {
        this.mState = drawableWrapperState;
        this.updateLocalState(resources);
    }

    private void updateLocalState(@Nullable Resources resources) {
        DrawableWrapperState drawableWrapperState = this.mState;
        if (drawableWrapperState != null && drawableWrapperState.mDrawableState != null) {
            this.setWrappedDrawable(this.newDrawableFromState(this.mState.mDrawableState, resources));
            return;
        }
    }

    private boolean updateTint(int[] arrn) {
        if (!this.isCompatTintEnabled()) {
            return false;
        }
        ColorStateList colorStateList = this.mState.mTint;
        PorterDuff.Mode mode = this.mState.mTintMode;
        if (colorStateList != null && mode != null) {
            int n = colorStateList.getColorForState(arrn, colorStateList.getDefaultColor());
            if (this.mColorFilterSet && n == this.mCurrentColor && mode == this.mCurrentMode) {
                return false;
            }
            this.setColorFilter(n, mode);
            this.mCurrentColor = n;
            this.mCurrentMode = mode;
            this.mColorFilterSet = true;
            return true;
        }
        this.mColorFilterSet = false;
        this.clearColorFilter();
        return false;
    }

    public void draw(Canvas canvas) {
        this.mDrawable.draw(canvas);
    }

    public int getChangingConfigurations() {
        int n = super.getChangingConfigurations();
        DrawableWrapperState drawableWrapperState = this.mState;
        int n2 = drawableWrapperState != null ? drawableWrapperState.getChangingConfigurations() : 0;
        return n | n2 | this.mDrawable.getChangingConfigurations();
    }

    @Nullable
    public Drawable.ConstantState getConstantState() {
        DrawableWrapperState drawableWrapperState = this.mState;
        if (drawableWrapperState != null && drawableWrapperState.canConstantState()) {
            this.mState.mChangingConfigurations = this.getChangingConfigurations();
            return this.mState;
        }
        return null;
    }

    public Drawable getCurrent() {
        return this.mDrawable.getCurrent();
    }

    public int getIntrinsicHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }

    public int getIntrinsicWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }

    public int getMinimumHeight() {
        return this.mDrawable.getMinimumHeight();
    }

    public int getMinimumWidth() {
        return this.mDrawable.getMinimumWidth();
    }

    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }

    public boolean getPadding(Rect rect) {
        return this.mDrawable.getPadding(rect);
    }

    public int[] getState() {
        return this.mDrawable.getState();
    }

    public Region getTransparentRegion() {
        return this.mDrawable.getTransparentRegion();
    }

    @Override
    public final Drawable getWrappedDrawable() {
        return this.mDrawable;
    }

    public void invalidateDrawable(Drawable drawable2) {
        this.invalidateSelf();
    }

    protected boolean isCompatTintEnabled() {
        return true;
    }

    public boolean isStateful() {
        DrawableWrapperState drawableWrapperState;
        drawableWrapperState = this.isCompatTintEnabled() && (drawableWrapperState = this.mState) != null ? drawableWrapperState.mTint : null;
        if (drawableWrapperState != null && drawableWrapperState.isStateful() || this.mDrawable.isStateful()) {
            return true;
        }
        return false;
    }

    public void jumpToCurrentState() {
        this.mDrawable.jumpToCurrentState();
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            DrawableWrapperState drawableWrapperState;
            this.mState = this.mutateConstantState();
            Drawable drawable2 = this.mDrawable;
            if (drawable2 != null) {
                drawable2.mutate();
            }
            if ((drawableWrapperState = this.mState) != null) {
                drawable2 = this.mDrawable;
                drawable2 = drawable2 != null ? drawable2.getConstantState() : null;
                drawableWrapperState.mDrawableState = drawable2;
            }
            this.mMutated = true;
            return this;
        }
        return this;
    }

    @NonNull
    DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateBase(this.mState, null);
    }

    protected Drawable newDrawableFromState(@NonNull Drawable.ConstantState constantState, @Nullable Resources resources) {
        return constantState.newDrawable(resources);
    }

    protected void onBoundsChange(Rect rect) {
        Drawable drawable2 = this.mDrawable;
        if (drawable2 != null) {
            drawable2.setBounds(rect);
            return;
        }
    }

    protected boolean onLevelChange(int n) {
        return this.mDrawable.setLevel(n);
    }

    public void scheduleDrawable(Drawable drawable2, Runnable runnable, long l) {
        this.scheduleSelf(runnable, l);
    }

    public void setAlpha(int n) {
        this.mDrawable.setAlpha(n);
    }

    public void setChangingConfigurations(int n) {
        this.mDrawable.setChangingConfigurations(n);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mDrawable.setColorFilter(colorFilter);
    }

    public void setDither(boolean bl) {
        this.mDrawable.setDither(bl);
    }

    public void setFilterBitmap(boolean bl) {
        this.mDrawable.setFilterBitmap(bl);
    }

    public boolean setState(int[] arrn) {
        boolean bl = this.mDrawable.setState(arrn);
        bl = this.updateTint(arrn) || bl;
        return bl;
    }

    @Override
    public void setTint(int n) {
        this.setTintList(ColorStateList.valueOf((int)n));
    }

    @Override
    public void setTintList(ColorStateList colorStateList) {
        this.mState.mTint = colorStateList;
        this.updateTint(this.getState());
    }

    @Override
    public void setTintMode(PorterDuff.Mode mode) {
        this.mState.mTintMode = mode;
        this.updateTint(this.getState());
    }

    public boolean setVisible(boolean bl, boolean bl2) {
        if (!super.setVisible(bl, bl2) && !this.mDrawable.setVisible(bl, bl2)) {
            return false;
        }
        return true;
    }

    @Override
    public final void setWrappedDrawable(Drawable drawable2) {
        Object object = this.mDrawable;
        if (object != null) {
            object.setCallback(null);
        }
        this.mDrawable = drawable2;
        if (drawable2 != null) {
            drawable2.setCallback((Drawable.Callback)this);
            this.setVisible(drawable2.isVisible(), true);
            this.setState(drawable2.getState());
            this.setLevel(drawable2.getLevel());
            this.setBounds(drawable2.getBounds());
            object = this.mState;
            if (object != null) {
                object.mDrawableState = drawable2.getConstantState();
            }
        }
        this.invalidateSelf();
    }

    public void unscheduleDrawable(Drawable drawable2, Runnable runnable) {
        this.unscheduleSelf(runnable);
    }

    protected static abstract class DrawableWrapperState
    extends Drawable.ConstantState {
        int mChangingConfigurations;
        Drawable.ConstantState mDrawableState;
        ColorStateList mTint = null;
        PorterDuff.Mode mTintMode = DrawableWrapperApi14.DEFAULT_TINT_MODE;

        DrawableWrapperState(@Nullable DrawableWrapperState drawableWrapperState, @Nullable Resources resources) {
            if (drawableWrapperState != null) {
                this.mChangingConfigurations = drawableWrapperState.mChangingConfigurations;
                this.mDrawableState = drawableWrapperState.mDrawableState;
                this.mTint = drawableWrapperState.mTint;
                this.mTintMode = drawableWrapperState.mTintMode;
                return;
            }
        }

        boolean canConstantState() {
            if (this.mDrawableState != null) {
                return true;
            }
            return false;
        }

        public int getChangingConfigurations() {
            int n = this.mChangingConfigurations;
            Drawable.ConstantState constantState = this.mDrawableState;
            int n2 = constantState != null ? constantState.getChangingConfigurations() : 0;
            return n | n2;
        }

        public Drawable newDrawable() {
            return this.newDrawable(null);
        }

        public abstract Drawable newDrawable(@Nullable Resources var1);
    }

    private static class DrawableWrapperStateBase
    extends DrawableWrapperState {
        DrawableWrapperStateBase(@Nullable DrawableWrapperState drawableWrapperState, @Nullable Resources resources) {
            super(drawableWrapperState, resources);
        }

        @Override
        public Drawable newDrawable(@Nullable Resources resources) {
            return new DrawableWrapperApi14(this, resources);
        }
    }

}

