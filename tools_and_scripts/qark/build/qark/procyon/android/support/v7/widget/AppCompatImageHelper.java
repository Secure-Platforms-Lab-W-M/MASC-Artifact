// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v7.widget;

import android.support.v7.content.res.AppCompatResources;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.graphics.drawable.RippleDrawable;
import android.os.Build$VERSION;
import android.graphics.PorterDuff$Mode;
import android.content.res.ColorStateList;
import android.support.v4.widget.ImageViewCompat;
import android.support.annotation.NonNull;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class AppCompatImageHelper
{
    private TintInfo mImageTint;
    private TintInfo mInternalImageTint;
    private TintInfo mTmpInfo;
    private final ImageView mView;
    
    public AppCompatImageHelper(final ImageView mView) {
        this.mView = mView;
    }
    
    private boolean applyFrameworkTintUsingColorFilter(@NonNull final Drawable drawable) {
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
        if (drawable == null) {
            return;
        }
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
        TintTypedArray obtainStyledAttributes;
        Drawable drawable;
        Label_0081_Outer:Label_0108_Outer:
        while (true) {
            obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), set, R.styleable.AppCompatImageView, resourceId, 0);
            while (true) {
            Label_0164:
                while (true) {
                Label_0161:
                    while (true) {
                        Label_0158: {
                            Label_0155: {
                                Label_0152: {
                                    try {
                                        if (this.mView.getDrawable() != null) {
                                            break Label_0158;
                                        }
                                        resourceId = obtainStyledAttributes.getResourceId(R.styleable.AppCompatImageView_srcCompat, -1);
                                        if (resourceId == -1) {
                                            break Label_0155;
                                        }
                                        drawable = AppCompatResources.getDrawable(this.mView.getContext(), resourceId);
                                        if (drawable == null) {
                                            break Label_0152;
                                        }
                                        this.mView.setImageDrawable(drawable);
                                        if (drawable == null) {
                                            break Label_0161;
                                        }
                                        DrawableUtils.fixDrawable(drawable);
                                        if (obtainStyledAttributes.hasValue(R.styleable.AppCompatImageView_tint)) {
                                            ImageViewCompat.setImageTintList(this.mView, obtainStyledAttributes.getColorStateList(R.styleable.AppCompatImageView_tint));
                                            if (obtainStyledAttributes.hasValue(R.styleable.AppCompatImageView_tintMode)) {
                                                ImageViewCompat.setImageTintMode(this.mView, DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(R.styleable.AppCompatImageView_tintMode, -1), null));
                                            }
                                            return;
                                        }
                                        break Label_0164;
                                    }
                                    finally {
                                        obtainStyledAttributes.recycle();
                                    }
                                }
                                continue Label_0081_Outer;
                            }
                            continue Label_0081_Outer;
                        }
                        continue Label_0081_Outer;
                    }
                    continue Label_0108_Outer;
                }
                continue;
            }
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
            final TintInfo mInternalImageTint = this.mInternalImageTint;
            mInternalImageTint.mTintList = mTintList;
            mInternalImageTint.mHasTintList = true;
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
        final TintInfo mImageTint = this.mImageTint;
        mImageTint.mTintList = mTintList;
        mImageTint.mHasTintList = true;
        this.applySupportImageTint();
    }
    
    void setSupportImageTintMode(final PorterDuff$Mode mTintMode) {
        if (this.mImageTint == null) {
            this.mImageTint = new TintInfo();
        }
        final TintInfo mImageTint = this.mImageTint;
        mImageTint.mTintMode = mTintMode;
        mImageTint.mHasTintMode = true;
        this.applySupportImageTint();
    }
}
