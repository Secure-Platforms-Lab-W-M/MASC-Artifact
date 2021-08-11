// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.net.Uri;
import android.graphics.drawable.Icon;
import android.graphics.Bitmap;
import android.support.annotation.DrawableRes;
import android.graphics.drawable.Drawable;
import android.graphics.PorterDuff$Mode;
import android.support.annotation.RestrictTo;
import android.support.annotation.Nullable;
import android.content.res.ColorStateList;
import android.view.View;
import android.util.AttributeSet;
import android.content.Context;
import android.support.v4.widget.TintableImageSourceView;
import android.support.v4.view.TintableBackgroundView;
import android.widget.ImageView;

public class AppCompatImageView extends ImageView implements TintableBackgroundView, TintableImageSourceView
{
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    private final AppCompatImageHelper mImageHelper;
    
    public AppCompatImageView(final Context context) {
        this(context, null);
    }
    
    public AppCompatImageView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public AppCompatImageView(final Context context, final AttributeSet set, final int n) {
        super(TintContextWrapper.wrap(context), set, n);
        (this.mBackgroundTintHelper = new AppCompatBackgroundHelper((View)this)).loadFromAttributes(set, n);
        (this.mImageHelper = new AppCompatImageHelper(this)).loadFromAttributes(set, n);
    }
    
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySupportBackgroundTint();
        }
        final AppCompatImageHelper mImageHelper = this.mImageHelper;
        if (mImageHelper != null) {
            mImageHelper.applySupportImageTint();
        }
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public ColorStateList getSupportBackgroundTintList() {
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            return mBackgroundTintHelper.getSupportBackgroundTintList();
        }
        return null;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public PorterDuff$Mode getSupportBackgroundTintMode() {
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            return mBackgroundTintHelper.getSupportBackgroundTintMode();
        }
        return null;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public ColorStateList getSupportImageTintList() {
        final AppCompatImageHelper mImageHelper = this.mImageHelper;
        if (mImageHelper != null) {
            return mImageHelper.getSupportImageTintList();
        }
        return null;
    }
    
    @Nullable
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public PorterDuff$Mode getSupportImageTintMode() {
        final AppCompatImageHelper mImageHelper = this.mImageHelper;
        if (mImageHelper != null) {
            return mImageHelper.getSupportImageTintMode();
        }
        return null;
    }
    
    public boolean hasOverlappingRendering() {
        return this.mImageHelper.hasOverlappingRendering() && super.hasOverlappingRendering();
    }
    
    public void setBackgroundDrawable(final Drawable backgroundDrawable) {
        super.setBackgroundDrawable(backgroundDrawable);
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundDrawable(backgroundDrawable);
        }
    }
    
    public void setBackgroundResource(@DrawableRes final int backgroundResource) {
        super.setBackgroundResource(backgroundResource);
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.onSetBackgroundResource(backgroundResource);
        }
    }
    
    public void setImageBitmap(final Bitmap imageBitmap) {
        super.setImageBitmap(imageBitmap);
        final AppCompatImageHelper mImageHelper = this.mImageHelper;
        if (mImageHelper != null) {
            mImageHelper.applySupportImageTint();
        }
    }
    
    public void setImageDrawable(@Nullable final Drawable imageDrawable) {
        super.setImageDrawable(imageDrawable);
        final AppCompatImageHelper mImageHelper = this.mImageHelper;
        if (mImageHelper != null) {
            mImageHelper.applySupportImageTint();
        }
    }
    
    public void setImageIcon(@Nullable final Icon imageIcon) {
        super.setImageIcon(imageIcon);
        final AppCompatImageHelper mImageHelper = this.mImageHelper;
        if (mImageHelper != null) {
            mImageHelper.applySupportImageTint();
        }
    }
    
    public void setImageResource(@DrawableRes final int imageResource) {
        final AppCompatImageHelper mImageHelper = this.mImageHelper;
        if (mImageHelper != null) {
            mImageHelper.setImageResource(imageResource);
        }
    }
    
    public void setImageURI(@Nullable final Uri imageURI) {
        super.setImageURI(imageURI);
        final AppCompatImageHelper mImageHelper = this.mImageHelper;
        if (mImageHelper != null) {
            mImageHelper.applySupportImageTint();
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportBackgroundTintList(@Nullable final ColorStateList supportBackgroundTintList) {
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.setSupportBackgroundTintList(supportBackgroundTintList);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportBackgroundTintMode(@Nullable final PorterDuff$Mode supportBackgroundTintMode) {
        final AppCompatBackgroundHelper mBackgroundTintHelper = this.mBackgroundTintHelper;
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.setSupportBackgroundTintMode(supportBackgroundTintMode);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportImageTintList(@Nullable final ColorStateList supportImageTintList) {
        final AppCompatImageHelper mImageHelper = this.mImageHelper;
        if (mImageHelper != null) {
            mImageHelper.setSupportImageTintList(supportImageTintList);
        }
    }
    
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public void setSupportImageTintMode(@Nullable final PorterDuff$Mode supportImageTintMode) {
        final AppCompatImageHelper mImageHelper = this.mImageHelper;
        if (mImageHelper != null) {
            mImageHelper.setSupportImageTintMode(supportImageTintMode);
        }
    }
}
