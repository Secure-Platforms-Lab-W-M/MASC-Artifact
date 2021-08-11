/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.graphics.Outline
 *  android.graphics.PorterDuff
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.DrawableContainer
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.InsetDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableWrapperDonut;
import android.support.v4.graphics.drawable.DrawableWrapperKitKat;

class DrawableWrapperLollipop
extends DrawableWrapperKitKat {
    DrawableWrapperLollipop(Drawable drawable) {
        super(drawable);
    }

    DrawableWrapperLollipop(DrawableWrapperDonut.DrawableWrapperState drawableWrapperState, Resources resources) {
        super(drawableWrapperState, resources);
    }

    public Rect getDirtyBounds() {
        return this.mDrawable.getDirtyBounds();
    }

    public void getOutline(Outline outline) {
        this.mDrawable.getOutline(outline);
    }

    @Override
    protected boolean isCompatTintEnabled() {
        if (Build.VERSION.SDK_INT == 21) {
            Drawable drawable = this.mDrawable;
            if (!(drawable instanceof GradientDrawable || drawable instanceof DrawableContainer || drawable instanceof InsetDrawable)) {
                return false;
            }
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    DrawableWrapperDonut.DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateLollipop(this.mState, null);
    }

    public void setHotspot(float f, float f2) {
        this.mDrawable.setHotspot(f, f2);
    }

    public void setHotspotBounds(int n, int n2, int n3, int n4) {
        this.mDrawable.setHotspotBounds(n, n2, n3, n4);
    }

    @Override
    public boolean setState(int[] arrn) {
        if (super.setState(arrn)) {
            this.invalidateSelf();
            return true;
        }
        return false;
    }

    public void setTint(int n) {
        if (this.isCompatTintEnabled()) {
            this.setCompatTint(n);
            return;
        }
        this.mDrawable.setTint(n);
    }

    public void setTintList(ColorStateList colorStateList) {
        if (this.isCompatTintEnabled()) {
            this.setCompatTintList(colorStateList);
            return;
        }
        this.mDrawable.setTintList(colorStateList);
    }

    public void setTintMode(PorterDuff.Mode mode) {
        if (this.isCompatTintEnabled()) {
            this.setCompatTintMode(mode);
            return;
        }
        this.mDrawable.setTintMode(mode);
    }

    private static class DrawableWrapperStateLollipop
    extends DrawableWrapperDonut.DrawableWrapperState {
        DrawableWrapperStateLollipop(@Nullable DrawableWrapperDonut.DrawableWrapperState drawableWrapperState, @Nullable Resources resources) {
            super(drawableWrapperState, resources);
        }

        @Override
        public Drawable newDrawable(@Nullable Resources resources) {
            return new DrawableWrapperLollipop(this, resources);
        }
    }

}

