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
 *  android.graphics.drawable.RippleDrawable
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
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
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.graphics.drawable.DrawableWrapperApi14;
import android.support.v4.graphics.drawable.DrawableWrapperApi19;
import android.util.Log;
import java.lang.reflect.Method;

@RequiresApi(value=21)
class DrawableWrapperApi21
extends DrawableWrapperApi19 {
    private static final String TAG = "DrawableWrapperApi21";
    private static Method sIsProjectedDrawableMethod;

    DrawableWrapperApi21(Drawable drawable2) {
        super(drawable2);
        this.findAndCacheIsProjectedDrawableMethod();
    }

    DrawableWrapperApi21(DrawableWrapperApi14.DrawableWrapperState drawableWrapperState, Resources resources) {
        super(drawableWrapperState, resources);
        this.findAndCacheIsProjectedDrawableMethod();
    }

    private void findAndCacheIsProjectedDrawableMethod() {
        if (sIsProjectedDrawableMethod == null) {
            try {
                sIsProjectedDrawableMethod = Drawable.class.getDeclaredMethod("isProjected", new Class[0]);
                return;
            }
            catch (Exception exception) {
                Log.w((String)"DrawableWrapperApi21", (String)"Failed to retrieve Drawable#isProjected() method", (Throwable)exception);
                return;
            }
        }
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
            Drawable drawable2 = this.mDrawable;
            if (!(drawable2 instanceof GradientDrawable || drawable2 instanceof DrawableContainer || drawable2 instanceof InsetDrawable || drawable2 instanceof RippleDrawable)) {
                return false;
            }
            return true;
        }
        return false;
    }

    @RestrictTo(value={RestrictTo.Scope.LIBRARY_GROUP})
    public boolean isProjected() {
        Method method;
        if (this.mDrawable != null && (method = sIsProjectedDrawableMethod) != null) {
            try {
                boolean bl = (Boolean)method.invoke((Object)this.mDrawable, new Object[0]);
                return bl;
            }
            catch (Exception exception) {
                Log.w((String)"DrawableWrapperApi21", (String)"Error calling Drawable#isProjected() method", (Throwable)exception);
                return false;
            }
        }
        return false;
    }

    @NonNull
    @Override
    DrawableWrapperApi14.DrawableWrapperState mutateConstantState() {
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

    @Override
    public void setTint(int n) {
        if (this.isCompatTintEnabled()) {
            super.setTint(n);
            return;
        }
        this.mDrawable.setTint(n);
    }

    @Override
    public void setTintList(ColorStateList colorStateList) {
        if (this.isCompatTintEnabled()) {
            super.setTintList(colorStateList);
            return;
        }
        this.mDrawable.setTintList(colorStateList);
    }

    @Override
    public void setTintMode(PorterDuff.Mode mode) {
        if (this.isCompatTintEnabled()) {
            super.setTintMode(mode);
            return;
        }
        this.mDrawable.setTintMode(mode);
    }

    private static class DrawableWrapperStateLollipop
    extends DrawableWrapperApi14.DrawableWrapperState {
        DrawableWrapperStateLollipop(@Nullable DrawableWrapperApi14.DrawableWrapperState drawableWrapperState, @Nullable Resources resources) {
            super(drawableWrapperState, resources);
        }

        @Override
        public Drawable newDrawable(@Nullable Resources resources) {
            return new DrawableWrapperApi21(this, resources);
        }
    }

}

