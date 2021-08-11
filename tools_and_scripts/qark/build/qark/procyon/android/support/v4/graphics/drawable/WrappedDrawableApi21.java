// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.graphics.drawable;

import android.support.annotation.Nullable;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.os.Build$VERSION;
import android.graphics.Outline;
import android.support.annotation.NonNull;
import android.graphics.Rect;
import android.util.Log;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import java.lang.reflect.Method;
import android.support.annotation.RequiresApi;

@RequiresApi(21)
class WrappedDrawableApi21 extends WrappedDrawableApi19
{
    private static final String TAG = "WrappedDrawableApi21";
    private static Method sIsProjectedDrawableMethod;
    
    WrappedDrawableApi21(final Drawable drawable) {
        super(drawable);
        this.findAndCacheIsProjectedDrawableMethod();
    }
    
    WrappedDrawableApi21(final DrawableWrapperState drawableWrapperState, final Resources resources) {
        super(drawableWrapperState, resources);
        this.findAndCacheIsProjectedDrawableMethod();
    }
    
    private void findAndCacheIsProjectedDrawableMethod() {
        if (WrappedDrawableApi21.sIsProjectedDrawableMethod != null) {
            return;
        }
        try {
            WrappedDrawableApi21.sIsProjectedDrawableMethod = Drawable.class.getDeclaredMethod("isProjected", (Class<?>[])new Class[0]);
        }
        catch (Exception ex) {
            Log.w("WrappedDrawableApi21", "Failed to retrieve Drawable#isProjected() method", (Throwable)ex);
        }
    }
    
    @NonNull
    public Rect getDirtyBounds() {
        return this.mDrawable.getDirtyBounds();
    }
    
    public void getOutline(@NonNull final Outline outline) {
        this.mDrawable.getOutline(outline);
    }
    
    @Override
    protected boolean isCompatTintEnabled() {
        boolean b = false;
        if (Build$VERSION.SDK_INT == 21) {
            final Drawable mDrawable = this.mDrawable;
            if (!(mDrawable instanceof GradientDrawable) && !(mDrawable instanceof DrawableContainer) && !(mDrawable instanceof InsetDrawable)) {
                b = b;
                if (!(mDrawable instanceof RippleDrawable)) {
                    return b;
                }
            }
            b = true;
        }
        return b;
    }
    
    public boolean isProjected() {
        if (this.mDrawable != null && WrappedDrawableApi21.sIsProjectedDrawableMethod != null) {
            try {
                return (boolean)WrappedDrawableApi21.sIsProjectedDrawableMethod.invoke(this.mDrawable, new Object[0]);
            }
            catch (Exception ex) {
                Log.w("WrappedDrawableApi21", "Error calling Drawable#isProjected() method", (Throwable)ex);
            }
        }
        return false;
    }
    
    @NonNull
    @Override
    DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateLollipop(this.mState, null);
    }
    
    public void setHotspot(final float n, final float n2) {
        this.mDrawable.setHotspot(n, n2);
    }
    
    public void setHotspotBounds(final int n, final int n2, final int n3, final int n4) {
        this.mDrawable.setHotspotBounds(n, n2, n3, n4);
    }
    
    @Override
    public boolean setState(@NonNull final int[] state) {
        if (super.setState(state)) {
            this.invalidateSelf();
            return true;
        }
        return false;
    }
    
    @Override
    public void setTint(final int n) {
        if (this.isCompatTintEnabled()) {
            super.setTint(n);
            return;
        }
        this.mDrawable.setTint(n);
    }
    
    @Override
    public void setTintList(final ColorStateList list) {
        if (this.isCompatTintEnabled()) {
            super.setTintList(list);
            return;
        }
        this.mDrawable.setTintList(list);
    }
    
    @Override
    public void setTintMode(final PorterDuff$Mode porterDuff$Mode) {
        if (this.isCompatTintEnabled()) {
            super.setTintMode(porterDuff$Mode);
            return;
        }
        this.mDrawable.setTintMode(porterDuff$Mode);
    }
    
    private static class DrawableWrapperStateLollipop extends DrawableWrapperState
    {
        DrawableWrapperStateLollipop(@Nullable final DrawableWrapperState drawableWrapperState, @Nullable final Resources resources) {
            super(drawableWrapperState, resources);
        }
        
        @NonNull
        @Override
        public Drawable newDrawable(@Nullable final Resources resources) {
            return new WrappedDrawableApi21(this, resources);
        }
    }
}
