// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.graphics.drawable;

import android.os.Build$VERSION;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable$ConstantState;

final class WrappedDrawableState extends Drawable$ConstantState
{
    int mChangingConfigurations;
    Drawable$ConstantState mDrawableState;
    ColorStateList mTint;
    PorterDuff$Mode mTintMode;
    
    WrappedDrawableState(final WrappedDrawableState wrappedDrawableState) {
        this.mTint = null;
        this.mTintMode = WrappedDrawableApi14.DEFAULT_TINT_MODE;
        if (wrappedDrawableState != null) {
            this.mChangingConfigurations = wrappedDrawableState.mChangingConfigurations;
            this.mDrawableState = wrappedDrawableState.mDrawableState;
            this.mTint = wrappedDrawableState.mTint;
            this.mTintMode = wrappedDrawableState.mTintMode;
        }
    }
    
    boolean canConstantState() {
        return this.mDrawableState != null;
    }
    
    public int getChangingConfigurations() {
        final int mChangingConfigurations = this.mChangingConfigurations;
        final Drawable$ConstantState mDrawableState = this.mDrawableState;
        int changingConfigurations;
        if (mDrawableState != null) {
            changingConfigurations = mDrawableState.getChangingConfigurations();
        }
        else {
            changingConfigurations = 0;
        }
        return mChangingConfigurations | changingConfigurations;
    }
    
    public Drawable newDrawable() {
        return this.newDrawable(null);
    }
    
    public Drawable newDrawable(final Resources resources) {
        if (Build$VERSION.SDK_INT >= 21) {
            return new WrappedDrawableApi21(this, resources);
        }
        return new WrappedDrawableApi14(this, resources);
    }
}
