// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.appcompat.widget;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.R$styleable;
import android.util.AttributeSet;
import android.graphics.drawable.RippleDrawable;
import android.os.Build$VERSION;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import androidx.core.widget.ImageViewCompat;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class AppCompatImageHelper
{
    private TintInfo mImageTint;
    private TintInfo mInternalImageTint;
    private TintInfo mTmpInfo;
    private final ImageView mView;
    
    public AppCompatImageHelper(final ImageView mView) {
        this.mView = mView;
    }
    
    private boolean applyFrameworkTintUsingColorFilter(final Drawable drawable) {
        if (this.mTmpInfo == null) {
            this.mTmpInfo = new TintInfo();
        }
        final TintInfo mTmpInfo = this.mTmpInfo;
        mTmpInfo.clear();
        final ColorStateList imageTintList = ImageViewCompat.getImageTintList(this.mView);
        if (imageTintList != null) {
            mTmpInfo.mHasTintList = true;
            mTmpInfo.mTintList = imageTintList;
        }
        final PorterDuff$Mode imageTintMode = ImageViewCompat.getImageTintMode(this.mView);
        if (imageTintMode != null) {
            mTmpInfo.mHasTintMode = true;
            mTmpInfo.mTintMode = imageTintMode;
        }
        if (!mTmpInfo.mHasTintList && !mTmpInfo.mHasTintMode) {
            return false;
        }
        AppCompatDrawableManager.tintDrawable(drawable, mTmpInfo, this.mView.getDrawableState());
        return true;
    }
    
    private boolean shouldApplyFrameworkTintUsingColorFilter() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        if (sdk_INT > 21) {
            return this.mInternalImageTint != null;
        }
        return sdk_INT == 21;
    }
    
    void applySupportImageTint() {
        final Drawable drawable = this.mView.getDrawable();
        if (drawable != null) {
            DrawableUtils.fixDrawable(drawable);
        }
        if (drawable != null) {
            if (this.shouldApplyFrameworkTintUsingColorFilter() && this.applyFrameworkTintUsingColorFilter(drawable)) {
                return;
            }
            final TintInfo mImageTint = this.mImageTint;
            if (mImageTint != null) {
                AppCompatDrawableManager.tintDrawable(drawable, mImageTint, this.mView.getDrawableState());
                return;
            }
            final TintInfo mInternalImageTint = this.mInternalImageTint;
            if (mInternalImageTint != null) {
                AppCompatDrawableManager.tintDrawable(drawable, mInternalImageTint, this.mView.getDrawableState());
            }
        }
    }
    
    ColorStateList getSupportImageTintList() {
        final TintInfo mImageTint = this.mImageTint;
        if (mImageTint != null) {
            return mImageTint.mTintList;
        }
        return null;
    }
    
    PorterDuff$Mode getSupportImageTintMode() {
        final TintInfo mImageTint = this.mImageTint;
        if (mImageTint != null) {
            return mImageTint.mTintMode;
        }
        return null;
    }
    
    boolean hasOverlappingRendering() {
        final Drawable background = this.mView.getBackground();
        return Build$VERSION.SDK_INT < 21 || !(background instanceof RippleDrawable);
    }
    
    public void loadFromAttributes(final AttributeSet set, int resourceId) {
        final TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), set, R$styleable.AppCompatImageView, resourceId, 0);
        try {
            Drawable drawable2;
            final Drawable drawable = drawable2 = this.mView.getDrawable();
            if (drawable == null) {
                resourceId = obtainStyledAttributes.getResourceId(R$styleable.AppCompatImageView_srcCompat, -1);
                drawable2 = drawable;
                if (resourceId != -1) {
                    final Drawable drawable3 = AppCompatResources.getDrawable(this.mView.getContext(), resourceId);
                    if ((drawable2 = drawable3) != null) {
                        this.mView.setImageDrawable(drawable3);
                        drawable2 = drawable3;
                    }
                }
            }
            if (drawable2 != null) {
                DrawableUtils.fixDrawable(drawable2);
            }
            if (obtainStyledAttributes.hasValue(R$styleable.AppCompatImageView_tint)) {
                ImageViewCompat.setImageTintList(this.mView, obtainStyledAttributes.getColorStateList(R$styleable.AppCompatImageView_tint));
            }
            if (obtainStyledAttributes.hasValue(R$styleable.AppCompatImageView_tintMode)) {
                ImageViewCompat.setImageTintMode(this.mView, DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(R$styleable.AppCompatImageView_tintMode, -1), null));
            }
        }
        finally {
            obtainStyledAttributes.recycle();
        }
    }
    
    public void setImageResource(final int n) {
        if (n != 0) {
            final Drawable drawable = AppCompatResources.getDrawable(this.mView.getContext(), n);
            if (drawable != null) {
                DrawableUtils.fixDrawable(drawable);
            }
            this.mView.setImageDrawable(drawable);
        }
        else {
            this.mView.setImageDrawable((Drawable)null);
        }
        this.applySupportImageTint();
    }
    
    void setInternalImageTint(final ColorStateList mTintList) {
        if (mTintList != null) {
            if (this.mInternalImageTint == null) {
                this.mInternalImageTint = new TintInfo();
            }
            this.mInternalImageTint.mTintList = mTintList;
            this.mInternalImageTint.mHasTintList = true;
        }
        else {
            this.mInternalImageTint = null;
        }
        this.applySupportImageTint();
    }
    
    void setSupportImageTintList(final ColorStateList mTintList) {
        if (this.mImageTint == null) {
            this.mImageTint = new TintInfo();
        }
        this.mImageTint.mTintList = mTintList;
        this.mImageTint.mHasTintList = true;
        this.applySupportImageTint();
    }
    
    void setSupportImageTintMode(final PorterDuff$Mode mTintMode) {
        if (this.mImageTint == null) {
            this.mImageTint = new TintInfo();
        }
        this.mImageTint.mTintMode = mTintMode;
        this.mImageTint.mHasTintMode = true;
        this.applySupportImageTint();
    }
}
